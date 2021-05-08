package com.github.gustavoflor.fronzabarberapi.infrastructure.shared.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Optional;

@Component
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class JwtHelper {

    private static final Long EXPIRATION_TIME = 860000000L;
    private static final String SECRET = "MySecret";
    private static final String TOKEN_PREFIX = "Bearer ";
    private static final SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS512;

    public static String createToken(String subject) {
        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(getIssuedAt())
                .setExpiration(getExpiration())
                .signWith(SIGNATURE_ALGORITHM, SECRET)
                .compact();
    }

    private static Date getIssuedAt() {
        return new Date(System.currentTimeMillis());
    }

    private static Date getExpiration() {
        return new Date(System.currentTimeMillis() + EXPIRATION_TIME);
    }

    public static Optional<Jws<Claims>> getJws(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(HttpHeaders.AUTHORIZATION))
                .map(JwtHelper::removeTokenPrefix)
                .map(JwtHelper::parseToken);
    }

    private static String removeTokenPrefix(String token) {
        return token.replace(TOKEN_PREFIX, "");
    }

    private static Jws<Claims> parseToken(String token) {
        return Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token);
    }

}
