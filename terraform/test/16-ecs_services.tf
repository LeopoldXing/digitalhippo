resource "aws_ecs_service" "logstash_service" {
  name            = "logstash-service"
  cluster         = aws_ecs_cluster.monitoring.id
  task_definition = aws_ecs_task_definition.logstash.arn
  desired_count   = 1
  launch_type     = "FARGATE"

  network_configuration {
    subnets          = aws_subnet.private[*].id
    security_groups = [aws_security_group.ecs_monitoring_logstash_sg.id]
    assign_public_ip = false
  }
}

resource "aws_ecs_service" "kibana_service" {
  name            = "kibana-service"
  cluster         = aws_ecs_cluster.monitoring.id
  task_definition = aws_ecs_task_definition.kibana.arn
  desired_count   = 1
  launch_type     = "FARGATE"

  network_configuration {
    subnets          = aws_subnet.private[*].id
    security_groups = [aws_security_group.ecs_monitoring_kibana_sg.id]
    assign_public_ip = false
  }
}

resource "aws_ecs_service" "prometheus_service" {
  name            = "prometheus-service"
  cluster         = aws_ecs_cluster.monitoring.id
  task_definition = aws_ecs_task_definition.prometheus.arn
  desired_count   = 1
  launch_type     = "FARGATE"

  network_configuration {
    subnets          = aws_subnet.private[*].id
    security_groups = [aws_security_group.ecs_monitoring_prometheus_sg.id]
    assign_public_ip = false
  }
}

resource "aws_ecs_service" "grafana_service" {
  name            = "grafana-service"
  cluster         = aws_ecs_cluster.monitoring.id
  task_definition = aws_ecs_task_definition.grafana.arn
  desired_count   = 1
  launch_type     = "FARGATE"

  network_configuration {
    subnets          = aws_subnet.private[*].id
    security_groups = [aws_security_group.ecs_monitoring_grafana_sg.id]
    assign_public_ip = false
  }
}
