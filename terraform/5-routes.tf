resource "aws_route_table" "private-routes" {
  vpc_id = aws_vpc.digitalhippo-vpc.id

  route = [
    {
      cidr_block                 = "0.0.0.0/0"
      nat_gateway_id             = aws_nat_gateway.nat-gateway.id
      carrier_gateway_id         = ""
      destination_prefix_list_id = ""
      egress_only_gateway_id     = ""
      gateway_id                 = ""
      instance_id                = ""
      ipv6_cidr_block            = ""
      local_gateway_id           = ""
      network_interface_id       = ""
      network_gateway_id         = ""
      vpc_endpoint_id            = ""
      vpc_peering_connection_id  = ""
    }
  ]

  tags = {
    Name = "digitalhippo-private-routes"
  }
}

resource "aws_route_table" "public-routes" {
  vpc_id = aws_vpc.digitalhippo-vpc.id

  route = [
    {
      cidr_block                 = "0.0.0.0/0"
      nat_gateway_id             = aws_nat_gateway.nat-gateway.id
      carrier_gateway_id         = ""
      destination_prefix_list_id = ""
      egress_only_gateway_id     = ""
      gateway_id                 = ""
      instance_id                = ""
      ipv6_cidr_block            = ""
      local_gateway_id           = ""
      network_interface_id       = ""
      network_gateway_id         = ""
      vpc_endpoint_id            = ""
      vpc_peering_connection_id  = ""
    }
  ]

  tags = {
    Name = "digitalhippo-public-routes"
  }
}

resource "aws_route_table_association" "private-association-ca-central-1a" {
  subnet_id = aws_subnet.private-subnet-ca-central-1a.id
  route_table_id = aws_route_table.private-routes.id
}

resource "aws_route_table_association" "private-association-ca-central-1b" {
  subnet_id = aws_subnet.private-subnet-ca-central-1b.id
  route_table_id = aws_route_table.private-routes.id
}

resource "aws_route_table_association" "public-association-ca-central-1a" {
  subnet_id = aws_subnet.public-subnet-ca-central-1a.id
  route_table_id = aws_route_table.private-routes.id
}

resource "aws_route_table_association" "public-association-ca-central-1b" {
  subnet_id = aws_subnet.public-subnet-ca-central-1b.id
  route_table_id = aws_route_table.private-routes.id
}