variable "region" {
  description = "部署的 AWS 区域"
  type        = string
  default     = "ca-central-1"
}

variable "vpc_cidr" {
  description = "VPC 的 CIDR 块"
  type        = string
  default     = "10.0.0.0/16"
}

variable "public_subnet_cidrs" {
  description = "公共子网的 CIDR 列表"
  type = list(string)
  default = ["10.0.1.0/24", "10.0.2.0/24", "10.0.3.0/24"]
}

variable "private_subnet_cidrs" {
  description = "私有子网的 CIDR 列表"
  type = list(string)
  default = ["10.0.11.0/24", "10.0.12.0/24", "10.0.13.0/24"]
}

# --- 数据库和 RDS Proxy 相关变量 ---
variable "db_cluster_identifier" {
  description = "Aurora 集群的标识符（Cluster Name）"
  type        = string
}

variable "db_master_username" {
  description = "Aurora 集群的主用户名"
  type        = string
}

variable "db_master_password" {
  description = "Aurora 集群的主用户密码"
  type        = string
  sensitive   = true
}

variable "serverless_min_capacity" {
  description = "Aurora Serverless v2 最小容量（ACU）"
  type        = number
  default     = 0.5
}

variable "serverless_max_capacity" {
  description = "Aurora Serverless v2 最大容量（ACU）"
  type        = number
  default     = 4
}

variable "db_instance_class" {
  description = "Aurora 集群实例的类型（Aurora Serverless v2 支持 db.serverless）"
  type        = string
  default     = "db.serverless"
}
