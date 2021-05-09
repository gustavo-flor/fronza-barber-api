package com.github.gustavoflor.fronzabarberapi.infrastructure.shared.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.impl.DefaultJwtBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

class JwtHelperTest {

    @Mock
    private DefaultJwtBuilder jwtBuilder;

    private String subject;
    private String secret;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        subject = UUID.randomUUID().toString();
        secret = Instant.now().toString();
    }

    @Test
    void shouldCreateAndParse() {
//        Mockito.mockStatic(Jwts.class).when(Jwts::builder).thenReturn(jwtBuilder);
//        Mockito.doAnswer(answer -> {
//            return answer.getMock();
//        }).when(jwtBuilder).signWith(Mockito.any(), Mockito.anyString());
        String token = JwtHelper.createToken(subject);
        String[] sections = token.split("\\.");
        int numberOfSectionsInJwt = 3;
        Assertions.assertEquals(numberOfSectionsInJwt, sections.length);
        Jws<Claims> jws = JwtHelper.getJws(token).orElse(null);
        Assertions.assertNotNull(jws);
        Assertions.assertEquals(subject, jws.getBody().getSubject());
        Assertions.assertTrue(jws.getBody().getIssuedAt().before(new Date()));
        Assertions.assertTrue(jws.getBody().getExpiration().after(new Date()));
    }

    @Test
    void shouldNotCreateWhenSubjectIsNull() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> JwtHelper.createToken(null));
    }

    @Test
    void shouldNotParseNullToken() {
        Assertions.assertFalse(JwtHelper.getJws(null).isPresent());
    }

    @Test
    void shouldNotParseInvalidToken() {
        Assertions.assertThrows(MalformedJwtException.class, () -> JwtHelper.getJws("header.payload.verifySignature"));
    }

}
