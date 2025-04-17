#################################################
# eks-cluster.tf - 创建基于 Fargate 的 EKS 集群
#################################################

# ====================================================
# 1. IAM 角色与策略配置
# ====================================================

# EKS Cluster 所需的 IAM 角色
resource "aws_iam_role" "eks_cluster_role" {
  name = "digitalhippo-eks-cluster-role"
  assume_role_policy = jsonencode({
    Version = "2012-10-17",
    Statement = [
      {
        Effect = "Allow",
        Principal = {
          Service = "eks.amazonaws.com"
        },
        Action = "sts:AssumeRole"
      }
    ]
  })
}

resource "aws_iam_role_policy_attachment" "eks_cluster_policy" {
  role       = aws_iam_role.eks_cluster_role.name
  policy_arn = "arn:aws:iam::aws:policy/AmazonEKSClusterPolicy"
}

# 附加资源控制器策略（推荐用于启用 VPC CNI 部分功能）
resource "aws_iam_role_policy_attachment" "eks_vpc_resource_controller" {
  role       = aws_iam_role.eks_cluster_role.name
  policy_arn = "arn:aws:iam::aws:policy/AmazonEKSVPCResourceController"
}

# Fargate Profile 所需的 IAM 角色
resource "aws_iam_role" "eks_fargate_profile_role" {
  name = "digitalhippo-eks-fargate-profile-role"
  assume_role_policy = jsonencode({
    Version = "2012-10-17",
    Statement = [
      {
        Effect = "Allow",
        Principal = {
          Service = "eks-fargate-pods.amazonaws.com"
        },
        Action = "sts:AssumeRole"
      }
    ]
  })
}

resource "aws_iam_role_policy_attachment" "eks_fargate_policy_attachment" {
  role       = aws_iam_role.eks_fargate_profile_role.name
  policy_arn = "arn:aws:iam::aws:policy/AmazonEKSFargatePodExecutionRolePolicy"
}

# ====================================================
# 2. 创建 EKS 集群
# ====================================================

resource "aws_eks_cluster" "eks_cluster" {
  name     = "digitalhippo-eks"
  role_arn = aws_iam_role.eks_cluster_role.arn

  vpc_config {
    # 此处直接引用之前创建好的私有子网
    subnet_ids = aws_subnet.private[*].id
    # 根据实际场景，可以开启公有访问，也可以关闭；下面示例同时启用公有和私有端点
    endpoint_public_access  = true
    endpoint_private_access = true
  }

  # 指定 Kubernetes 版本，根据实际情况选取支持 Fargate 的版本
  version = "1.32"

  tags = {
    Name = "digitalhippo-eks"
  }
}

# ====================================================
# 3. 创建 Fargate Profile（Serverless 模式）
# ====================================================

resource "aws_eks_fargate_profile" "fargate_profile" {
  cluster_name         = aws_eks_cluster.eks_cluster.name
  fargate_profile_name = "digitalhippo-fargate-profile"
  pod_execution_role_arn = aws_iam_role.eks_fargate_profile_role.arn
  # 使用之前定义好的私有子网作为 EKS Fargate 的运行子网
  subnet_ids           = aws_subnet.private[*].id

  # 此处示例将默认命名空间的所有 Pod 调度到 Fargate 上，
  # 若需要更细粒度控制，请调整 selector
  selector {
    namespace = "default"
  }

  # 如果需要让其他命名空间也运行在 Fargate 上，可添加额外 selector 配置
}
