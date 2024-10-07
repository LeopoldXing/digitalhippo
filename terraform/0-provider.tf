provider "aws" {
  region = "ca-central-1"
}

terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 3.27"
    }
  }
}

variable "region" {
  description = "AWS region"
  type        = string
  default     = "ca-central-1"
}