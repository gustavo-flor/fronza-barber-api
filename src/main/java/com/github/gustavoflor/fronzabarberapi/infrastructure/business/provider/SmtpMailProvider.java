package com.github.gustavoflor.fronzabarberapi.infrastructure.business.provider;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class SmtpMailProvider implements MailProvider {

    private final MailSender mailSender;

    @Override
    public void send(SimpleMailMessage message) {
        log.info("Enviando email...");
        mailSender.send(message);
        log.info("Email enviado com sucesso!");
    }

}
