package com.github.gustavoflor.fronzabarberapi.infrastructure.business.provider;

import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MockMailProvider implements MailProvider {

    @Override
    public void send(SimpleMailMessage message) {
        String subject = message.getSubject();
        String text = message.getText();
        String from = message.getFrom();
        String[] to = message.getTo();
        log.info("Simulando envio de email...");
        log.info("Título: {}", subject);
        log.info("Corpo: {}", text);
        log.info("De: {}, Para: {}", from, to);
        log.info("Email enviado com sucesso!");
    }

}
