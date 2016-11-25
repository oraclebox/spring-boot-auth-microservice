package me.oraclebox.auth.service.service

import io.jsonwebtoken.*
import me.oraclebox.auth.service.model.Account
import me.oraclebox.auth.service.model.AccountRepository
import me.oraclebox.auth.service.property.ApplicationProperty
import me.oraclebox.exception.AuthenticationException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

import javax.crypto.spec.SecretKeySpec
import javax.xml.bind.DatatypeConverter
import java.security.Key

/**
 *
 * Created by oraclebox on 11/25/2016.
 */
interface AuthService {
    Account byUsername(String username);

    Account save(Account account);

    boolean validatePassword(String password, Account account);

    String encryptPassword(String password);

    String generateJWT(Account account);

    Account parseJWT(String token);
}

@Service
class AuthServiceImpl implements AuthService {

    @Autowired
    AccountRepository accountRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    ApplicationProperty property;
    @Autowired
    StringRedisTemplate redis;

    @Override
    Account byUsername(String username) {
        return accountRepository.findByUsername(username);
    }

    @Override
    Account save(Account account) {
        return accountRepository.save(account);
    }

    @Override
    boolean validatePassword(String password, Account account) {
        return passwordEncoder.matches(password, account.password);
    }

    @Override
    String encryptPassword(String password) {
        return passwordEncoder.encode(password);
    }

    @Override
    String generateJWT(Account account) {
        //The JWT signature algorithm we will be using to sign the token
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        //We will sign our JWT with our ApiKey secret
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(property.jwtPhase);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        String jwtId = UUID.randomUUID().toString();
        //Let's set the JWT Claims
        JwtBuilder builder = Jwts.builder()
                .setId(jwtId)
                .setIssuedAt(now)
                .setSubject(account.id) // User id
                .setIssuer(property.jwtIssuer)
                .setHeaderParam("typ", "JWT")
                .signWith(signatureAlgorithm, signingKey);

        //Let's add the expiration
        if (property.jwtTTL >= 0) {
            long expMillis = nowMillis + property.jwtTTL;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);
        }

        String jwt = builder.compact();

        // Remove previous token record
        redis.keys("account:tkn:" + account.id).each {
            key ->
                String id = redis.opsForValue().get("account:tkn:" + account.id);
                if (!id.equals(jwtId)) {
                    redis.delete("tkn:" + id);
                }
        }

        // Store to redis
        redis.opsForValue().set("tkn:" + jwtId, jwt);
        redis.opsForValue().set("account:tkn:" + account.id, jwtId);
        redis.expireAt("tkn:" + jwtId, new Date(nowMillis + property.jwtTTL));

        return jwt;
    }

    @Override
    Account parseJWT(String token) {
        //This line will throw an exception if it is not a signed JWS (as expected)
        Jws<Claims> jwsClaims = Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(property.jwtPhase))
                .parseClaimsJws(token);

        if (!jwsClaims.header.getAlgorithm().equals(SignatureAlgorithm.HS256.value))
            throw new AuthenticationException("Invalid JWT token." + jwsClaims.header.getAlgorithm());
        Claims claims = jwsClaims.getBody();

        // Find record from redis
        String record = redis.opsForValue().get("tkn:" + claims.getId());
        if (record == null || !token.equals(record)) {
            throw new AuthenticationException("Token is expired.");
        }

        Account account = accountRepository.findOne(claims.getSubject());
        if (account == null)
            throw new AuthenticationException("Cannot find user account.");
        if (!account.active)
            throw new AuthenticationException("Account is inactivated.");
        return account;
    }
}