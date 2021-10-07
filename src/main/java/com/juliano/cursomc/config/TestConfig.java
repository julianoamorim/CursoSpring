package com.juliano.cursomc.config;
import com.juliano.cursomc.services.DBService;
import com.juliano.cursomc.services.EmailService;
import com.juliano.cursomc.services.MockEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.text.ParseException;

@Configuration
@Profile("test") //classe que gerencia a conexao de banco de dados application-test.properties
public class TestConfig {
    //Os beans da classe so serao ativos qdo o profile test estiver ativo no properties

    @Autowired
    private DBService dbService;

    @Bean
    public boolean instanciacaoDataBase() throws ParseException {
        dbService.instanciacaoTestDatabase();
        return true;
    }

    @Bean //transforma o metodo em um componente do Spring podendo ser usado em outras classes
    public EmailService emailService(){
        return new MockEmailService();
    } //configuracao de email do perfil dev

    @Bean
    public JavaMailSender jMS (){
        return new JavaMailSenderImpl();
    }
}
