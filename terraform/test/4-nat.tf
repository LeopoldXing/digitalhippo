# 为每个公共子网创建 NAT 网关需要的弹性 IP
resource "aws_eip" "nat_eip" {
  count = length(aws_subnet.public)
  domain = "vpc"
  depends_on = [aws_internet_gateway.igw]
}

# 在每个公共子网中创建 NAT 网关
resource "aws_nat_gateway" "nat_gw" {
  count = length(aws_subnet.public)
  allocation_id = aws_eip.nat_eip[count.index].id
  subnet_id     = aws_subnet.public[count.index].id

  tags = {
    Name = "digitalhippo-natgw-${count.index + 1}"
  }
}
