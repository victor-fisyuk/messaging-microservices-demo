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
}

provider "consul" {
  address = var.consul_address
  scheme  = var.consul_scheme
}

data "terraform_remote_state" "database" {
  backend = "local"
  config = {
    path = "../0100-database/terraform.tfstate"
  }
}

data "terraform_remote_state" "oidc" {
  backend = "local"
  config = {
    path = "../0200-oidc/terraform.tfstate"
  }
}

locals {
  database_jdbc_url = "jdbc:postgresql://${var.database_host}:${var.database_port}/${data.terraform_remote_state.database.outputs.database_name}"
  database_username = data.terraform_remote_state.database.outputs.database_username
  database_password = data.terraform_remote_state.database.outputs.database_password

  keycloak_realm_url = "${var.keycloak_url}/auth/realms/${data.terraform_remote_state.oidc.outputs.realm_id}"
}

# Consul keys

resource "consul_key_prefix" "messages_service_config" {
  path_prefix = "config/messages-service/"

  subkeys = {
    "spring.datasource.url"      = local.database_jdbc_url
    "spring.datasource.username" = local.database_username
    "spring.datasource.password" = local.database_password

    "spring.security.oauth2.resourceserver.jwt.issuer-uri" = local.keycloak_realm_url

    "spring.security.oauth2.client.provider.keycloak.issuer-uri" = local.keycloak_realm_url

    "spring.security.oauth2.client.registration.messages-service.client-secret" = data.terraform_remote_state.oidc.outputs.messages_service_client_secret
  }
}

resource "consul_key_prefix" "profiles_service_config" {
  path_prefix = "config/profiles-service/"

  subkeys = {
    "spring.datasource.url"      = local.database_jdbc_url
    "spring.datasource.username" = local.database_username
    "spring.datasource.password" = local.database_password

    "spring.security.oauth2.resourceserver.jwt.issuer-uri" = local.keycloak_realm_url

    "initial-profiles" = jsonencode([for user in data.terraform_remote_state.oidc.outputs.users : {
      user_id    = user.id
      first_name = user.first_name
      last_name  = user.last_name
      email      = user.email
    }])
  }
}
