###################################
# 1. 创建 RDS Proxy 的 IAM Role
###################################
resource "aws_iam_role" "rds_proxy_role" {
  name = "${var.db_cluster_identifier}-rds-proxy-role"
  assume_role_policy = jsonencode({
    Version = "2012-10-17",
    Statement = [
      {
        Effect = "Allow",
        Principal = {
          Service = "rds.amazonaws.com"
        },
        Action = "sts:AssumeRole"
      }
    ]
  })
}

# 附加一个内联策略，允许 RDS Proxy 读取 Secrets Manager 中的凭证
resource "aws_iam_policy" "rds_proxy_policy" {
  name   = "${var.db_cluster_identifier}-rds-proxy-policy"
  policy = jsonencode({
    Version = "2012-10-17",
    Statement = [
      {
        Effect = "Allow",
        Action = [
          "secretsmanager:GetSecretValue",
          "secretsmanager:DescribeSecret"
        ],
        Resource = "*"  # 如有需要，可进一步限制到具体的 Secret ARN
      }
    ]
  })
}

resource "aws_iam_role_policy_attachment" "rds_proxy_policy_attach" {
  role       = aws_iam_role.rds_proxy_role.name
  policy_arn = aws_iam_policy.rds_proxy_policy.arn
}

###################################
# 2. 创建用于 RDS Proxy 的安全组
###################################
resource "aws_security_group" "rds_proxy_sg" {
  name        = "${var.db_cluster_identifier}-rds-proxy-sg"
  description = "Security group for RDS Proxy"
  vpc_id      = aws_vpc.digitalhippo_vpc.id

  # 允许来自 VPC 内（例如来自私有子网）的流量访问 RDS Proxy 默认的 PostgreSQL 端口（5432）
  ingress {
    from_port   = 5432
    to_port     = 5432
    protocol    = "tcp"
    cidr_blocks = [var.vpc_cidr]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = {
    Name = "${var.db_cluster_identifier}-rds-proxy-sg"
  }
}

###################################
# 3. eks service role
###################################
resource "aws_iam_role" "digitalhippo-eks-service-role" {
  name = "digitalhippo-eks-service-role"

  assume_role_policy = jsonencode({
    Version = "2012-10-17",
    Statement = [
      {
        Effect = "Allow",
        Action = "sts:AssumeRole",
        Principal = {
          Service = "eks.amazonaws.com"
        }
      }
    ]
  })
}