variable "consul_address" {
  type        = string
  description = "The HTTP(S) API address of the Consul agent to use"
}

variable "consul_scheme" {
  type        = string
  description = "The URL scheme of the Consul agent to use ('http' or 'https')"
  default     = "https"
}

variable "database_host" {
  type        = string
  description = "PostgreSQL server host used by services"
}

variable "database_port" {
  type        = number
  description = "PostgreSQL server port used by services"
  default     = 5432
}

variable "keycloak_url" {
  type        = string
  description = "Keycloak instance URL, before /auth/admin, used by services"
}

variable "keycloak_frontend_url" {
  type        = string
  description = "Keycloak instance URL, before /auth/admin, used for user authentication"
}

variable "redis_host" {
  type        = string
  description = "Redis server host"
}
