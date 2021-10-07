package com.juliano.cursomc.services;

import com.juliano.cursomc.domain.Cliente;
import com.juliano.cursomc.domain.Pedido;
import org.springframework.mail.SimpleMailMessage;


public interface EmailService {
    void mandarEmailConfirmacao(Pedido obj);

    void mandarEmail(SimpleMailMessage msg);

    void mandarNovaSenhaEmail(Cliente cliente, String novaSenha);
}
