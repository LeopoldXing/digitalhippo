###############################################
# redis-elasticache.tf
###############################################

# 此处直接引用之前创建的 VPC 资源与私有子网
# - aws_vpc.digitalhippo_vpc.id
# - aws_subnet.private[*].id
# 同时使用变量 vpc_cidr 来设置安全组规则

##############################################################
# 创建 Redis 子网组：使用之前创建的私有子网
##############################################################
resource "aws_elasticache_subnet_group" "redis_subnet_group" {
  name        = "${var.redis_cluster_name}-subnet-group"
  description = "Subnet group for Redis cluster"
  subnet_ids  = aws_subnet.private[*].id

  tags = {
    Name = "${var.redis_cluster_name}-subnet-group"
  }
}

##############################################################
# 创建用于 Redis 的安全组
##############################################################
resource "aws_security_group" "redis_elasticache_sg" {
  name        = "${var.redis_cluster_name}-sg"
  description = "Security group for Redis Elasticache cluster"
  vpc_id      = aws_vpc.digitalhippo_vpc.id

  ingress {
    description = "Allow access on Redis port 6379 within VPC"
    from_port   = 6379
    to_port     = 6379
    protocol    = "tcp"
    cidr_blocks = [var.vpc_cidr]
  }

  egress {
    description = "Allow all outbound traffic"
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = {
    Name = "${var.redis_cluster_name}-sg"
  }
}

##############################################################
# 创建 ElastiCache Redis 集群
##############################################################
resource "aws_elasticache_serverless_cache" "valkeys_redis" {
  engine = "valkey"
  name   = "valkey-redis-cache"
  cache_usage_limits {
    data_storage {
      maximum = 10
      unit    = "GB"
    }
    ecpu_per_second {
      maximum = 5000
    }
  }
  daily_snapshot_time      = "09:00"
  description              = "Digitalhippo cache server"
  major_engine_version     = "7"
  snapshot_retention_limit = 1
  security_group_ids = [aws_security_group.redis_elasticache_sg.id]
  subnet_ids               = aws_elasticache_subnet_group.redis_subnet_group.subnet_ids


  tags = {
    Name = var.redis_cluster_name
  }
}
