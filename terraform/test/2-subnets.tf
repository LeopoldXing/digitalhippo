# 获取当前区域内的可用区列表
data "aws_availability_zones" "available" {
  state = "available"
}

# 创建三个公共子网
resource "aws_subnet" "public" {
  count                   = length(var.public_subnet_cidrs)
  vpc_id                  = aws_vpc.digitalhippo_vpc.id
  cidr_block              = var.public_subnet_cidrs[count.index]
  availability_zone       = data.aws_availability_zones.available.names[count.index]
  map_public_ip_on_launch = true

  tags = {
    Name = "digitalhippo-public-subnet-${count.index + 1}"
  }
}

# 创建三个私有子网
resource "aws_subnet" "private" {
  count             = length(var.private_subnet_cidrs)
  vpc_id            = aws_vpc.digitalhippo_vpc.id
  cidr_block        = var.private_subnet_cidrs[count.index]
  availability_zone = data.aws_availability_zones.available.names[count.index]

  tags = {
    Name = "digitalhippo-private-subnet-${count.index + 1}"
  }
}
