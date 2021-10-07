package com.juliano.cursomc.resources;

import com.juliano.cursomc.domain.Categoria;
import com.juliano.cursomc.dto.CategoriaDTO;
import com.juliano.cursomc.services.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
//Junto com essa camada Resource(REST) sera colocada a camada de Validacao usando BeanValidation
//Esta classe e responsavel por fazer as requisicoes do Banco de Dados com a aplicacao, o CRUD
//GET = Consultar
//POST = Criar
//PUT = Mudar
//DELETE = Deletar

@RestController
@RequestMapping(value = "/categorias")
public class CategoriaResource {

    @Autowired
    private CategoriaService service;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Categoria> buscar(@PathVariable Integer id){
        /*Em vez de usar try/cath usar um obj ResourceExceptionHandler para tratar o erro em outra classe, nao e bom fazer
          esse tratamento nesta classe */
        Categoria obj = service.buscar(id);
        return ResponseEntity.ok().body(obj);
    }

    //metodo que permite inserir objetos pelo Postman, @Valid verifica a validacao dos dados definida em CategoriaDTO
    @PreAuthorize("hasAnyRole('ADMIN')") //Para usar o metodo POST e necessario ser usuario ADMIN
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> inserir(@Valid @RequestBody CategoriaDTO objDto){ //RequestBody converte o Json em obj java
        Categoria obj = service.fromDTO(objDto);
        obj = service.inserir(obj);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(obj.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    //metodo que permite atualizar objetos pelo Postman
    @PreAuthorize("hasAnyRole('ADMIN')")
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Void> atualizar(@Valid @RequestBody CategoriaDTO objDto, @PathVariable Integer id){
        Categoria obj = service.fromDTO(objDto);
        obj.setId(id); //verifica a chave-primaria passada pelo Json
        obj = service.atualizar(obj);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Categoria> deletar(@PathVariable Integer id){
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<CategoriaDTO>> buscarTodos(){
        List<Categoria> list = service.buscarTodos();
        List<CategoriaDTO> listDto = list.stream().map(
                obj -> new CategoriaDTO(obj)).collect(Collectors.toList()); //Transforma a List<Categoria> para List<CategoriaDTO>
        return ResponseEntity.ok().body(listDto);
    }

    //Metodo que representa o ENDPOINT: URL onde seu serviço pode ser acessado por uma aplicação cliente.
    //Controle de Paginacao -> determina ate quantos objetos serao carregados por consulta, evitando uso excessivo de memoria
    @RequestMapping(value ="/page", method = RequestMethod.GET)
    public ResponseEntity<Page<CategoriaDTO>> buscarPagina( //As informacoes serao passadas como parametros na URL
            @RequestParam(value = "page", defaultValue = "0") Integer page, //Ex: categorias/page?page=0&linesPerPage=20
            @RequestParam(value = "linesPerPage", defaultValue = "24") Integer linesPerPage,
            @RequestParam(value = "orderBy", defaultValue = "nome") String orderBy,
            @RequestParam(value = "direction", defaultValue = "ASC") String direction){
        Page<Categoria> list = service.buscarPagina(page,linesPerPage,orderBy,direction);
        Page<CategoriaDTO> listDto = list.map(obj -> new CategoriaDTO(obj)); //Transforma a Page<Categoria> para List<CategoriaDTO>
        return ResponseEntity.ok().body(listDto);
    }
}
