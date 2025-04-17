#################################################
# Elasticsearch Cluster Terraform Template (方案1)
#################################################

# 数据源：获取最新的 Amazon Linux 2 AMI
data "aws_ami" "amazon_linux_2" {
  most_recent = true
  owners = ["amazon"]

  filter {
    name = "name"
    values = ["amzn2-ami-hvm-2.0.*-x86_64-gp2"]
  }

  filter {
    name = "virtualization-type"
    values = ["hvm"]
  }
}

# 安全组：允许 Elasticsearch 内部通信、HTTP、SSH
resource "aws_security_group" "elasticsearch_sg" {
  name        = "${var.elasticsearch_cluster_name}-sg"
  description = "Security group for Elasticsearch cluster"
  vpc_id      = aws_vpc.digitalhippo_vpc.id

  ingress {
    description = "Allow Elasticsearch HTTP (9200) access within VPC"
    from_port   = 9200
    to_port     = 9200
    protocol    = "tcp"
    cidr_blocks = [var.vpc_cidr]
  }

  ingress {
    description = "Allow Elasticsearch transport (9300) access within VPC"
    from_port   = 9300
    to_port     = 9300
    protocol    = "tcp"
    cidr_blocks = [var.vpc_cidr]
  }

  ingress {
    description = "Allow SSH access"
    from_port   = 22
    to_port     = 22
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
    description = "Allow all outbound traffic"
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = {
    Name = "${var.elasticsearch_cluster_name}-sg"
  }
}

# 内部 NLB 作为 Elasticsearch 统一访问入口
resource "aws_lb" "elasticsearch_lb" {
  name               = "${var.elasticsearch_cluster_name}-lb"
  internal           = true
  load_balancer_type = "network"
  subnets            = aws_subnet.private[*].id
  security_groups = [aws_security_group.elasticsearch_sg.id]

  tags = {
    Name = "${var.elasticsearch_cluster_name}-lb"
  }
}

# NLB 目标组配置（转发到 ES 节点的 9200 端口）
resource "aws_lb_target_group" "elasticsearch_tg" {
  name     = "${var.elasticsearch_cluster_name}-tg"
  port     = 9200
  protocol = "TCP"
  vpc_id   = aws_vpc.digitalhippo_vpc.id

  health_check {
    protocol            = "HTTP"
    port                = "9200"
    path                = "/"
    healthy_threshold   = 2
    unhealthy_threshold = 2
    interval            = 30
    timeout             = 10
  }

  tags = {
    Name = "${var.elasticsearch_cluster_name}-tg"
  }
}

# NLB Listener：将流量转发到目标组
resource "aws_lb_listener" "elasticsearch_listener" {
  load_balancer_arn = aws_lb.elasticsearch_lb.arn
  port              = 9200
  protocol          = "TCP"

  default_action {
    type             = "forward"
    target_group_arn = aws_lb_target_group.elasticsearch_tg.arn
  }
}

# 创建 Elasticsearch 节点：EC2 实例
resource "aws_instance" "elasticsearch_node" {
  count = var.elasticsearch_node_count
  ami   = data.aws_ami.amazon_linux_2.id
  instance_type = var.elasticsearch_instance_type
  # 根据索引均匀分布到已有的私有子网中
  subnet_id = element(aws_subnet.private[*].id, count.index % length(aws_subnet.private))
  vpc_security_group_ids = [
    aws_security_group.elasticsearch_sg.id
  ]

  # 根设备配置
  root_block_device {
    volume_size = 50
    volume_type = "gp3"
  }

  # 附加 EBS 卷用于存储 Elasticsearch 数据
  ebs_block_device {
    device_name           = "/dev/sdf"
    volume_size           = var.elasticsearch_volume_size
    volume_type           = "gp3"
    delete_on_termination = true
  }

  # 使用优化过的 user_data，不引用同一资源的 private_ip，
  # discovery.seed_hosts 这里置空（[]），节点启动后可通过其他方式动态发现
  user_data = base64encode(<<-EOF
    #!/bin/bash
    set -ex

    # 系统更新及安装 Java 11
    sudo yum update -y
    sudo amazon-linux-extras install java-openjdk11 -y

    # 下载并安装 Elasticsearch
    wget https://artifacts.elastic.co/downloads/elasticsearch/elasticsearch-8.14.1-linux-x86_64.tar.gz -O /tmp/es.tar.gz
    sudo tar -xzf /tmp/es.tar.gz -C /opt
    sudo mv /opt/elasticsearch-8.14.1 /opt/elasticsearch

    # 创建专用用户并调整目录权限
    sudo useradd elasticsearch -s /sbin/nologin || true
    sudo mkdir -p /var/lib/elasticsearch
    sudo chown -R elasticsearch:elasticsearch /opt/elasticsearch /var/lib/elasticsearch

    # 获取当前实例 ID 作为节点名称
    NODE_NAME=$(curl -s http://169.254.169.254/latest/meta-data/instance-id)

    # 写入初始 Elasticsearch 配置文件
    # 注意：discovery.seed_hosts 设置为空，后续可通过额外脚本或服务发现更新为正确的种子节点列表
    sudo tee /opt/elasticsearch/config/elasticsearch.yml > /dev/null <<EOC
cluster.name: "${var.elasticsearch_cluster_name}"
node.name: "$${NODE_NAME}"
network.host: 0.0.0.0
http.port: 9200
discovery.seed_hosts: []
path.data: /var/lib/elasticsearch
EOC

    # 设置 systemd 服务管理 Elasticsearch
    sudo tee /etc/systemd/system/elasticsearch.service > /dev/null <<EOF2
[Unit]
Description=Elasticsearch
After=network.target

[Service]
Type=simple
User=elasticsearch
ExecStart=/opt/elasticsearch/bin/elasticsearch
Restart=on-failure

[Install]
WantedBy=multi-user.target
EOF2

    sudo systemctl daemon-reload
    sudo systemctl enable elasticsearch
    sudo systemctl start elasticsearch
  EOF
  )

  tags = {
    Name = "${var.elasticsearch_cluster_name}-node-${count.index + 1}"
  }
}

# 将 Elasticsearch 节点注册到 NLB 目标组
resource "aws_lb_target_group_attachment" "elasticsearch_tg_attachment" {
  count            = var.elasticsearch_node_count
  target_group_arn = aws_lb_target_group.elasticsearch_tg.arn
  target_id        = aws_instance.elasticsearch_node[count.index].id
  port             = 9200
}
