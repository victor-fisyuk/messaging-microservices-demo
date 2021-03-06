# Messaging Microservices Demo

Demonstrates how to create Java/Spring based microservices using modern approaches:
* Service Discovery (HashiCorp Consul)
* API Gateway (Spring Cloud Gateway)
* Client-side load-balancing (Spring Cloud LoadBalancer)
* OpenID Connect based user authentication (Spring Security/Keycloak)
* Distributed sessions (Spring Session/Redis)
* OAuth 2.0 scope based service-to-service authorization (Spring Security/Keycloak)
* Data caching (Spring Cache/Redis)
* Cloud Bus to broadcast cache invalidation notifications between microservices (Spring Cloud Bus/Apache Kafka)
* Database schema evolution (Liquibase)
* Java classes mapping (MapStruct)
* Distributed tracing (Spring Sleuth/Zipkin)
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
* Run under _infrastructure/terraform_ directory
```
source ./env.sh
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
| Kafdrop  | http://localhost:9000 ||
| Zipkin   | http://localhost:9411 ||

## How to run

### Without containers
* Build and run microservices in _api-gateway_, _messages-service_ and _profiles-service_ directories
```
mvn -DskipTests spring-boot:run
```

### Inside containers
* Build docker containers for microservices in _api-gateway_, _messages-service_ and _profiles-service_ directories
```
mvn -DskipTests clean package docker:build
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

By default, there are no messages. You could send new messages using Postman collection _postman_collection.json_.
You could also update user profiles using Postman.
