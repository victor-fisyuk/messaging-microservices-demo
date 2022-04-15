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
  database_jdbc_url = "jdbc:postgresql://${var.database_host}:${var.database_port}/${data.terraform_remote_state.database.outputs.database_name}"
  database_username = data.terraform_remote_state.database.outputs.database_username
  database_password = data.terraform_remote_state.database.outputs.database_password

  keycloak_oidc_base_url          = "${var.keycloak_url}/auth/realms/${data.terraform_remote_state.oidc.outputs.realm_id}/protocol/openid-connect"
  keycloak_oidc_frontend_base_url = "${var.keycloak_frontend_url}/auth/realms/${data.terraform_remote_state.oidc.outputs.realm_id}/protocol/openid-connect"

  keycloak_jwk_set_url        = "${local.keycloak_oidc_base_url}/certs"
  keycloak_auth_url           = "${local.keycloak_oidc_frontend_base_url}/auth"
  keycloak_token_endpoint_url = "${local.keycloak_oidc_base_url}/token"
  keycloak_user_info_url      = "${local.keycloak_oidc_base_url}/userinfo"
  keycloak_end_session_url    = "${local.keycloak_oidc_frontend_base_url}/logout"
}

# Consul keys

resource "consul_key_prefix" "api_gateway_service_config" {
  path_prefix = "config/api-gateway/"

  subkeys = {
    "spring.security.oauth2.client.provider.keycloak.jwk-set-uri"       = local.keycloak_jwk_set_url
    "spring.security.oauth2.client.provider.keycloak.authorization-uri" = local.keycloak_auth_url
    "spring.security.oauth2.client.provider.keycloak.token-uri"         = local.keycloak_token_endpoint_url
    "spring.security.oauth2.client.provider.keycloak.user-info-uri"     = local.keycloak_user_info_url
    "spring.security.oauth2.client.provider.keycloak.end-session-uri"   = local.keycloak_end_session_url

    "spring.security.oauth2.client.registration.iam.client-secret" = data.terraform_remote_state.oidc.outputs.api_gateway_client_secret
  }
}

resource "consul_key_prefix" "messages_service_config" {
  path_prefix = "config/messages-service/"

  subkeys = {
    "spring.datasource.url"      = local.database_jdbc_url
    "spring.datasource.username" = local.database_username
    "spring.datasource.password" = local.database_password

    "spring.security.oauth2.resourceserver.jwt.jwk-set-uri" = local.keycloak_jwk_set_url

    "spring.security.oauth2.client.provider.keycloak.jwk-set-uri" = local.keycloak_jwk_set_url
    "spring.security.oauth2.client.provider.keycloak.token-uri"   = local.keycloak_token_endpoint_url

    "spring.security.oauth2.client.registration.messages-service.client-secret" = data.terraform_remote_state.oidc.outputs.messages_service_client_secret
  }
}

resource "consul_key_prefix" "profiles_service_config" {
  path_prefix = "config/profiles-service/"

  subkeys = {
    "spring.datasource.url"      = local.database_jdbc_url
    "spring.datasource.username" = local.database_username
    "spring.datasource.password" = local.database_password

    "spring.security.oauth2.resourceserver.jwt.jwk-set-uri" = local.keycloak_jwk_set_url

    "initial-profiles" = jsonencode([for user in data.terraform_remote_state.oidc.outputs.users : {
      user_id    = user.id
      first_name = user.first_name
      last_name  = user.last_name
      email      = user.email
    }])
  }
}
