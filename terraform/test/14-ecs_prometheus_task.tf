resource "aws_ecs_task_definition" "prometheus" {
  family             = "prometheus-dh"
  requires_compatibilities = ["FARGATE"]
  network_mode       = "awsvpc"
  cpu                = "512"
  memory             = "1024"
  execution_role_arn = aws_iam_role.ecs_task_execution_role.arn

  container_definitions = jsonencode([
    {
      name      = "prometheus"
      image     = "prom/prometheus:v2.54.0"
      essential = true
      portMappings = [
        { containerPort = 9090, protocol = "tcp" }
      ]
      command = [
        "sh", "-c",
        "echo 'global:\\n  scrape_interval: 5s\\n  evaluation_interval: 5s\\n\\nscrape_configs:\\n  - job_name: \"ms-gateway\"\\n    metrics_path: \"/actuator/prometheus\"\\n    static_configs:\\n      - targets: [\"localhost:8080\"]\\n  - job_name: \"ms-cart\"\\n    metrics_path: \"/actuator/prometheus\"\\n    static_configs:\\n      - targets: [\"localhost:9007\"]\\n  - job_name: \"ms-product\"\\n    metrics_path: \"/actuator/prometheus\"\\n    static_configs:\\n      - targets: [\"localhost:9006\"]\\n  - job_name: \"ms-order\"\\n    metrics_path: \"/actuator/prometheus\"\\n    static_configs:\\n      - targets: [\"localhost:9008\"]\\n  - job_name: \"ms-user\"\\n    metrics_path: \"/actuator/prometheus\"\\n    static_configs:\\n      - targets: [\"localhost:9004\"]' > /etc/prometheus/prometheus.yml && prometheus --config.file=/etc/prometheus/prometheus.yml"
      ]
      logConfiguration = {
        logDriver = "awslogs",
        options = {
          "awslogs-group"         = "/ecs/prometheus-dh",
          "awslogs-region"        = var.region,
          "awslogs-stream-prefix" = "ecs"
        }
      }
    }
  ])
}
