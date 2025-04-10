output "vpc_id" {
  description = "创建的 VPC ID"
  value       = aws_vpc.digitalhippo_vpc.id
}

output "public_subnet_ids" {
  description = "公共子网的 ID 列表"
  value       = aws_subnet.public[*].id
}

output "private_subnet_ids" {
  description = "私有子网的 ID 列表"
  value       = aws_subnet.private[*].id
}
