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

output "eks_cluster_endpoint" {
  description = "EKS 集群的 API Server Endpoint"
  value       = aws_eks_cluster.eks_cluster.endpoint
}

output "eks_cluster_ca_data" {
  description = "EKS 集群的 Certificate Authority Data，供 kubectl 等工具使用"
  value       = aws_eks_cluster.eks_cluster.certificate_authority[0].data
}

# 输出 NLB 的 DNS 名称，作为内部 Elasticsearch Endpoint
output "elasticsearch_endpoint" {
  description = "内部 Elasticsearch 访问入口（供 EKS 等使用）"
  value       = aws_lb.elasticsearch_lb.dns_name
}
