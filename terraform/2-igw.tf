resource "aws_internet_gateway" "igw" {
  vpc_id = aws_vpc.digitalhippo-vpc.id

  tags = {
    Name = "igw"
  }
}