resource "aws_ecs_task_definition" "kibana" {
  family             = "kibana-dh"
  requires_compatibilities = ["FARGATE"]
  network_mode       = "awsvpc"
  cpu                = "512"
  memory             = "1024"
  execution_role_arn = aws_iam_role.ecs_task_execution_role.arn

  container_definitions = jsonencode([
    {
      name      = "kibana"
      image     = "kibana:8.14.1"
      essential = true
      portMappings = [
        { containerPort = 5601, protocol = "tcp" }
      ]
      environment = [
        { name = "ELASTICSEARCH_HOST", value = aws_lb.elasticsearch_lb.dns_name }
      ]
      command = [
        "sh", "-c",
        "echo \"elasticsearch.hosts: [ \\\"${ELASTICSEARCH_HOST}:9200\\\" ]\" > /usr/share/kibana/config/kibana.yml && kibana"
      ]
      logConfiguration = {
        logDriver = "awslogs",
        options = {
          "awslogs-group"         = "/ecs/kibana-dh",
          "awslogs-region"        = var.region,
          "awslogs-stream-prefix" = "ecs"
        }
      }
    }
  ])
}
