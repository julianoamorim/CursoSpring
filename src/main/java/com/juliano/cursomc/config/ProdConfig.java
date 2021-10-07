package com.juliano.cursomc.config;
import com.juliano.cursomc.services.DBService;
import com.juliano.cursomc.services.EmailService;
import com.juliano.cursomc.services.SmtpEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.text.ParseException;

@Configuration
@Profile("prod") //classe que gerencia a conexao de banco de dados application-test.properties
public class ProdConfig {
    //Os beans da classe so serao ativos qdo o profile test estiver ativo no properties

    @Autowired
    private DBService dbService;

    @Value("${spring.jpa.hibernate.ddl-auto}")
    private String strategy;

    @Bean
    public boolean instanciacaoDataBase() throws ParseException {
        if(!"create".equals(strategy)){ // caso spring.jpa.hibernate.ddl-auto != create, entao faca
            return false;
        }
        dbService.instanciacaoTestDatabase();
        return true;
    }

    @Bean //configuracao de email do perfil dev
    public EmailService emailService(){
        return new SmtpEmailService();
    }


}
