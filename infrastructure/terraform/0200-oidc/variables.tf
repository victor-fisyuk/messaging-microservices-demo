variable "keycloak_url" {
  type        = string
  description = "Keycloak instance URL, before /auth/admin"
}

variable "keycloak_tls_insecure_skip_verify" {
  type        = bool
  description = "Allows ignoring insecure certificates"
  default     = false
}

variable "realm_offline_session_max_lifespan" {
  type        = string
  description = "The maximum amount of time before an offline session expires regardless of activity."
  default     = "30m"
}

variable "api_gateway_url" {
  type        = string
  description = "API Gateway base URL"
}

variable "demo_users_password" {
  type        = string
  description = "Password for demo users"
  default     = "secret"
}
