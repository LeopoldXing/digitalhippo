resource "aws_subnet" "private-subnet-ca-central-1a" {
  vpc_id            = aws_vpc.digitalhippo-vpc.id
  cidr_block        = "10.0.0.0/19"
  availability_zone = "ca-central-1a"

  tags = {
    "Name"                                               = "digitalhippo-subnet-private-ca-central-1a"
    "kubernetes.io/role/internal-elb"                    = "1"
    "kubernetes.io/cluster/digitalhippo-backend-cluster" = "owned"
  }
}

resource "aws_subnet" "private-subnet-ca-central-1b" {
  vpc_id            = aws_vpc.digitalhippo-vpc.id
  cidr_block        = "10.0.32.0/19"
  availability_zone = "ca-central-1b"

  tags = {
    "Name"                                               = "digitalhippo-subnet-private-ca-central-1b"
    "kubernetes.io/role/internal-elb"                    = "1"
    "kubernetes.io/cluster/digitalhippo-backend-cluster" = "owned"
  }
}

resource "aws_subnet" "public-subnet-ca-central-1a" {
  vpc_id                  = aws_vpc.digitalhippo-vpc.id
  cidr_block              = "10.0.64.0/19"
  availability_zone       = "ca-central-1a"
  map_public_ip_on_launch = true

  tags = {
    "Name"                                               = "digitalhippo-subnet-public-ca-central-1a"
    "kubernetes.io/role/elb"                             = "1"
    "kubernetes.io/cluster/digitalhippo-backend-cluster" = "owned"
  }
}

resource "aws_subnet" "public-subnet-ca-central-1b" {
  vpc_id                  = aws_vpc.digitalhippo-vpc.id
  cidr_block              = "10.0.96.0/19"
  availability_zone       = "ca-central-1b"
  map_public_ip_on_launch = true

  tags = {
    "Name"                                               = "digitalhippo-subnet-public-ca-central-1b"
    "kubernetes.io/role/elb"                             = "1"
    "kubernetes.io/cluster/digitalhippo-backend-cluster" = "owned"
  }
}