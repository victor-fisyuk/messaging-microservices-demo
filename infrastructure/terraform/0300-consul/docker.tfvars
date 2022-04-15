# Default values for Docker environment
#
# terraform apply -var-file="docker.tfvars"

consul_address = "localhost:8500"
consul_scheme  = "http"

database_host         = "postgres"
keycloak_url          = "http://keycloak:8080"
keycloak_frontend_url = "http://localhost:8180"
