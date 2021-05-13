package com.github.gustavoflor.fronzabarberapi.infrastructure.business.provider;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

@Service
public class GoogleMailProvider implements MailProvider {

    @Override
    public void send(SimpleMailMessage message) {
        // TODO
    }

}
