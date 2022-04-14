output "realm_id" {
  description = "Keycloak realm ID"
  value       = keycloak_realm.messaging.id
}

output "users" {
  description = "Initial users"
  value = [for user in [keycloak_user.victor_fisyuk, keycloak_user.john_doe] : {
    id         = user.id
    first_name = user.first_name
    last_name  = user.last_name
    email      = user.email
  }]
}
