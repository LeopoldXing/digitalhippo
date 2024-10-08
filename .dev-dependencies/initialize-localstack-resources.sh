#!/bin/bash

# Wait for LocalStack to be ready
until curl -s http://localhost:4566 > /dev/null; do
  echo "Waiting for LocalStack to be ready..."
  sleep 5
done

echo "LocalStack is up and running!"

# Configure AWS CLI for LocalStack
AWS_ACCESS_KEY_ID="test"
AWS_SECRET_ACCESS_KEY="test"
AWS_DEFAULT_REGION="ca-central-1"

# Set configuration for AWS CLI
aws configure set aws_access_key_id $AWS_ACCESS_KEY_ID
aws configure set aws_secret_access_key $AWS_SECRET_ACCESS_KEY
aws configure set default.region $AWS_DEFAULT_REGION

# Ensure LocalStack endpoint is used
ENDPOINT_URL="http://localhost:4566"

# Create an SNS topic and capture the ARN
TOPIC_ARN=$(aws --endpoint-url=$ENDPOINT_URL sns create-topic --name digitalhippo-email-topic --query 'TopicArn' --output text)
if [ -z "$TOPIC_ARN" ]; then
    echo "Failed to create SNS topic."
    exit 1
fi

# Create an SQS queue
EMAIL_QUEUE_URL=$(aws --endpoint-url=$ENDPOINT_URL sqs create-queue --queue-name email-queue --query 'QueueUrl' --output text)
if [ -z "$EMAIL_QUEUE_URL" ]; then
    echo "Failed to create SQS queue."
    exit 1
fi

# Create a DLQ for the main queue
DLQ_URL=$(aws --endpoint-url=$ENDPOINT_URL sqs create-queue --queue-name email-dlq --query 'QueueUrl' --output text)
if [ -z "$DLQ_URL" ]; then
    echo "Failed to create DLQ."
    exit 1
fi

# Get the ARN of the DLQ
DLQ_ARN=$(aws --endpoint-url=$ENDPOINT_URL sqs get-queue-attributes --queue-url $DLQ_URL --attribute-names QueueArn --query 'Attributes.QueueArn' --output text)
if [ -z "$DLQ_ARN" ]; then
    echo "Failed to get DLQ ARN."
    exit 1
fi

# Set the redrive policy for the main queue to use the DLQ
aws --endpoint-url=$ENDPOINT_URL sqs set-queue-attributes --queue-url $EMAIL_QUEUE_URL --attributes "{\"RedrivePolicy\":\"{\\\"deadLetterTargetArn\\\":\\\"$DLQ_ARN\\\",\\\"maxReceiveCount\\\":\\\"5\\\"}\"}"

# Subscribe the SQS queue to the SNS topic
aws --endpoint-url=$ENDPOINT_URL sns subscribe --topic-arn $TOPIC_ARN --protocol sqs --notification-endpoint $EMAIL_QUEUE_URL

echo "Setup complete."
