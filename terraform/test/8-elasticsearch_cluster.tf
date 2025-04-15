###############################
# 8-elasticsearch_cluster.tf
###############################

#################################################
# 数据源：自动获取当前区域最新的 Amazon Linux 2 AMI
#################################################
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

#################################################
# 安全组：用于 Elasticsearch 节点之间及内部访问
#################################################
resource "aws_security_group" "elasticsearch_sg" {
  name        = "${var.elasticsearch_cluster_name}-sg"
  description = "Security group for Elasticsearch cluster"
  vpc_id      = aws_vpc.digitalhippo_vpc.id

  # 允许 HTTP 端口 9200 (供内部服务访问)
  ingress {
    description = "Allow Elasticsearch HTTP (9200) access within VPC"
    from_port   = 9200
    to_port     = 9200
    protocol    = "tcp"
    cidr_blocks = [var.vpc_cidr]
  }

  # 允许节点间传输数据时使用的 9300 端口
  ingress {
    description = "Allow Elasticsearch transport (9300) access within VPC"
    from_port   = 9300
    to_port     = 9300
    protocol    = "tcp"
    cidr_blocks = [var.vpc_cidr]
  }

  # 可选：允许 SSH（生产中建议限制来源，此处已开放）
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

#################################################
# 内部 NLB 作为统一访问入口
#################################################
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

resource "aws_lb_target_group" "elasticsearch_tg" {
  name   = "${var.elasticsearch_cluster_name}-tg"
  port   = 9200
  protocol = "HTTP"      # 由 TCP 改为 HTTP
  vpc_id = aws_vpc.digitalhippo_vpc.id

  health_check {
    protocol            = "HTTP"
    path = "/"   # 或者 "/_cluster/health" 如果 ES 返回健康状态
    matcher = "200-399"  # 接受 200 至 399 的状态码
    interval            = 60
    timeout             = 10
    healthy_threshold   = 3
    unhealthy_threshold = 3
  }

  tags = {
    Name = "${var.elasticsearch_cluster_name}-tg"
  }
}

resource "aws_lb_listener" "elasticsearch_listener" {
  load_balancer_arn = aws_lb.elasticsearch_lb.arn
  port              = 9200
  protocol          = "TCP"

  default_action {
    type             = "forward"
    target_group_arn = aws_lb_target_group.elasticsearch_tg.arn
  }
}

#################################################
# Elasticsearch 节点：使用固定数量的 EC2 实例
#################################################
resource "aws_instance" "elasticsearch_node" {
  count         = var.elasticsearch_node_count
  ami           = data.aws_ami.amazon_linux_2.id
  instance_type = var.elasticsearch_instance_type
  # 移除 key_name，因为不需要远程 SSH 连接
  # 将节点均匀分布到私有子网中
  subnet_id = element(aws_subnet.private[*].id, count.index % length(aws_subnet.private))

  vpc_security_group_ids = [
    aws_security_group.elasticsearch_sg.id
  ]

  # 根块设备配置
  root_block_device {
    volume_size = 50
    volume_type = "gp3"
  }

  # 附加用于存储 Elasticsearch 数据的 EBS 卷
  ebs_block_device {
    device_name           = "/dev/sdf"
    volume_size           = var.elasticsearch_volume_size
    volume_type = "gp3"
    # 设为 false，确保实例终止时数据不会被自动删除
    delete_on_termination = true
  }

  # user_data 脚本：自动安装并启动指定版本的 Elasticsearch
  user_data = base64encode(<<-EOF
    #!/bin/bash
    set -ex
    # 更新系统并安装 Java 11
    sudo yum update -y
    sudo amazon-linux-extras install java-openjdk11 -y

    # 下载并安装 Elasticsearch ${var.elasticsearch_version}
    wget https://artifacts.elastic.co/downloads/elasticsearch/elasticsearch-${var.elasticsearch_version}-linux-x86_64.tar.gz -O /tmp/es.tar.gz
    sudo tar -xzf /tmp/es.tar.gz -C /opt
    sudo mv /opt/elasticsearch-${var.elasticsearch_version} /opt/elasticsearch

    # 创建专用用户并设置目录权限
    sudo useradd elasticsearch -s /sbin/nologin || true
    sudo mkdir -p /var/lib/elasticsearch
    sudo chown -R elasticsearch:elasticsearch /opt/elasticsearch /var/lib/elasticsearch

    # 获取实例 ID 作为节点名称
    NODE_NAME=$(curl -s http://169.254.169.254/latest/meta-data/instance-id)

    # 生成 Elasticsearch 配置文件
    sudo tee /opt/elasticsearch/config/elasticsearch.yml > /dev/null <<EOC
    cluster.name: "${var.elasticsearch_cluster_name}"
    node.name: "$${NODE_NAME}"
    network.host: 0.0.0.0
    http.port: 9200
    # 配置 discovery.seed_hosts 为所有节点的私有 IP 地址
    discovery.seed_hosts: [$${join(", ", formatlist("\"%s\"", aws_instance.elasticsearch_node[*].private_ip))}]
    path.data: /var/lib/elasticsearch
    EOC

    # 配置 systemd 管理 Elasticsearch 服务
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

#################################################
# 注册固定节点到目标组
#################################################
resource "aws_lb_target_group_attachment" "elasticsearch_tg_attachment" {
  count            = var.elasticsearch_node_count
  target_group_arn = aws_lb_target_group.elasticsearch_tg.arn
  target_id        = aws_instance.elasticsearch_node[count.index].id
  port             = 9200
}

#################################################
# 输出统一的内部 Elasticsearch Endpoint
#################################################
output "elasticsearch_endpoint" {
  description = "统一的内部 Elasticsearch endpoint，供 EKS 使用"
  value       = aws_lb.elasticsearch_lb.dns_name
}
