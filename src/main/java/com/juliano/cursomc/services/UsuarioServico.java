package com.juliano.cursomc.services;

import com.juliano.cursomc.security.UsuarioSpringSecurity;
import org.springframework.security.core.context.SecurityContextHolder;


public class UsuarioServico {
    public  static UsuarioSpringSecurity autenticado(){ //metodo nao precisa ser instanciado
        try{
            return (UsuarioSpringSecurity) SecurityContextHolder.getContext().getAuthentication().getPrincipal(); //retorna o usuario logado no sistema
        }catch (Exception e){
            return  null;
        }
    }
}
