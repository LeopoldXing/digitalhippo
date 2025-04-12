###################################
# 1. 数据库子网组
###################################
resource "aws_db_subnet_group" "aurora_subnets" {
  name       = "${var.db_cluster_identifier}-subnet-group"
  subnet_ids = aws_subnet.private[*].id

  tags = {
    Name = "${var.db_cluster_identifier}-subnet-group"
  }
}

###################################
# 2. Aurora 集群参数组 （可选，使用 dev/test 模板）
###################################
resource "aws_rds_cluster_parameter_group" "aurora_pg_param_group" {
  name        = "${var.db_cluster_identifier}-param-group"
  family      = "aurora-postgresql16"
  description = "Parameter group for dev/test environment for Aurora PostgreSQL 16"
}

###################################
# 3. Aurora RDS 集群
###################################
resource "aws_rds_cluster" "aurora_cluster" {
  cluster_identifier   = var.db_cluster_identifier
  engine               = "aurora-postgresql"
  engine_version       = "16.6"
  engine_mode = "provisioned"  # Aurora Serverless v2 目前配置在 provisioned 集群中加入 serverless scaling block
  database_name        = "digitalhippo"
  master_username      = var.db_master_username
  master_password      = var.db_master_password
  db_subnet_group_name = aws_db_subnet_group.aurora_subnets.name
  storage_encrypted    = true
  skip_final_snapshot  = true

  serverlessv2_scaling_configuration {
    min_capacity             = var.serverless_min_capacity
    max_capacity             = var.serverless_max_capacity
  }

  tags = {
    Name = var.db_cluster_identifier
  }
}

###################################
# 4. Aurora 集群实例（至少一个写实例及两个读实例位于不同 AZ）
###################################
# 写实例（主节点）
resource "aws_rds_cluster_instance" "aurora_instance_writer" {
  identifier          = "${var.db_cluster_identifier}-writer"
  cluster_identifier  = aws_rds_cluster.aurora_cluster.id
  instance_class      = var.db_instance_class
  engine              = aws_rds_cluster.aurora_cluster.engine
  engine_version      = aws_rds_cluster.aurora_cluster.engine_version
  publicly_accessible = false
  availability_zone = element(data.aws_availability_zones.available.names, 0)

  tags = {
    Name = "${var.db_cluster_identifier}-writer"
  }
}

# 读实例（Aurora Replica 或 Reader，建议放在不同 AZ）
resource "aws_rds_cluster_instance" "aurora_instance_reader" {
  count              = 2
  identifier         = "${var.db_cluster_identifier}-reader-${count.index + 1}"
  cluster_identifier = aws_rds_cluster.aurora_cluster.id
  instance_class     = var.db_instance_class
  engine             = aws_rds_cluster.aurora_cluster.engine
  engine_version     = aws_rds_cluster.aurora_cluster.engine_version
  publicly_accessible = false
  # 选择可用区：如果 data.aws_availability_zones.available.names 至少包含 3 个 AZ（writer 用了第 0 个，则 reader 用第 1 和第 2 个）
  availability_zone = element(data.aws_availability_zones.available.names, count.index + 1)

  tags = {
    Name = "${var.db_cluster_identifier}-reader-${count.index + 1}"
  }
}

###################################
# 5. 创建 Secrets Manager 用于 RDS Proxy 的凭证存储
###################################
resource "aws_secretsmanager_secret" "rds_proxy_secret" {
  name = "${var.db_cluster_identifier}-rds-proxy-secret"
}

resource "aws_secretsmanager_secret_version" "rds_proxy_secret" {
  secret_id = aws_secretsmanager_secret.rds_proxy_secret.id
  secret_string = jsonencode({
    username = var.db_master_username,
    password = var.db_master_password
  })
}

###################################
# 6. RDS Proxy
###################################
resource "aws_db_proxy" "aurora_proxy" {
  name                  = "${var.db_cluster_identifier}-proxy"
  debug_logging         = false
  engine_family         = "POSTGRESQL"
  idle_client_timeout   = 1800
  require_tls           = true
  role_arn              = aws_iam_role.rds_proxy_role.arn
  vpc_subnet_ids        = aws_subnet.private[*].id

  auth {
    auth_scheme = "SECRETS"
    iam_auth    = "DISABLED"
    secret_arn  = aws_secretsmanager_secret_version.rds_proxy_secret.arn
  }

  tags = {
    Name = "${var.db_cluster_identifier}-proxy"
  }
}
