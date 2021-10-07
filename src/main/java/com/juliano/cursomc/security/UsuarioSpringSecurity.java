package com.juliano.cursomc.security;

import com.juliano.cursomc.domain.enums.Perfil;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

/*Classe que gera a autenticacao do Cliente (se a senha/usuario esta correto), onde e gerado um Token para acesso a aplicacao
 essa classe implementa a Classe do Spring Security UserDetails  */
public class UsuarioSpringSecurity implements UserDetails {
    private static final long serialVersionUID = 1L; //garante compatibilidade de versoes diferentes do app

    private Integer id;
    private String email;
    private String senha;
    private Collection<? extends GrantedAuthority> authorities; //Perfis do usuario (CLIENTE, ADMIN)

    public UsuarioSpringSecurity() {
    }

    public UsuarioSpringSecurity(Integer id, String email, String senha, Set<Perfil> perfis) {
        this.id = id;
        this.email = email;
        this.senha = senha;
        this.authorities = perfis.stream().map(x -> new SimpleGrantedAuthority(x.getDescricao())).collect(Collectors.toList());
    }

    public Integer getId(){
        return this.id;
    }

    //metodos que representam testes do Spring Security para o acesso a aplicacao
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return senha;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() { //A conta nao esta expirada?
        return true; //Possivel colocar um timer para expirar a sessao
    }

    @Override
    public boolean isAccountNonLocked() { //A conta nao esta bloqueada?
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() { //A credencial nao esta expirada?
        return true;
    }

    @Override
    public boolean isEnabled() { //A credencial esta ativa?
        return true;
    }

    public boolean possuiPerfil(Perfil perfil){ //testa se o usuario possui um dos perfis cadastrados, CLIENTE/ADMIN
        return getAuthorities().contains(new SimpleGrantedAuthority(perfil.getDescricao()));
    }
}
