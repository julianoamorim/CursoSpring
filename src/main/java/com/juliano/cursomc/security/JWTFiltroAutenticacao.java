package com.juliano.cursomc.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.juliano.cursomc.dto.CredenciaisDTO;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

// UsernamePasswordAuthenticationFilter classe do SpringSecurity q sera usada como base para fazer o filtro de autenticacao
public class JWTFiltroAutenticacao extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;
    private JWTUtil jwtUtil;

    public JWTFiltroAutenticacao(AuthenticationManager authenticationManager, JWTUtil jwtUtil) {
        setAuthenticationFailureHandler(new JWTAuthenticationFailureHandler());
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @Override //sobrescrever metodo da classe para adaptar ao projeto, e um caso de BOILERPLATE: implementar algo padronizado
    public Authentication attemptAuthentication(HttpServletRequest req,
                                                HttpServletResponse res) throws AuthenticationException {
        //Nao entendi nada, aula 72 do curso SpringBoot
        try {
            CredenciaisDTO creds = new ObjectMapper()
                    .readValue(req.getInputStream(), CredenciaisDTO.class);

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(creds.getEmail(), creds.getSenha(), new ArrayList<>());

            Authentication auth = authenticationManager.authenticate(authToken);
            return auth; //variavel que informa se a autenticacao esta correta ou nao
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override //sobrescrever metodo da classe caso a autenticacao tenha sucesso, BOILERPLATE
    protected void successfulAuthentication(HttpServletRequest req,
                                            HttpServletResponse res,
                                            FilterChain chain,
                                            Authentication auth) throws IOException, ServletException {
        String username = ((UsuarioSpringSecurity) auth.getPrincipal()).getUsername();
        String token = jwtUtil.gerarToken(username);
        res.addHeader("Authorization", "Bearer " + token); //consegue passar o token para o Frontend no arquivo home.page.ts
        res.addHeader("access-control-expose-headers", "Authorization");
        /*CORS(Cross-origin resource sharing): mecanismo que gerencia quais recursos(http, headers) estao disponiveis para
         requisicoes(GET,POST,PUT,DELET) de fora do servidor*/
    }

    private class JWTAuthenticationFailureHandler implements AuthenticationFailureHandler {

        @Override
        public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
                throws IOException, ServletException {
            response.setStatus(401);
            response.setContentType("application/json");
            response.getWriter().append(json());
        }

        private String json() {
            long date = new Date().getTime();
            return "{\"timestamp\": " + date + ", "
                    + "\"status\": 401, "
                    + "\"error\": \"Não autorizado\", "
                    + "\"message\": \"Email ou senha inválidos\", "
                    + "\"path\": \"/login\"}";
        }
    }

}
