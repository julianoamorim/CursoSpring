package com.juliano.cursomc.services.exceptions;

//Classe criada para fazer excecoes personalizadas
public class ObjetoNaoEncontradoExcecao extends RuntimeException {
    public ObjetoNaoEncontradoExcecao(String msg){
        super(msg);
    }
    public ObjetoNaoEncontradoExcecao(String msg, Throwable cause){
        super(msg,cause);
    }
}
