package com.github.gustavoflor.fronzabarberapi.infrastructure.delivery.filter;

import com.github.gustavoflor.fronzabarberapi.infrastructure.delivery.dto.CredentialsDTO;
import com.github.gustavoflor.fronzabarberapi.infrastructure.shared.exception.BusinessException;
import com.github.gustavoflor.fronzabarberapi.infrastructure.shared.util.JwtHelper;
import com.github.gustavoflor.fronzabarberapi.infrastructure.shared.util.ObjectParser;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@AllArgsConstructor
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    public static final String TOKEN_PREFIX = "Bearer ";

    private final AuthenticationManager authenticationManager;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        try {
            CredentialsDTO credentialsDTO = ObjectParser.transform(request.getInputStream(), CredentialsDTO.class);
            return authenticationManager.authenticate(credentialsDTO.getAuthenticationToken());
        } catch (IOException e) {
            throw new BusinessException("It was not possible to parse credentials.");
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authentication) {
        String email = ((UserDetails) authentication.getPrincipal()).getUsername();
        String token = JwtHelper.createToken(email);
        response.addHeader(HttpHeaders.AUTHORIZATION, TOKEN_PREFIX + token);
    }

}
