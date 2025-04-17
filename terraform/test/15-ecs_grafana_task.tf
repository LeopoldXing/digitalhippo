resource "aws_ecs_task_definition" "grafana" {
  family             = "grafana-dh"
  requires_compatibilities = ["FARGATE"]
  network_mode       = "awsvpc"
  cpu                = "512"
  memory             = "1024"
  execution_role_arn = aws_iam_role.ecs_task_execution_role.arn

  container_definitions = jsonencode([
    {
      name      = "grafana"
      image     = "grafana/grafana:11.2.2"
      essential = true
      portMappings = [
        { containerPort = 3000, protocol = "tcp" }
      ]
      environment = [
        { name = "GF_PATHS_PROVISIONING", value = "/etc/grafana/provisioning" },
        { name = "GF_AUTH_ANONYMOUS_ENABLED", value = "true" },
        { name = "GF_AUTH_ANONYMOUS_ORG_ROLE", value = "Admin" }
      ]
      command = [
        "sh", "-c",
        "echo 'apiVersion: 1\\n\\ndeleteDatasources:\\n  - name: Prometheus\\n\\ndatasources:\\n  - name: Prometheus\\n    type: prometheus\\n    uid: prometheus\\n    url: http://prometheus-dh:9090\\n    access: proxy\\n    orgId: 1\\n    basicAuth: false\\n    isDefault: false\\n    version: 1\\n    editable: true\\n    jsonData:\\n      httpMethod: GET' > /etc/grafana/provisioning/datasources/datasource.yml && /run.sh"
      ]
      logConfiguration = {
        logDriver = "awslogs",
        options = {
          "awslogs-group"         = "/ecs/grafana-dh",
          "awslogs-region"        = var.region,
          "awslogs-stream-prefix" = "ecs"
        }
      }
    }
  ])
}
