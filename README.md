# Messaging Microservices Demo

Demonstrates how to create Java/Spring based microservices using modern approaches:
* Service Discovery (HashiCorp Consul)
* API Gateway (Spring Cloud Gateway)
* Client-side load-balancing (Spring Cloud LoadBalancer)
* OAuth 2.0 scope based service-to-service authorization (Spring Security/Keycloak)
* Database schema evolution (Liquibase)
* Java classes mapping (MapStruct)
* Infrastructure as Code (HashiCorp Terraform)

## Prerequisites
* JDK 11
* Maven
* Docker
* Docker Compose
* Terraform

## Infrastructure provisioning
* Run under _infrastructure_ directory
```
docker-compose up -d
```
* Run under _infrastructure/terraform/0100-database_ directory
```
terraform init
terraform apply
```
* Run under _infrastructure/terraform/0200-oidc_ directory
```
terraform init
terraform apply
```
* Run under _infrastructure/terraform/0300-consul_ directory
```
terraform init
terraform apply
```

You can access some infrastructure services using the following addresses:

| Service  | Address               | Credentials  |
|----------|-----------------------|--------------|
| Consul   | http://localhost:8500 ||
| Keycloak | http://localhost:8180 | admin/secret |
