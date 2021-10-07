package com.juliano.cursomc.services;
import com.juliano.cursomc.domain.Cliente;
import com.juliano.cursomc.repositories.ClienteRepository;
import com.juliano.cursomc.security.UsuarioSpringSecurity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

//Classe que busca um Cliente pelo email na autenticacao do usuario
@Service
public class UsuarioDetailsServiceImplemt implements UserDetailsService {

    @Autowired
    private ClienteRepository repo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Cliente cli = repo.findByEmail(email);
        if (cli == null) {
            throw new UsernameNotFoundException(email);
        }
        return new UsuarioSpringSecurity(cli.getId(), cli.getEmail(), cli.getSenha(), cli.getPerfis());
    }
}
