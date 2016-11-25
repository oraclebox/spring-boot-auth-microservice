# spring-auth-microservice
Try implement authetication and api gateway microservices.

## Overview
### auth-service
Create a new user account by using username/passwordssss
Validate JWT token and retreive account information.
ccccc
### api-gateway
Route all incoming messages to corresponing service.
Validate JWT token if request url containing /private.
Reject all private APIs access if failed JWT validation. 

![overview](https://raw.githubusercontent.com/oraclebox/spring-auth-microservice/master/docs/overview.png)


