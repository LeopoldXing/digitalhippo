resource "aws_security_group" "ecs_monitoring_logstash_sg" {
  name        = "ecs-monitoring-logstash-sg"
  description = "Security group for ECS Logstash tasks"
  vpc_id      = aws_vpc.digitalhippo_vpc.id

  ingress {
    from_port = 5000
    to_port   = 5000
    protocol  = "tcp"
    cidr_blocks = [var.vpc_cidr]
  }
  ingress {
    from_port = 5000
    to_port   = 5000
    protocol  = "udp"
    cidr_blocks = [var.vpc_cidr]
  }
  egress {
    from_port = 0
    to_port   = 0
    protocol  = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

resource "aws_security_group" "ecs_monitoring_kibana_sg" {
  name        = "ecs-monitoring-kibana-sg"
  description = "Security group for ECS Kibana tasks"
  vpc_id      = aws_vpc.digitalhippo_vpc.id

  ingress {
    from_port = 5601
    to_port   = 5601
    protocol  = "tcp"
    cidr_blocks = [var.vpc_cidr]
  }
  egress {
    from_port = 0
    to_port   = 0
    protocol  = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

resource "aws_security_group" "ecs_monitoring_prometheus_sg" {
  name        = "ecs-monitoring-prometheus-sg"
  description = "Security group for ECS Prometheus tasks"
  vpc_id      = aws_vpc.digitalhippo_vpc.id

  ingress {
    from_port = 9090
    to_port   = 9090
    protocol  = "tcp"
    cidr_blocks = [var.vpc_cidr]
  }
  egress {
    from_port = 0
    to_port   = 0
    protocol  = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

resource "aws_security_group" "ecs_monitoring_grafana_sg" {
  name        = "ecs-monitoring-grafana-sg"
  description = "Security group for ECS Grafana tasks"
  vpc_id      = aws_vpc.digitalhippo_vpc.id

  ingress {
    from_port = 3000
    to_port   = 3000
    protocol  = "tcp"
    cidr_blocks = [var.vpc_cidr]
  }
  egress {
    from_port = 0
    to_port   = 0
    protocol  = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}
