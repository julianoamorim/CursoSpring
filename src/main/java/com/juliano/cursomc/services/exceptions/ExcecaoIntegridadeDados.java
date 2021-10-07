package com.juliano.cursomc.services.exceptions;

//Classe criada para fazer excecoes personalizadas
public class ExcecaoIntegridadeDados extends RuntimeException {
    public ExcecaoIntegridadeDados(String msg){
        super(msg);
    }
    public ExcecaoIntegridadeDados(String msg, Throwable cause){
        super(msg,cause);
    }
}
