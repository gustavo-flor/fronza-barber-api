package com.github.gustavoflor.fronzabarberapi.infrastructure.configuration;

import com.github.gustavoflor.fronzabarberapi.infrastructure.business.provider.MailProvider;
import com.github.gustavoflor.fronzabarberapi.infrastructure.business.provider.MockMailProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("test")
public class TestConfiguration {

    @Bean
    public MailProvider mailProvider() {
        return new MockMailProvider();
    }

}
