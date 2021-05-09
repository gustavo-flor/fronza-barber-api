package com.github.gustavoflor.fronzabarberapi.infrastructure.shared.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.impl.DefaultJwtBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

class JwtHelperTest {

    private String subject;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        subject = UUID.randomUUID().toString();
    }

    @Test
    void shouldCreateAndParse() {
        String token = JwtHelper.createToken(subject);
        String[] sections = token.split("\\.");
        int numberOfSectionsInJwt = 3;
        Assertions.assertEquals(numberOfSectionsInJwt, sections.length);
        Claims claims = JwtHelper.getClaims(token).orElse(null);
        Assertions.assertNotNull(claims);
        Assertions.assertEquals(subject, claims.getSubject());
        Assertions.assertTrue(claims.getIssuedAt().before(new Date()));
        Assertions.assertTrue(claims.getExpiration().after(new Date()));
    }

    @Test
    void shouldNotCreateWhenSubjectIsNull() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> JwtHelper.createToken(null));
    }

    @Test
    void shouldNotParseNullToken() {
        Assertions.assertFalse(JwtHelper.getClaims(null).isPresent());
    }

    @Test
    void shouldNotParseInvalidToken() {
        Assertions.assertThrows(MalformedJwtException.class, () -> JwtHelper.getClaims("header.payload.verifySignature"));
    }

}
