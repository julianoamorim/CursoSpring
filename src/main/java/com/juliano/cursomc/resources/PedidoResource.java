package com.juliano.cursomc.resources;

import com.juliano.cursomc.domain.Pedido;
import com.juliano.cursomc.services.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

//Esta classe e responsavel por fazer as requisicoes do Banco de Dados com a aplicacao, o CRUD
@RestController
@RequestMapping(value = "/pedidos")
public class PedidoResource {

    @Autowired
    private PedidoService service;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Pedido> find(@PathVariable Integer id){
        /*Em vez de usar try/cath usar um obj ResourceExceptionHandler para tratar o erro em outra classe, nao e bom fazer
          esse tratamento nesta classe */
        Pedido obj = service.buscar(id);
        return ResponseEntity.ok().body(obj);
    }

    //metodo que permite inserir objetos pelo Postman, @Valid verifica a validacao dos dados definida em Pedido
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> inserir(@Valid @RequestBody Pedido obj){ //RequestBody converte o Json em obj java
        obj = service.inserir(obj);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(obj.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    //Metodo que representa o ENDPOINT: URL onde seu serviço pode ser acessado por uma aplicação cliente.
    @RequestMapping(method = RequestMethod.GET) //da acesso a consulta de todos os pedidos -> localhost:8080/pedidos
    public ResponseEntity<Page<Pedido>> buscarPagina( //As informacoes serao passadas como parametros na URL
                                                            @RequestParam(value = "page", defaultValue = "0") Integer page, //Ex: categorias/page?page=0&linesPerPage=20
                                                            @RequestParam(value = "linesPerPage", defaultValue = "24") Integer linesPerPage,
                                                            @RequestParam(value = "orderBy", defaultValue = "instante") String orderBy,
                                                            @RequestParam(value = "direction", defaultValue = "DESC") String direction){
        Page<Pedido> list = service.buscarPagina(page,linesPerPage,orderBy,direction);
        return ResponseEntity.ok().body(list);
    }
}
