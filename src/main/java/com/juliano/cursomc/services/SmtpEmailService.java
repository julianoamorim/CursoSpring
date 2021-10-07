package com.juliano.cursomc.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

public class SmtpEmailService extends AbstractEmailService {

    @Autowired
    private MailSender mailSender; //instancia que pega as informacoes fornecidas para acessar o servidor do Gmail no application.properties

    private static final Logger LOG = LoggerFactory.getLogger(SmtpEmailService.class);

    @Override
    public void mandarEmail(SimpleMailMessage msg) {
        LOG.info("Simulando envio de email..."); //mensagem mostrada no console do Spring Boot qdo manda o email
        mailSender.send(msg);
        LOG.info("Email enviado");
    }


}
