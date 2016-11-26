# spring-auth-microservice
[中文版本](README_CN.md)
Try implement authetication and api gateway microservices.

## Overview
### auth-service
```
Create a new user account by using username/password.
Validate JWT token and retreive account information.
```
### api-gateway
```
Route all incoming messages to corresponing service.
Validate JWT token if request url containing **/private**.
Reject all private APIs access if failed JWT validation. 
```
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
#### auth-service
```
cd auth-service
chmod +x gradlew
./gradlew clean build
docker build -t localhost/auth-service:latest .
docker run -it --name auth-service -p 9000:9000 -d localhost/auth-service
```

#### api-gateway
```
cd api-gateway
chmod +x gradlew
./gradlew clean build
docker build -t localhost/api-gateway:latest .
docker run -it --name api-gateway -p 0.0.0.0:8888:8888 -d localhost/api-gateway
```

## Create account by email
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
  "token": "{JWT token.....}",
  "data": {
    "id": "5837f61325f710407084a13e",
    "username": "Oraclebox",
    "email": "abc@gmail.com"
  }
}
```

## Create account via facebook
POST http://localhost:8888/auth/v1/continuous/facebook with Facebook token get from mobile/website login.
```
{
  "accessToken": "EAAIPVNssQLUBAPVvpaO1VuxmhCFErzEgKNhHp3Wb82qgWDa7CZBncdESEQWseqeqw3131ZAsjGnuuVmPaiZATZBtlZAZABpFxQEVQ0uvQclVGxCEPZAR2gU1sTsk7tLbdKK2P8TxHP551W92TYltcrnObZATxSW3123sdwe1MT8cIx7Os78TO52jELx0dnZBtmUuEZAUmfFAFYnwewQZewD"
}
```
Response will give you JWT token and account
```
{
  "code": 200,
  "message": "Success",
  "token": {token},
  "data": {
    "id": "58392ca031313712c88a0b84",
    "email": "oraclebox@gmail.com",
    "active": true,
    "socialId": "10153213359378475",
    "verified": true
  }
}
```

### Try to access private service
```
GET http://localhost:8888/auth/private/v1/greeting with HEARDER
Authorization: Bearer {JWT token.....}
Return 200 if success, 403 if token invalid.
```
## Add New API to getway which require token validation. 
For example add http://localhost:9300/book/private/remove/{id} and http://localhost:9300/book/list

api-gateway > application.yml
```
zuul:
  sensitiveHeaders: Authetication
  routes:
    auth: #authetication service
      path: /auth/**
      url: http://localhost:9000/auth
    book: #authetication service
      path: /book/**
      url: http://localhost:9300/book      
```
The can access them via API Gateway

http://localhost:8888/book/private/remove/{id} (Require JWT header because url contain /private)

http://localhost:8888/book/list


