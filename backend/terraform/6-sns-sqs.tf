# sns topic - email
resource "aws_sns_topic" "email_topic" {
  name = "digitalhippo-email-topic"
}

# sqs dlq - email
resource "aws_sqs_queue" "email_dlq" {
  name = "digitalhippo-dlq"
}

# sqs email
resource "aws_sqs_queue" "email_queue" {
  name = "digitalhippo-verification-email-queue"
  redrive_policy = jsonencode({
    deadLetterTargetArn = aws_sqs_queue.email_dlq.arn
    maxReceiveCount     = 10
  })
}

# sqs subscribe sns topic
resource "aws_sns_topic_subscription" "email_subscription" {
  topic_arn = aws_sns_topic.email_topic.arn
  protocol  = "sqs"
  endpoint  = aws_sqs_queue.email_queue.arn
}

# modify the sqs policy
resource "aws_sqs_queue_policy" "email_queue_policy" {
  queue_url = aws_sqs_queue.email_queue.id

  policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Effect    = "Allow"
        Principal = "*"
        Action    = "sqs:SendMessage"
        Resource  = aws_sqs_queue.email_queue.arn
        Condition = {
          ArnEquals = {
            "aws:SourceArn" = aws_sns_topic.email_topic.arn
          }
        }
      },
    ]
  })
}

# vpc endpoint for sns
resource "aws_vpc_endpoint" "sns_vpc_endpoint" {
  vpc_id            = aws_vpc.digitalhippo-vpc.id
  service_name      = "com.amazonaws.${var.region}.sns"
  vpc_endpoint_type = "Interface"
  subnet_ids        = [aws_subnet.private-subnet-ca-central-1a.id, aws_subnet.private-subnet-ca-central-1b.id]
  security_group_ids = [aws_security_group.eks_sg.id]
  private_dns_enabled = true
}

# vpc endpoint for sqs
resource "aws_vpc_endpoint" "sqs_vpc_endpoint" {
  vpc_id            = aws_vpc.digitalhippo-vpc.id
  service_name      = "com.amazonaws.${var.region}.sqs"
  vpc_endpoint_type = "Interface"
  subnet_ids        = [aws_subnet.private-subnet-ca-central-1a.id, aws_subnet.private-subnet-ca-central-1b.id]
  security_group_ids = [aws_security_group.eks_sg.id]
  private_dns_enabled = true
}