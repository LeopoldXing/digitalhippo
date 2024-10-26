variable "redis_node_type" {
  description = "The instance class used for the Redis cluster"
  type        = string
  default     = "cache.t3.micro"
}

variable "redis_engine_version" {
  description = "Redis engine version"
  type        = string
  default     = "6.x"
}

resource "aws_elasticache_subnet_group" "redis_subnet_group" {
  name = "digitalhippo-redis-subnet-group"
  subnet_ids = [
    aws_subnet.private-subnet-ca-central-1b.id,
    aws_subnet.private-subnet-ca-central-1a.id
  ]

  tags = {
    Name = "digitalhippo-redis-subnet-group"
  }
}

resource "aws_security_group" "redis_sg" {
  name        = "digitalhippo-redis-sg"
  description = "Security group for ElastiCache Redis cluster"
  vpc_id      = aws_vpc.digitalhippo-vpc.id

  ingress {
    from_port = 6379
    to_port   = 6379
    protocol  = "tcp"
    security_groups = [aws_security_group.eks_sg.id]
  }

  egress {
    from_port = 0
    to_port   = 0
    protocol  = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = {
    Name = "digitalhippo-redis-sg"
  }
}

resource "aws_elasticache_replication_group" "redis_replication_group" {
  replication_group_id       = "digitalhippo-redis-rg"
  engine                     = "redis"
  engine_version             = var.redis_engine_version
  node_type                  = var.redis_node_type
  num_cache_clusters         = 1
  automatic_failover_enabled = false
  transit_encryption_enabled = false
  at_rest_encryption_enabled = false
  subnet_group_name          = aws_elasticache_subnet_group.redis_subnet_group.name
  security_group_ids = [aws_security_group.redis_sg.id]
  port                       = 6379

  tags = {
    Name = "digitalhippo-redis-rg"
  }

  depends_on = [aws_elasticache_subnet_group.redis_subnet_group]
}
