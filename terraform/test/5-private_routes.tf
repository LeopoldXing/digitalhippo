# 为每个私有子网创建单独的路由表，默认路由指向对应的 NAT 网关
resource "aws_route_table" "private_rt" {
  count  = length(aws_subnet.private)
  vpc_id = aws_vpc.digitalhippo_vpc.id

  route {
    cidr_block     = "0.0.0.0/0"
    nat_gateway_id = aws_nat_gateway.nat_gw[count.index].id
  }

  tags = {
    Name = "digitalhippo-private-rt-${count.index + 1}"
  }
}

# 将每个私有子网与相应的路由表关联
resource "aws_route_table_association" "private_rt_association" {
  count          = length(aws_subnet.private)
  subnet_id      = aws_subnet.private[count.index].id
  route_table_id = aws_route_table.private_rt[count.index].id
}
