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

# --- Elasticsearch 相关变量 ---
variable "elasticsearch_version" {
  description = "Elasticsearch 版本，默认 8.14.1"
  type        = string
  default     = "8.14.1"
}

variable "elasticsearch_node_count" {
  description = "Elasticsearch 集群的节点数量，至少 1"
  type        = number
  default     = 3
}

variable "elasticsearch_instance_type" {
  description = "Elasticsearch 节点 EC2 实例类型。"
  type        = string
  default     = "t3.large"
}

variable "elasticsearch_volume_size" {
  description = "Elasticsearch 数据盘的 EBS 卷大小（GB）"
  type        = number
  default     = 50
}

variable "elasticsearch_cluster_name" {
  description = "Elasticsearch 集群名称"
  type        = string
  default     = "digitalhippo-es-cluster"
}