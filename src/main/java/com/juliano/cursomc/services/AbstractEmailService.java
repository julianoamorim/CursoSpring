package com.juliano.cursomc.services;

import com.juliano.cursomc.domain.Cliente;
import com.juliano.cursomc.domain.Pedido;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;

import java.util.Date;

public abstract class AbstractEmailService implements EmailService {

    @Value("${default.sender}") //atributo sender pega o valor do default.sender do application.properties
    private String sender;

    @Override
    public void mandarEmailConfirmacao(Pedido obj){
        SimpleMailMessage sm = prepareSimpleMailMessageFromPedido(obj);
        mandarEmail(sm);
    }

    protected SimpleMailMessage prepareSimpleMailMessageFromPedido(Pedido obj) {
        SimpleMailMessage sm = new SimpleMailMessage();
        sm.setTo(obj.getCliente().getEmail()); //destinatario
        sm.setSubject("Pedido confirmado ! Código: "+obj.getId());
        sm.setSentDate(new Date(System.currentTimeMillis())); //email e mandado informando a hora do servidor
        sm.setText(obj.toString()); //corpo do Email, o toString do Pedido
        sm.setFrom(sender);
        return sm;
    }

    @Override
    public void mandarNovaSenhaEmail(Cliente cliente, String novaSenha){
        SimpleMailMessage sm = prepareNewPasswordEmail(cliente,novaSenha);
        mandarEmail(sm);
    }

    protected SimpleMailMessage prepareNewPasswordEmail(Cliente cliente, String novaSenha) {
        SimpleMailMessage sm = new SimpleMailMessage();
        sm.setTo(cliente.getEmail()); //destinatario
        sm.setSubject("Solicitação de Nova Senha");
        sm.setSentDate(new Date(System.currentTimeMillis())); //email e mandado informando a hora do servidor
        sm.setText("Nova senha: "+novaSenha); //corpo do Email, o toString do Pedido
        sm.setFrom(sender);
        return sm;
    }
}
