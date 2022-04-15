# Default values for Docker environment
#
# terraform apply -var-file="docker.tfvars"

consul_address = "localhost:8500"
consul_scheme  = "http"

database_host           = "postgres"
keycloak_url            = "http://keycloak:8080"
keycloak_frontend_url   = "http://localhost:8180"
redis_host              = "redis"
kafka_bootstrap_servers = ["kafka1:9092", "kafka2:9092", "kafka3:9092"]
zipkin_base_url         = "http://zipkin:9411/"
