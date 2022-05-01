# Consul layer
#
# Provisions the following resources:
#   - Consul keys

terraform {
  required_providers {
    consul = {
      source  = "hashicorp/consul"
      version = "~> 2.15.1"
    }
  }

  backend "consul" {
    path = "terraform/messaging/state/0300-consul"
  }
}

provider "consul" {
  address = var.consul_address
  scheme  = var.consul_scheme
}

data "terraform_remote_state" "database" {
  backend = "consul"
  config = {
    path = "terraform/messaging/state/0100-database"
  }
}

data "terraform_remote_state" "oidc" {
  backend = "consul"
  config = {
    path = "terraform/messaging/state/0200-oidc"
  }
}

locals {
  database_username = data.terraform_remote_state.database.outputs.database_username
  database_password = data.terraform_remote_state.database.outputs.database_password
}

# Consul keys

resource "consul_key_prefix" "api_gateway_service_config" {
  path_prefix = "config/api-gateway/"

  subkeys = {
    "spring.security.oauth2.client.registration.iam.client-secret" = data.terraform_remote_state.oidc.outputs.api_gateway_client_secret
  }
}

resource "consul_key_prefix" "messages_service_config" {
  path_prefix = "config/messages-service/"

  subkeys = {
    "spring.datasource.username" = local.database_username
    "spring.datasource.password" = local.database_password

    "spring.security.oauth2.client.registration.messages-service.client-secret" = data.terraform_remote_state.oidc.outputs.messages_service_client_secret
  }
}

resource "consul_key_prefix" "profiles_service_config" {
  path_prefix = "config/profiles-service/"

  subkeys = {
    "spring.datasource.username" = local.database_username
    "spring.datasource.password" = local.database_password

    "initial-profiles" = jsonencode([for user in data.terraform_remote_state.oidc.outputs.users : {
      user_id    = user.id
      first_name = user.first_name
      last_name  = user.last_name
      email      = user.email
    }])
  }
}
