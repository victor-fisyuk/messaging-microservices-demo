variable "database_host" {
  type        = string
  description = "PostgreSQL server host"
}

variable "database_port" {
  type        = number
  description = "PostgreSQL server port"
  default     = 5432
}

variable "terraform_database_username" {
  type        = string
  description = "Username used by Terraform to connect to PostgreSQL"
  default     = "postgres"
}

variable "terraform_database_password" {
  type        = string
  description = "Password used by Terraform to connect to PostgreSQL"
}

variable "database_sslmode" {
  type        = string
  description = "PostgreSQL SSL connection verification mode"
  default     = "require"
}

variable "service_app_database_username" {
  type        = string
  description = "Username used by services to connect to PostgreSQL"
  default     = "service-app"
}
