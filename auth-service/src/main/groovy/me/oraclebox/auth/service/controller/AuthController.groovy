package me.oraclebox.auth.service.controller

import me.oraclebox.auth.service.model.Account
import me.oraclebox.auth.service.service.AuthService
import me.oraclebox.exception.AuthenticationException
import me.oraclebox.http.ResultModel
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.util.Assert
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

import javax.servlet.http.HttpServletRequest

/**
 * 處理注冊或登入請求
 * Created by oraclebox@gmail.com on 11/25/2016.
 */
@RestController
@RequestMapping("/auth")
class AuthController {

    @Autowired
    AuthService service;
    /**
     * 處理用戶名稱/密碼的注冊或登入請求
     * 首次請求創建帳戶並返回JWT
     * 其后更新JWT並返回
     *{* "username": "userABC",
     * "email":"userABC@gmail.com",
     * "password":"312313123213123123213"
     *}*/
    @RequestMapping(value = "/v1/continuous", method = RequestMethod.POST)
    ResponseEntity continuousByUsernameAndPassword(@RequestBody Map json) {
        Assert.isTrue(json != null && json.size() > 0, "Missing json.");
        Assert.isTrue(json.containsKey("username"), "Missing username.");
        Assert.isTrue(json.containsKey("email"), "Missing email.");
        Assert.isTrue(json.containsKey("password"), "Missing password.");

        String username = json.get("username").toString();
        Account account = null;
        String jwt = null;

        // Find account with same username, check password
        if ((account = service.byUsername(username)) != null) {
            // Validate
            if (service.validatePassword(json.get("password").toString(), account)) {
                jwt = service.generateJWT(account);
            } else {
                throw new AuthenticationException("Username/Password is not correct.");
            }
        }
        // Fist time sign up
        else {
            account = service.save(new Account(username: json.get("username"),
                    email: json.get("email"),
                    password: service.encryptPassword(json.get("password").toString())));
            jwt = service.generateJWT(account);
            //TODO Email verification
        }
        return new ResponseEntity<ResultModel<Account>>(ResultModel._200("Success", jwt, account), HttpStatus.OK);
    }

    /**
     * 請求標頭中取出JWT，驗証並在有效的情況下則返回帳戶資料
     * Validate JWT token in request header and return account if valid.
     */
    @RequestMapping(value = "/v1/account", method = RequestMethod.GET)
    ResponseEntity getAccount(HttpServletRequest request) {
        String token = getJWT(request);
        Account account = service.parseJWT(token);
        return new ResponseEntity<ResultModel<Account>>(ResultModel._200("Success", token, account), HttpStatus.OK);
    }

    @RequestMapping(value = "/private/v1/greeting", method = RequestMethod.GET)
    ResponseEntity greeting(HttpServletRequest request) {
        return new ResponseEntity<ResultModel<Account>>(ResultModel._200("Success", "Hello World!!!"), HttpStatus.OK);
    }

    String getJWT(HttpServletRequest request) {
        final String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new AuthenticationException("Missing Authorization header and token.");
        }
        return authHeader.substring(7);
    }
}
