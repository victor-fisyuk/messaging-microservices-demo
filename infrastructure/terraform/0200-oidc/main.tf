# OIDC layer
#
# Provisions the following resources:
#   - Keycloak messaging realm
#   - demo users

terraform {
  required_providers {
    keycloak = {
      source  = "mrparkers/keycloak"
      version = "~> 3.7.0"
    }
  }
}

provider "keycloak" {
  url                      = var.keycloak_url
  tls_insecure_skip_verify = var.keycloak_tls_insecure_skip_verify
  client_id                = "admin-cli"
}

# Messaging realm

resource "keycloak_realm" "messaging" {
  realm        = "messaging"
  display_name = "Messaging Microservices Demo"

  login_with_email_allowed = true

  default_signature_algorithm          = "RS256"
  offline_session_max_lifespan_enabled = true
  offline_session_max_lifespan         = var.realm_offline_session_max_lifespan

  # Doesn't work - https://github.com/mrparkers/terraform-provider-keycloak/issues/547
  default_default_client_scopes  = []
  default_optional_client_scopes = []
}

resource "keycloak_default_roles" "default_roles" {
  realm_id      = keycloak_realm.messaging.id
  default_roles = []
}

# Users

resource "keycloak_user" "victor_fisyuk" {
  realm_id = keycloak_realm.messaging.id

  username       = "victor.fisyuk"
  email          = "victor.fisyuk@gmail.com"
  email_verified = true
  first_name     = "Victor"
  last_name      = "Fisyuk"

  initial_password {
    value = var.demo_users_password
  }
}

resource "keycloak_user" "john_doe" {
  realm_id = keycloak_realm.messaging.id

  username       = "john.doe"
  email          = "john.doe@mail.local"
  email_verified = true
  first_name     = "John"
  last_name      = "Doe"

  initial_password {
    value = var.demo_users_password
  }
}
