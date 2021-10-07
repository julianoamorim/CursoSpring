package com.juliano.cursomc.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JWTUtil { //Classe que gera o token de autenticacao

    @Value("${jwt.secret}") //pega o valor do jwt.secret do application.propierts e coloca na variavel
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    public String gerarToken(String username){
        return Jwts.builder()
                .setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis()+expiration)) //Secao expira com o valor do expiration a partir da hora do login
                .signWith(SignatureAlgorithm.HS512,secret.getBytes()) //a string de secret vai ser embaralhada na URI junto as credencias por um algoritmo HS512
                .compact();
    }

    public boolean tokenValido(String token){
        Claims claims = getClaims(token);
        if(claims!=null){
            String username = claims.getSubject();
            Date expirationDate = claims.getExpiration();
            Date agora = new Date(System.currentTimeMillis());
            if(username!=null && expirationDate!=null && agora.before(expirationDate)){
                return true; //token ainda valido
            }
        }
        return false; //token nao e mais valido
    }

    public String getNomeUsuario(String token) {
        Claims claims = getClaims(token); //Claims: informacoes da entidade, normalmente do usuario, que e usado na validacao do token
        if (claims != null) {
            return claims.getSubject(); //Ao passar o token pelo Postman busca o usuario dono do token no Banco de Dados
        }
        return null;
    }

    private Claims getClaims(String token) {
        try {
            return Jwts.parser().setSigningKey(secret.getBytes()).parseClaimsJws(token).getBody();
        }
        catch (Exception e){
            return  null;
        }
    }


}
