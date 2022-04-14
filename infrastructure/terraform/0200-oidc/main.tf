# OIDC layer
#
# Provisions the following resources:
#   - Keycloak messaging realm
#   - OAuth2 client scopes
#   - OAuth2 clients for services
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

# OAuth2 client scopes

resource "keycloak_openid_client_scope" "messages_read" {
  realm_id               = keycloak_realm.messaging.id
  name                   = "messages:read"
  description            = "Read messages"
  consent_screen_text    = "Read your messages"
  include_in_token_scope = true
  gui_order              = 1
}

resource "keycloak_openid_client_scope" "messages_write" {
  realm_id               = keycloak_realm.messaging.id
  name                   = "messages:write"
  description            = "Write messages"
  consent_screen_text    = "Send new messages and remove your existing messages"
  include_in_token_scope = true
  gui_order              = 2
}

resource "keycloak_openid_client_scope" "profiles_read" {
  realm_id               = keycloak_realm.messaging.id
  name                   = "profiles:read"
  description            = "Read profiles"
  consent_screen_text    = "Read profiles"
  include_in_token_scope = true
  gui_order              = 3
}

resource "keycloak_openid_client_scope" "profiles_write" {
  realm_id               = keycloak_realm.messaging.id
  name                   = "profiles:write"
  description            = "Write profiles"
  consent_screen_text    = "Write profiles"
  include_in_token_scope = true
  gui_order              = 4
}

# Messages Service OAuth2 client

resource "keycloak_openid_client" "messages_service" {
  realm_id  = keycloak_realm.messaging.id
  client_id = "messages-service"
  name      = "Messages Service"

  access_type              = "CONFIDENTIAL"
  service_accounts_enabled = true
  full_scope_allowed       = false
}

resource "keycloak_openid_client_default_scopes" "messages_service_default_scopes" {
  realm_id  = keycloak_realm.messaging.id
  client_id = keycloak_openid_client.messages_service.id

  default_scopes = []
}

resource "keycloak_openid_client_optional_scopes" "messages_service_optional_scopes" {
  realm_id  = keycloak_realm.messaging.id
  client_id = keycloak_openid_client.messages_service.id

  optional_scopes = [
    keycloak_openid_client_scope.profiles_read.name,
    keycloak_openid_client_scope.profiles_write.name
  ]
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
