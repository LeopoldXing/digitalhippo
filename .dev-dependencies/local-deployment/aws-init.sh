# Create SNS topic
aws --endpoint-url=http://localhost:4566 sns create-topic --name digitalhippo-email-topic

# Create SQS dead-letter queue
# aws --endpoint-url=http://localhost:4566 sqs create-queue --queue-name digitalhippo-email-dlq
# Get SQS DLQ ARN
# aws --endpoint-url=http://localhost:4566 sqs get-queue-attributes --queue-url http://sqs.ca-central-1.localhost.localstack.cloud:4566/000000000000/digitalhippo-email-dlq --attribute-names QueueArn --query 'Attributes.QueueArn' --output text

# Create other SQS queues with dead-letter queue setup
aws --endpoint-url=http://localhost:4566 sqs create-queue --queue-name digitalhippo-receipt-email-queue
aws --endpoint-url=http://localhost:4566 sqs create-queue --queue-name digitalhippo-verification-email-queue

# Subscribe sns
aws --endpoint-url=http://localhost:4566 sns subscribe --topic-arn arn:aws:sns:ca-central-1:000000000000:digitalhippo-email-topic --protocol sqs --notification-endpoint arn:aws:sqs:ca-central-1:000000000000:digitalhippo-receipt-email-queue
aws --endpoint-url=http://localhost:4566 sns subscribe --topic-arn arn:aws:sns:ca-central-1:000000000000:digitalhippo-email-topic --protocol sqs --notification-endpoint arn:aws:sqs:ca-central-1:000000000000:digitalhippo-verification-email-queue

# Create S3 bucket and apply public read policy
aws --endpoint-url=http://localhost:4566 s3 mb s3://digitalhippo-leopoldxing
aws --endpoint-url=http://localhost:4566 s3api put-bucket-policy --bucket digitalhippo-leopoldxing --policy file://public-read-policy.json
