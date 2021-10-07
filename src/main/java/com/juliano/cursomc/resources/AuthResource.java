package com.juliano.cursomc.resources;


import com.juliano.cursomc.dto.EmailDTO;
import com.juliano.cursomc.security.JWTUtil;
import com.juliano.cursomc.security.UsuarioSpringSecurity;
import com.juliano.cursomc.services.NovaSenha;
import com.juliano.cursomc.services.UsuarioServico;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/*Controlador REST que disponibiliza endpoints para Autenticacao/Autorizacao gerando um Resfresh token baseado no uso do
usuario para que seu login fique salvo por mais tempo na aplicacao*/
@RestController
@RequestMapping(value = "/auth")
public class AuthResource {

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private NovaSenha service; //Equivalente AuthService

    //Endpoint protegido por autenticacao, o usuario deve estar logado p acessar o metodo
    @RequestMapping(value="/refresh_token", method= RequestMethod.POST)
    public ResponseEntity<Void> refreshToken(HttpServletResponse response) {
        UsuarioSpringSecurity user = UsuarioServico.autenticado();
        String token = jwtUtil.gerarToken(user.getUsername()); //gera um novo token q renova o tempo de expiracao da sessao
        response.addHeader("Authorization", "Bearer " + token);
        response.addHeader("access-control-expose-headers", "Authorization");
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value="/forgot", method= RequestMethod.POST)
    public ResponseEntity<Void> forgot(@Valid @RequestBody EmailDTO objDto) {
        service.mandarNovaSenha(objDto.getEmail());
        return ResponseEntity.noContent().build();
    }
}
