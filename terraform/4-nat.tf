resource "aws_eip" "eip" {
  tags = {
    Name = "digitalhippo-nat"
  }
}

resource "aws_nat_gateway" "nat-gateway" {
  allocation_id = aws_eip.eip.id
  subnet_id     = aws_subnet.public-subnet-ca-central-1a.id

  tags = {
    Name = "digitalhippo-nat"
  }

  depends_on = [aws_internet_gateway.igw]
}