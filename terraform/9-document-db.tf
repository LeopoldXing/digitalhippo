variable "docdb_instance_class" {
  description = "The instance class used for the DocumentDB cluster"
  type        = string
  default     = "db.r5.large"
}

variable "docdb_engine_version" {
  description = "DocumentDB engine version"
  type        = string
  default     = "4.0"
}

variable "docdb_username" {
  description = "Username for the DocumentDB cluster"
  type        = string
  default     = "admin"
}

variable "docdb_password" {
  description = "Password for the DocumentDB cluster"
  type        = string
}

resource "aws_docdb_subnet_group" "docdb_subnet_group" {
  name       = "digitalhippo-docdb-subnet-group"
  subnet_ids = [
    aws_subnet.private-subnet-ca-central-1a.id,
    aws_subnet.private-subnet-ca-central-1b.id
  ]

  tags = {
    Name = "digitalhippo-docdb-subnet-group"
  }
}

resource "aws_security_group" "docdb_sg" {
  name        = "digitalhippo-docdb-sg"
  description = "Security group for Amazon DocumentDB cluster"
  vpc_id      = aws_vpc.digitalhippo-vpc.id

  ingress {
    from_port       = 27017
    to_port         = 27017
    protocol        = "tcp"
    security_groups = [aws_security_group.eks_sg.id]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = {
    Name = "digitalhippo-docdb-sg"
  }
}

# 创建Amazon DocumentDB集群
resource "aws_docdb_cluster" "docdb_cluster" {
  cluster_identifier = "digitalhippo-docdb-cluster"
  engine             = "docdb"
  engine_version     = var.docdb_engine_version
  master_username    = var.docdb_username
  master_password    = var.docdb_password
  vpc_security_group_ids = [aws_security_group.docdb_sg.id]
  db_subnet_group_name   = aws_docdb_subnet_group.docdb_subnet_group.name
  storage_encrypted      = true
  skip_final_snapshot    = true

  tags = {
    Name = "digitalhippo-docdb-cluster"
  }
}

resource "aws_docdb_cluster_instance" "docdb_instance" {
  cluster_identifier = aws_docdb_cluster.docdb_cluster.id
  identifier         = "digitalhippo-docdb-instance-1"
  instance_class     = var.docdb_instance_class
  engine             = aws_docdb_cluster.docdb_cluster.engine
  engine_version     = aws_docdb_cluster.docdb_cluster.engine_version

  tags = {
    Name = "digitalhippo-docdb-instance-1"
  }
}
