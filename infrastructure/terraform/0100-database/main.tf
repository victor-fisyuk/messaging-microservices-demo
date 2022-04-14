# Database layer
#
# Provisions the following resources:
#   - messaging database
#   - database role for services
#   - database schemas for services

terraform {
  required_providers {
    random = {
      source = "hashicorp/random"
      version = "~> 3.1.2"
    }

    postgresql = {
      source  = "cyrilgdn/postgresql"
      version = "~> 1.15.0"
    }
  }
}

provider "postgresql" {
  host     = var.database_host
  port     = var.database_port
  username = var.terraform_database_username
  password = var.terraform_database_password
  sslmode  = var.database_sslmode
}

# Database and schemas

resource "postgresql_database" "messaging" {
  name = "messaging"
}

resource "postgresql_schema" "schemas" {
  for_each = toset(["messages", "profiles"])
  database = postgresql_database.messaging.name
  name     = each.key
}

# Role for services

resource "random_password" "service_app_role_password" {
  length  = 16
  special = true
}

resource "postgresql_role" "service_app" {
  name     = var.service_app_database_username
  login    = true
  password = random_password.service_app_role_password.result
}

resource "postgresql_grant" "database" {
  database    = postgresql_database.messaging.name
  role        = postgresql_role.service_app.name
  object_type = "database"
  privileges  = ["CONNECT"]
}

resource "postgresql_grant" "schemas" {
  for_each    = postgresql_schema.schemas
  database    = postgresql_database.messaging.name
  role        = postgresql_role.service_app.name
  object_type = "schema"
  schema      = each.value.name
  privileges  = ["CREATE", "USAGE"]
}
