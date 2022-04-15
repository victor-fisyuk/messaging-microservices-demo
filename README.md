# Messaging Microservices Demo

Demonstrates how to create Java/Spring based microservices using modern approaches:
* Service Discovery (HashiCorp Consul)
* API Gateway (Spring Cloud Gateway)
* Client-side load-balancing (Spring Cloud LoadBalancer)
* OpenID Connect based user authentication (Spring Security/Keycloak)
* OAuth 2.0 scope based service-to-service authorization (Spring Security/Keycloak)
* Database schema evolution (Liquibase)
* Java classes mapping (MapStruct)
* Containerized microservices (Docker)
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
* Run under _infrastructure/terraform/0300-consul_ directory (to run microservices without containers)
```
terraform init
terraform apply
```
* Run under _infrastructure/terraform/0300-consul_ directory (to run microservices inside containers)
```
terraform init
terraform apply -var-file="docker.tfvars"
```

You can access some infrastructure services using the following addresses:

| Service  | Address               | Credentials  |
|----------|-----------------------|--------------|
| Consul   | http://localhost:8500 ||
| Keycloak | http://localhost:8180 | admin/secret |

## How to run

### Without containers
* Build and run microservices in _api-gateway_, _messages-service_ and _profiles-service_ directories
```
mvn -DskipTests spring-boot:run
```

### Inside containers
* Build docker containers for microservices in _api-gateway_, _messages-service_ and _profiles-service_ directories
```
mvn -DskipTests package docker:build 
```
* Start microservices containers
```
docker-compose up -d
```

## How to use

Open in a browser the following URLs to see the inbox and outbox messages:

http://localhost:8080/api/messages/message/inbox
<br>
http://localhost:8080/api/messages/message/outbox

You will be redirected to the login page. Use one of the following users - _victor.fisyuk_, _john.doe_ (use _secret_ as password).
