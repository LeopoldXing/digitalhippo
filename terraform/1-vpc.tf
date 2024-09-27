resource "aws_vpc" "digitalhippo-vpc" {
  cidr_block = "10.0.0.0/16"

  tags = {
    Name = "digitalhippo-vpc"
  }
}
