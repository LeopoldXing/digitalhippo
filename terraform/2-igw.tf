resource "aws_internet_gateway" "digitalhippo-igw" {
  vpc_id = aws_vpc.digitalhippo-vpc.id

  tags = {
    Name = "digitalhippo-igw"
  }
}