# Messaging Microservices Demo

Demonstrates how to create Java/Spring based microservices using modern approaches:
* Infrastructure as Code (HashiCorp Terraform)

## Prerequisites
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

You can access some infrastructure services using the following addresses:

| Service  | Address               | Credentials  |
|----------|-----------------------|--------------|
| Keycloak | http://localhost:8180 | admin/secret |
