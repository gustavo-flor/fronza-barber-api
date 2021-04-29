package com.github.gustavoflor.fronzabarberapi.infrastructure.delivery;

import com.github.gustavoflor.fronzabarberapi.infrastructure.shared.util.JwtHelper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import lombok.AllArgsConstructor;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

@AllArgsConstructor
public class AuthenticationFilter extends GenericFilterBean {

    private final AuthenticationService authenticationService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) {
        JwtHelper.getJws((HttpServletRequest) request)
                .map(Jwt::getBody)
                .map(Claims::getSubject)
                .flatMap(authenticationService::findBySubject)
                .ifPresent(AuthenticationContext::setCurrentUser);
    }

}
