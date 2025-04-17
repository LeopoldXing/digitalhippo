resource "aws_ecs_task_definition" "logstash" {
  family             = "logstash-dh"
  requires_compatibilities = ["FARGATE"]
  network_mode       = "awsvpc"
  cpu                = "512"
  memory             = "1024"
  execution_role_arn = aws_iam_role.ecs_task_execution_role.arn

  container_definitions = jsonencode([
    {
      name      = "logstash"
      image     = "logstash:8.14.1"
      essential = true
      portMappings = [
        { containerPort = 5000, protocol = "tcp" },
        { containerPort = 5000, protocol = "udp" }
      ]
      environment = [
        { name = "LS_JAVA_OPTS", value = "-Xmx256m -Xms256m" },
        { name = "ELASTICSEARCH_HOST", value = aws_lb.elasticsearch_lb.dns_name }
      ]
      command = [
        "sh", "-c",
        "echo \"http.host: \\\"0.0.0.0\\\"\\npath.config: /usr/share/logstash/pipeline\\nxpack.monitoring.enabled: true\\nxpack.monitoring.elasticsearch.hosts: [ \\\"${ELASTICSEARCH_HOST}:9200\\\" ]\" > /usr/share/logstash/config/logstash.yml && " +
        "mkdir -p /usr/share/logstash/pipeline && " +
        "echo \"input { tcp { port => 5000 codec => json } }\\n\\noutput { elasticsearch { hosts => [ \\\"${ELASTICSEARCH_HOST}:9200\\\" ] index => \\\"digitalhippo-logs-%{microservice}\\\" } }\" > /usr/share/logstash/pipeline/logstash.conf && " +
        "logstash"
      ]
      logConfiguration = {
        logDriver = "awslogs",
        options = {
          "awslogs-group"         = "/ecs/logstash-dh",
          "awslogs-region"        = var.region,
          "awslogs-stream-prefix" = "ecs"
        }
      }
    }
  ])
}
