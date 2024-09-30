# IAM role
resource "aws_iam_role" "digitalhippo-backend-nodes-role" {
  name = "digitalhippo-backend-nodes-role"

  assume_role_policy = jsonencode({
    Statement = [
      {
        Action = "sts:AssumeRole"
        Effect = "Allow"
        Principal = {
          Service = "ec2.amazonaws.com"
        }
      }
    ]
    Version = "2012-10-17"
  })
}

# attach worker node policy to IAM role
resource "aws_iam_role_policy_attachment" "node-AmazonEKSWorkerNodePolicy" {
  policy_arn = "arn:aws:iam::aws:policy/AmazonEKSWorkerNodePolicy"
  role       = aws_iam_role.digitalhippo-backend-nodes-role.name
}

# attach CNI policy to IAM role
resource "aws_iam_role_policy_attachment" "docker-engine-AmazonEKS_CNI_Policy" {
  policy_arn = "arn:aws:iam::aws:policy/AmazonEKS_CNI_Policy"
  role       = aws_iam_role.digitalhippo-backend-nodes-role.name
}

# attach EC2 Container Registry ReadOnly Policy to IAM Role
resource "aws_iam_role_policy_attachment" "docker-engine-AmazonEC2ContainerRegistryReadOnly" {
  policy_arn = "arn:aws:iam::aws:policy/AmazonEC2ContainerRegistryReadOnly"
  role       = aws_iam_role.digitalhippo-backend-nodes-role.name
}

# define nodes
resource "aws_eks_node_group" "private-nodes" {
  cluster_name    = aws_eks_cluster.digitalhippo-backend-cluster.name
  node_group_name = "private-nodes"
  node_role_arn   = aws_iam_role.digitalhippo-backend-nodes-role.arn

  subnet_ids = [
    aws_subnet.private-subnet-ca-central-1b.id,
    aws_subnet.private-subnet-ca-central-1a.id
  ]

  # capacity_type = "ON_DEMAND"
  capacity_type = "SPOT"
  instance_types = ["t3.micro", "t3.small"]
  scaling_config {
    desired_size = 1
    max_size     = 3
    min_size     = 0
  }

  update_config {
    max_unavailable = 1
  }

  labels = {
    role = "private-node"
  }
}