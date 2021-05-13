package com.github.gustavoflor.fronzabarberapi.infrastructure.configuration.environment;

import com.github.gustavoflor.fronzabarberapi.infrastructure.business.provider.GoogleMailProvider;
import com.github.gustavoflor.fronzabarberapi.infrastructure.business.provider.MailProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("production")
public class ProductionConfiguration {

    @Bean
    public MailProvider mailProvider() {
        return new GoogleMailProvider();
    }

}
