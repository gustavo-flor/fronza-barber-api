package com.github.gustavoflor.fronzabarberapi.infrastructure.delivery.filter;

import com.github.gustavoflor.fronzabarberapi.infrastructure.shared.util.JwtHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Slf4j
public class AuthorizationFilter extends BasicAuthenticationFilter {

    private static final String TOKEN_PREFIX = AuthenticationFilter.TOKEN_PREFIX;

    private final UserDetailsService userDetailsService;

    public AuthorizationFilter(AuthenticationManager authenticationManager, UserDetailsService userDetailsService) {
        super(authenticationManager);
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws IOException, ServletException {
        Optional.ofNullable(request.getHeader(HttpHeaders.AUTHORIZATION))
                .map(this::getToken)
                .filter(JwtHelper::isValid)
                .map(JwtHelper::getSubject)
                .flatMap(this::createAuthenticationToken)
                .ifPresent(this::setAuthenticationToken);
        filterChain.doFilter(request, response);
    }

    private String getToken(String authorization) {
        if (authorization.startsWith(TOKEN_PREFIX)) {
            return authorization.replace(TOKEN_PREFIX, "");
        }
        return null;
    }

    private Optional<UsernamePasswordAuthenticationToken> createAuthenticationToken(String email) {
        try {
            UserDetails user = userDetailsService.loadUserByUsername(email);
            return Optional.of(new UsernamePasswordAuthenticationToken(user.getUsername(), null, user.getAuthorities()));
        } catch (UsernameNotFoundException exception) {
            log.debug("{} - \"{}\" args", exception.getMessage(), email);
            return Optional.empty();
        }
    }

    private void setAuthenticationToken(UsernamePasswordAuthenticationToken authenticationToken) {
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }

}
