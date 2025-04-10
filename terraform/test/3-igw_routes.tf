# 创建 Internet Gateway
resource "aws_internet_gateway" "igw" {
  vpc_id = aws_vpc.digitalhippo_vpc.id

  tags = {
    Name = "digitalhippo-igw"
  }
}

# 创建公共路由表
resource "aws_route_table" "public_rt" {
  vpc_id = aws_vpc.digitalhippo_vpc.id

  route {
    cidr_block = "0.0.0.0/0"
    gateway_id = aws_internet_gateway.igw.id
  }

  tags = {
    Name = "digitalhippo-public-rt"
  }
}

# 将公共子网与公共路由表关联
resource "aws_route_table_association" "public_rt_association" {
  count          = length(aws_subnet.public)
  subnet_id      = aws_subnet.public[count.index].id
  route_table_id = aws_route_table.public_rt.id
}
