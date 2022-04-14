output "database_name" {
  description = "Database name"
  value       = postgresql_database.messaging.name
}

output "database_username" {
  description = "Username used by services to connect to PostgreSQL"
  value       = postgresql_role.service_app.name
}

output "database_password" {
  description = "Password used by services to connect to PostgreSQL"
  value       = postgresql_role.service_app.password
  sensitive   = true
}
