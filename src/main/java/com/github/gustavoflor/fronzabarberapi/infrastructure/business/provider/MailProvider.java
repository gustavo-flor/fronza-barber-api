package com.github.gustavoflor.fronzabarberapi.infrastructure.business.provider;

import org.springframework.mail.SimpleMailMessage;

public interface MailProvider {

    void send(SimpleMailMessage message);

}
