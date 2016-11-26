# spring-auth-microservice
嘗試實現用於RESTFUL風格的簡單身份驗證和api網關的微服務。

## Overview
### auth-service 服務負責
```
使用EMAIL/密碼來創建新的帳戶。
每次帳戶登入檢查密碼並產生新的JWT。
容許其他服務驗證JWT真偽並返回帳戶信息。
管理JWT的期限
```
### api-gateway
```
將所有傳入的請求路由到相應的服務。
如果請求URL路徑包含** /private **，則在路由之前向auth-service請求驗證JWT。
如果JWT驗證失敗，則拒絕API訪問。
```
![overview](https://raw.githubusercontent.com/oraclebox/spring-auth-microservice/master/docs/overview.png)

## 需要安裝的組件(以下經Docker安裝)
### Mongodb 
```
 docker run --name mongo -p 27017:27017 -d mongo
```
### Redis
```
 docker run --name redis -p 6379:6379 -d redis
```
### auth-service
```
cd auth-service
chmod +x gradlew
./gradlew clean build
docker build -t localhost/auth-service:latest .
docker run -it --name auth-service -p 9000:9000 -d localhost/auth-service
```
### api-gateway
```
cd api-gateway
chmod +x gradlew
./gradlew clean build
docker build -t localhost/api-gateway:latest .
docker run -it --name api-gateway -p 0.0.0.0:8888:8888 -d localhost/api-gateway
```

## API
### 用電子郵件創建帳戶
POST http://localhost:8888/auth/v1/continuous with JSON
```
{
	"username": "Oraclebox",
	"email":"abc@gmail.com",
	"password":"pw31931836193@SEED"
}
```
返回會給你JWT作為其他私有服務登入的TOKEN及帳戶信息
```
{
  "code": 200,
  "message": "Success",
  "token": "{JWT}",
  "data": {
    "id": "5837f61325f710407084a13e",
    "username": "Oraclebox",
    "email": "abc@gmail.com"
  }
}
```

### 用電子郵件登入
POST http://localhost:8888/auth/v1/continuous with JSON
```
{
	"email":"abc@gmail.com",
	"password":"pw31931836193@SEED"
}
```
返回會給你已更新的JWT作為其他私有服務登入的TOKEN及帳戶信息
```
{
  "code": 200,
  "message": "Success",
  "token": "{JWT}",
  "data": {
    "id": "5837f61325f710407084a13e",
    "username": "Oraclebox",
    "email": "abc@gmail.com"
  }
}
```
## 通過Facebook創建帳戶, 手機/網站用Facebook的SDK作客戶端的登入，並把FACEBOOK TOKEN 發過來。
POST http://localhost:8888/auth/v1/continuous/facebook with Facebook token get from mobile/website login.
```
{
  "accessToken": "EAAIPVNssQLUBAPVvpaO1VuxmhCFErzEgKNhHp3Wb82qgWDa7CZBncdESEQWseqeqw3131ZAsjGnuuVmPaiZATZBtlZAZABpFxQEVQ0uvQclVGxCEPZAR2gU1sTsk7tLbdKK2P8TxHP551W92TYltcrnObZATxSW3123sdwe1MT8cIx7Os78TO52jELx0dnZBtmUuEZAUmfFAFYnwewQZewD"
}
```
auth-service會向FACEBOOK請求用戶資訊以确保accessToken有效，如果是第一次登入建立帳戶，返回會給你已更新的JWT作為其他私有服務登入的TOKEN及帳戶信息
```
{
  "code": 200,
  "message": "Success",
  "token": {JWT},
  "data": {
    "id": "58392ca031313712c88a0b84",
    "email": "oraclebox@gmail.com",
    "active": true,
    "socialId": "10153213359378475",
    "verified": true
  }
}
```

### 嘗試訪問私人服務(必須login的服務 URL的路徑可含/private)
把返回的{JWT}放到HTTP HEADER之中，格式是 Key="Authorization" value="Bearer {JWT}"
```
GET http://localhost:8888/auth/private/v1/greeting with HEARDER
Authorization: Bearer {JWT}
Return 200 if success, 403 if token invalid.
```
## 添加新的API(需要驗證)到getway。
例如添加 http://localhost:9300/book/private/remove/{id} 及 http://localhost:9300/book/list

其中http://localhost:9300/book/private/remove/{id} 需要{JWT}

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
可以通過API網關訪問它們

http://localhost:8888/book/private/remove/{id} (因為路徑包括/private網關會請求auth-service驗證才會向下轉發)

http://localhost:8888/book/list


