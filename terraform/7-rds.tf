variable "db_name" {
  description = "The name of the RDS database"
  type        = string
  default     = "digitalhippo_db"
}

variable "db_username" {
  description = "Username for the RDS database"
  type        = string
  default     = "admin"
}

variable "db_password" {
  description = "Password for the RDS database"
  type        = string
}

variable "db_port" {
  description = "Port for the RDS database"
  type        = number
  default     = 5432
}


resource "aws_db_subnet_group" "digitalhippo_db_subnet_group" {
  name = "digitalhippo-db-subnet-group"
  subnet_ids = [
    aws_subnet.private-subnet-ca-central-1a.id,
    aws_subnet.private-subnet-ca-central-1b.id
  ]

  tags = {
    Name = "digitalhippo-db-subnet-group"
  }
}


resource "aws_security_group" "rds_sg" {
  name        = "digitalhippo-rds-sg"
  description = "Security group for the RDS instance"
  vpc_id      = aws_vpc.digitalhippo-vpc.id

  ingress {
    from_port = var.db_port
    to_port   = var.db_port
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
    Name = "digitalhippo-rds-sg"
  }
}

resource "aws_db_instance" "digitalhippo_db" {
  allocated_storage    = 20
  storage_type         = "gp2"
  engine               = "postgres"
  engine_version       = "13.4"
  instance_class       = "db.t4g.micro"
  db_name              = var.db_name
  username             = var.db_username
  password             = var.db_password
  port                 = var.db_port
  db_subnet_group_name = aws_db_subnet_group.digitalhippo_db_subnet_group.name
  vpc_security_group_ids = [aws_security_group.rds_sg.id]
  multi_az             = false
  publicly_accessible  = false
  skip_final_snapshot  = true

  tags = {
    Name = "digitalhippo-db"
  }

  depends_on = [aws_db_subnet_group.digitalhippo_db_subnet_group]
}
