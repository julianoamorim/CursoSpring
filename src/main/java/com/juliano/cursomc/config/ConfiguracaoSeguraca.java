package com.juliano.cursomc.config;

import com.juliano.cursomc.security.JWTFiltroAutenticacao;
import com.juliano.cursomc.security.JWTFiltroAutorizacao;
import com.juliano.cursomc.security.JWTUtil;
import com.juliano.cursomc.services.UsuarioDetailsServiceImplemt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration //classe de configuracao
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ConfiguracaoSeguraca extends WebSecurityConfigurerAdapter {

    @Autowired
    private Environment env;


    @Autowired
    private UsuarioDetailsServiceImplemt userDetailsService;
    @Autowired
    private JWTUtil jwtUtil;

    public static final String[] PUBLIC_MATCHERS = { //caminhos que estarao liberados da seguranca por padrao
            "/h2-console/**"
    };

    public static final String[] PUBLIC_MATCHERS_GET = { //caminhos que estarao liberados da seguranca, mas nao pode alterar dados
            "/produtos/**",
            "/categorias/**",
            "/clientes/**",
            "/pedidos/**",
            "/estados/**"
    };

    public static final String[] PUBLIC_MATCHERS_POST = { //caminho para cadastro de usuario
            "/clientes/**",
            "/auth/forgot/**"
    };

    @Override //Libera o swagger nas configuracoes de seguranca
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/v2/api-docs", "/configuration/ui", "/swagger-resources/**", "/configuration/**",
                "/swagger-ui.html", "/webjars/**");
    }

    @Override //sobrescrever metodo configure do WebSecurityConfigurerAdapter
    protected void configure(HttpSecurity http) throws Exception{
        if (Arrays.asList(env.getActiveProfiles()).contains("test")) { //se o perfil test estiver ativo em application.properties
            http.headers().frameOptions().disable(); //libera o acesso ao H2Database
        }
        http.cors().and().csrf().disable(); //desabilita a protecao contra CSRF (copia de requisicao http)
        http.authorizeRequests()
                .antMatchers(HttpMethod.POST,PUBLIC_MATCHERS_POST).permitAll()
                .antMatchers(HttpMethod.GET,PUBLIC_MATCHERS_GET).permitAll()//permite acessar a URI mas nao altera-ls
                .antMatchers(PUBLIC_MATCHERS).permitAll() //todos os caminho do vetor estao livres de seguranca
                .anyRequest().authenticated(); //para o restante exigir autenticacao
        http.addFilter(new JWTFiltroAutenticacao(authenticationManager(),jwtUtil));
        http.addFilter(new JWTFiltroAutorizacao(authenticationManager(),jwtUtil,userDetailsService));
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS); //back-end nao cria sessao de usuario
    }

    @Override //sobrescrita do metodo de autenticao do Spring Security informando o UsuarioDetailsServiceImplemt
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
    }

    @Bean // Metodo do CORS
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration().applyPermitDefaultValues();
        configuration.setAllowedMethods(Arrays.asList("POST", "GET", "PUT", "DELETE", "OPTIONS")); //Metodos liberados para o CORS
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    /*@Bean
    public FilterRegistrationBean<CorsFilter> simpleCorsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOriginPatterns(Collections.singletonList("*"));
        config.setAllowedMethods(Collections.singletonList("*"));
        config.setAllowedHeaders(Collections.singletonList("*"));
        source.registerCorsConfiguration("/**", config);
        FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(new CorsFilter(source));
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return bean;
    }*/

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){ //metodo para criptografar a senha do usuario
        return new BCryptPasswordEncoder();
    }
}
