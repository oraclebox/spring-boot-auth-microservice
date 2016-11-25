# spring-auth-microservice
Try implement authetication and api gateway microservices.

## Overview
### auth-service
Create a new user account by using username/password.

Validate JWT token and retreive account information.

### api-gateway
Route all incoming messages to corresponing service.

Validate JWT token if request url containing **/private**.

Reject all private APIs access if failed JWT validation. 

![overview](https://raw.githubusercontent.com/oraclebox/spring-auth-microservice/master/docs/overview.png)

## Require Install 
### Mongodb 
```
 docker run --name mongo -p 27017:27017 -d mongo
```
### Redis
```
 docker run --name redis -p 6379:6379 -d redis
```
### Start spring-boot service
auth-service

api-gateway

## Create account
POST http://localhost:8888/auth/v1/continuous with JSON
```
{
	"username": "Oraclebox",
	"email":"abc@gmail.com",
	"password":"pw31931836193@SEED"
}
```
Response will give you JWT token
```
{
  "code": 200,
  "message": "Success",
  "token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI5ZmM1NjM2ZC1mYTBmLTRjZmItYTc1Ny1jYTU2OWQwNTAyNWIiLCJpYXQiOjE0ODAwNjI1MzgsInN1YiI6IjU4MzdmNjEzMjVmNzEwNDA3MDg0YTEzZSIsImlzcyI6Im9yYWNsZWJveCIsImV4cCI6MTQ4NzgzODUzOH0.Ryys6NQVlWj44ESpcaTqA-l0c4XCdkyhEzQGRdr5Gqk",
  "data": {
    "id": "5837f61325f710407084a13e",
    "username": "Oraclebox",
    "email": "abc@gmail.com"
  }
}



``
