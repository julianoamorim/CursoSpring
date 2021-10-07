package com.juliano.cursomc.services;


import com.juliano.cursomc.domain.Cliente;
import com.juliano.cursomc.repositories.ClienteRepository;
import com.juliano.cursomc.services.exceptions.ObjetoNaoEncontradoExcecao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Random;

//Classe equivalente a AuthService.class do curso Spring
@Service
public class NovaSenha {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private BCryptPasswordEncoder pe;

    @Autowired
    private EmailService emailService;

    private Random rand = new Random(); //gera valores aleatorios

    public void mandarNovaSenha(String email){
        Cliente cliente = clienteRepository.findByEmail(email);
        if (cliente==null){
            throw new ObjetoNaoEncontradoExcecao("Email não encontrado");
        }
        String novaSenha = NovoPassword();
        cliente.setSenha(pe.encode(novaSenha));//criptografa a nova senha
        clienteRepository.save(cliente);

        emailService.mandarNovaSenhaEmail(cliente,novaSenha);
    }

    private String NovoPassword() {
        char[] vet = new char[10];
        for(int i=0;i<10;i++){
            vet[i] = randomChar();
        }
        return new String(vet);
    }

    private char randomChar() { //metodo para criar uma senha aleatoria, baseada na tabela unicode
        int opt = rand.nextInt(3);
        if(opt==0){ //gera um numero
            return (char) (rand.nextInt(10) + 48); //escolhe 10 numeros a partir do primeiro num do unicode
        }
        else if(opt==1){ //gera letra maiuscula
            return (char) (rand.nextInt(26) + 65);
        }
        else{ //gera letra minuscula
            return (char) (rand.nextInt(26) + 97);
        }
    }


}
