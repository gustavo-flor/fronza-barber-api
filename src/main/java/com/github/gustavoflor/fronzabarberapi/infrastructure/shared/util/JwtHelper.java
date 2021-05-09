package com.github.gustavoflor.fronzabarberapi.infrastructure.shared.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.lang.Assert;
import lombok.experimental.UtilityClass;
import org.springframework.beans.factory.annotation.Value;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@UtilityClass
public class JwtHelper {

    @Value("${application.security.jwt.secret}")
    private String SECRET = "MySecret"; // TODO: Change injection of hardcoded String.

    private final Long EXPIRATION_TIME = 43200000L;
    private final SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS512;

    public String createToken(String subject) {
        Assert.notNull(subject, "Subject cannot be null.");
        return Jwts.builder()
                .setId(UUID.randomUUID().toString())
                .setSubject(subject)
                .setIssuedAt(getIssuedAt())
                .setExpiration(getExpiration())
                .signWith(SIGNATURE_ALGORITHM, SECRET)
                .compact();
    }

    private Date getIssuedAt() {
        return new Date();
    }

    private Date getExpiration() {
        return new Date(System.currentTimeMillis() + EXPIRATION_TIME);
    }

    public Optional<Claims> getClaims(String token) {
        return Optional.ofNullable(token).map(JwtHelper::parseToken);
    }

    private Claims parseToken(String token) {
        return Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
    }

    public boolean isValid(String token) {
        return getClaims(token)
                .map(claims -> claims.getSubject() != null && claims.getExpiration().after(new Date()))
                .orElse(false);
    }

    public String getSubject(String token) {
        return getClaims(token).map(Claims::getSubject).orElse(null);
    }

}
