variable "region" {
  description = "部署的 AWS 区域"
  type        = string
  default     = "ca-central-1"  # 可根据需要修改默认区域
}

variable "vpc_cidr" {
  description = "VPC 的 CIDR 块"
  type        = string
  default     = "10.0.0.0/16"
}

variable "public_subnet_cidrs" {
  description = "公共子网的 CIDR 列表"
  type        = list(string)
  default     = ["10.0.1.0/24", "10.0.2.0/24", "10.0.3.0/24"]
}

variable "private_subnet_cidrs" {
  description = "私有子网的 CIDR 列表"
  type        = list(string)
  default     = ["10.0.11.0/24", "10.0.12.0/24", "10.0.13.0/24"]
}
