package com.github.gustavoflor.fronzabarberapi.infrastructure.configuration;

import com.github.gustavoflor.fronzabarberapi.infrastructure.UserService;
import com.github.gustavoflor.fronzabarberapi.infrastructure.delivery.AuthenticationFilter;
import lombok.AllArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
public class FilterConfiguration {

    private final UserService userService;

    @Bean
    public AuthenticationFilterRegistrationBean authenticationFilterRegistrationBean() {
        AuthenticationFilterRegistrationBean registrationBean = new AuthenticationFilterRegistrationBean();
        registrationBean.setFilter(new AuthenticationFilter(userService));
        registrationBean.setOrder(1000);
        return registrationBean;
    }

    public static class AuthenticationFilterRegistrationBean extends FilterRegistrationBean<AuthenticationFilter> {
    }

}
