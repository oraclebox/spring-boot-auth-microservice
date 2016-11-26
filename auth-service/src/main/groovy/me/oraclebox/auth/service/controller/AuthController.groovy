package me.oraclebox.auth.service.controller

import me.oraclebox.auth.service.model.Account
import me.oraclebox.auth.service.service.AuthService
import me.oraclebox.exception.AuthenticationException
import me.oraclebox.facebook.FacebookAccount
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
        Assert.isTrue(json.containsKey("email"), "Missing email.");
        Assert.isTrue(json.containsKey("password"), "Missing password.");

        String email = json.get("email").toString();
        Account account = null;
        String jwt = null;

        // Find account with same username, check password
        if ((account = service.byEmail(email)) != null) {
            // Validate
            if (service.validatePassword(json.get("password").toString(), account)) {
                jwt = service.generateJWT(account);
            } else {
                throw new AuthenticationException("Username/Password is not correct.");
            }
        }
        // Fist time sign up
        else {
            account = service.save(new Account(
                    username: json.get("username"),
                    password: service.encryptPassword(json.get("password").toString()),
                    email: email,
                    via: ["email"],
                    active: true));
            jwt = service.generateJWT(account);
            //TODO Email verification
        }
        return new ResponseEntity<ResultModel<Account>>(ResultModel._200("Success", jwt, account), HttpStatus.OK);
    }

    @RequestMapping(value = "/v1/continuous/facebook", method = RequestMethod.POST)
    ResponseEntity continuousFacebook(@RequestBody Map json) {
        Assert.isTrue(json != null && json.size() > 0, "Missing json.");
        Assert.isTrue(json.containsKey("accessToken"), "Missing facebook accessToken in json.");
        String accessToken = (String) json.get("accessToken");

        // Call facebook /me
        FacebookAccount fbAccount = service.facebookMe(accessToken).toBlocking().first();

        String jwt = null;
        Account account = null;

        // Find existing account registered by email, add facebook information
        if ((account = service.byEmail(fbAccount.email)) != null && account.via == ["email"]) {
            account.via.add("facebook");
            account.socialId = fbAccount.id;
            account.verified = true;
            service.save(account);
        }
        // Create an new account if cannot find via social account id.
        else if ((account = service.bySocialId(fbAccount.id)) == null) {
            account = service.save(new Account(
                    username: fbAccount.name,
                    socialId: fbAccount.id,
                    email: fbAccount.email,
                    verified: true,
                    via: ["facebook"],
                    active: true));
        }
        // Renew JWT token each time login
        jwt = service.generateJWT(account);

        return new ResponseEntity<ResultModel<Account>>(ResultModel._200("Success", jwt, account), HttpStatus.OK);
    }

    /**
     * 請求標頭中取出JWT，驗証並在有效的情況下則返回帳戶資料
     * Validate JWT token in request header and return account if valid.
     */
    @RequestMapping(value = "/v1/me", method = RequestMethod.GET)
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
