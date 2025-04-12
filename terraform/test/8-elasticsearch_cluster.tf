###############################
# 8-elasticsearch_cluster.tf
###############################

# 假设以下变量在其他模板中已定义并输出：
# - aws_vpc.digitalhippo_vpc.id
# - var.vpc_cidr
# - aws_subnet.private[*].id
#
# 新增变量 elasticsearch_version 在 variables.tf 中定义，例如：
#
# variable "elasticsearch_version" {
#   description = "Elasticsearch 版本，默认 8.14.1"
#   type        = string
#   default     = "8.14.1"
# }

#################################################
# 安全组：用于 Elasticsearch 节点之间和外部调用
#################################################
resource "aws_security_group" "elasticsearch_sg" {
  name        = "${var.elasticsearch_cluster_name}-sg"
  description = "Security group for Elasticsearch cluster"
  vpc_id      = aws_vpc.digitalhippo_vpc.id

  # 开放 HTTP 端口（9200）
  ingress {
    description = "Allow Elasticsearch HTTP (9200) access within VPC"
    from_port   = 9200
    to_port     = 9200
    protocol    = "tcp"
    cidr_blocks = [var.vpc_cidr]
  }

  # 开放集群内通信使用的传输端口（9300）
  ingress {
    description = "Allow Elasticsearch transport (9300) access within VPC"
    from_port   = 9300
    to_port     = 9300
    protocol    = "tcp"
    cidr_blocks = [var.vpc_cidr]
  }

  # 可选：允许 SSH 访问，建议在实际环境中限制来源 IP
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
# 部署 Elasticsearch 节点的 EC2 实例
#################################################
resource "aws_instance" "elasticsearch_node" {
  count         = var.elasticsearch_instance_count
  ami           = var.elasticsearch_ami
  instance_type = var.elasticsearch_instance_type
  key_name      = var.elasticsearch_key_name

  # 部署在私有子网中；若私有子网数不足，则采用轮转机制
  subnet_id = element(aws_subnet.private[*].id, count.index % length(aws_subnet.private))

  vpc_security_group_ids = [
    aws_security_group.elasticsearch_sg.id
  ]

  # Root Block Device 设置
  root_block_device {
    volume_size = 50
    volume_type = "gp3"
  }

  # 附加用于 Elasticsearch 数据存储的 EBS 卷
  ebs_block_device {
    device_name           = "/dev/sdf"
    volume_size           = var.elasticsearch_volume_size
    volume_type           = "gp3"
    delete_on_termination = true
  }

  # 利用 user_data 脚本自动安装和启动 Elasticsearch
  user_data = <<-EOF
    #!/bin/bash
    set -ex
    # 更新系统并安装必要的软件包
    sudo yum update -y
    sudo amazon-linux-extras install java-openjdk11 -y

    # 下载并安装 Elasticsearch ${var.elasticsearch_version}
    wget https://artifacts.elastic.co/downloads/elasticsearch/elasticsearch-${var.elasticsearch_version}-linux-x86_64.tar.gz -O /tmp/es.tar.gz
    sudo tar -xzf /tmp/es.tar.gz -C /opt
    sudo mv /opt/elasticsearch-${var.elasticsearch_version} /opt/elasticsearch

    # 创建专用用户并设置权限
    sudo useradd elasticsearch -s /sbin/nologin
    sudo chown -R elasticsearch:elasticsearch /opt/elasticsearch
    sudo mkdir -p /var/lib/elasticsearch
    sudo chown elasticsearch:elasticsearch /var/lib/elasticsearch

    # 生成 Elasticsearch 配置文件
    cat <<EOC > /opt/elasticsearch/config/elasticsearch.yml
    cluster.name: "${var.elasticsearch_cluster_name}"
    node.name: "node-${count.index + 1}"
    network.host: 0.0.0.0
    http.port: 9200
    discovery.seed_hosts: [${join(", ", formatlist("\"%s\"", aws_instance.elasticsearch_node[*].private_ip))}]
    cluster.initial_master_nodes: [${join(", ", ["\"node-1\"", "\"node-2\"", "\"node-3\""])}]
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

  tags = {
    Name = "${var.elasticsearch_cluster_name}-node-${count.index + 1}"
  }
}

#################################################
# 输出 Elasticsearch 节点的私有 IP 地址
#################################################
output "elasticsearch_node_private_ips" {
  description = "Elasticsearch 节点的私有 IP 列表"
  value       = aws_instance.elasticsearch_node[*].private_ip
}
