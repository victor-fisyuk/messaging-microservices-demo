variable "consul_address" {
  type        = string
  description = "The HTTP(S) API address of the Consul agent to use"
}

variable "consul_scheme" {
  type        = string
  description = "The URL scheme of the Consul agent to use ('http' or 'https')"
  default     = "https"
}
