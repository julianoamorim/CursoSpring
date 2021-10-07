package com.juliano.cursomc.services.exceptions;

public class AutorizacaoExcecao extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public AutorizacaoExcecao(String msg) {
        super(msg);
    }

    public AutorizacaoExcecao(String msg, Throwable cause) {
        super(msg, cause);
    }

}
