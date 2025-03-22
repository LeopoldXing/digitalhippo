resource "aws_internet_gateway" "igw" {
  vpc_id = aws_vpc.digitalhippo-vpc.id

  tags = {
    Name = "igw"
  }
}

# security group for eks
resource "aws_security_group" "eks_sg" {
  name        = "eks-cluster-sg"
  description = "Security group for all nodes in the EKS cluster"
  vpc_id      = aws_vpc.digitalhippo-vpc.id

  ingress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["10.0.0.0/16"]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}