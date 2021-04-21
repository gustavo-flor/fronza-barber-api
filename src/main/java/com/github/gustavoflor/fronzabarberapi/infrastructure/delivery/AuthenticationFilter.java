package com.github.gustavoflor.fronzabarberapi.infrastructure.delivery;

import com.github.gustavoflor.fronzabarberapi.infrastructure.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Optional;

@Slf4j
@AllArgsConstructor
public class AuthenticationFilter extends GenericFilterBean {

    private static final String USER_ID_HEADER_FIELD = "userId";

    private final UserService userService;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        extractUserIdFromRequestHeader(request).ifPresent(this::findCurrentUser);

        filterChain.doFilter(servletRequest, servletResponse);
    }

    private Optional<Long> extractUserIdFromRequestHeader(HttpServletRequest request) {
        try {
            return Optional.ofNullable(request.getHeader(USER_ID_HEADER_FIELD)).map(Long::parseLong);
        } catch (NumberFormatException exception) {
            log.error(String.format("Error on parse header > %s < to Long value", USER_ID_HEADER_FIELD));
        }
        return Optional.empty();
    }

    private void findCurrentUser(Long userId) {
        userService.findById(userId).ifPresent(AuthenticationContext::setCurrentUser);
    }

}
