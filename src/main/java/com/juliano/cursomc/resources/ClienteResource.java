package com.juliano.cursomc.resources;

import com.juliano.cursomc.domain.Cliente;
import com.juliano.cursomc.dto.ClienteDTO;
import com.juliano.cursomc.dto.ClienteNewDTO;
import com.juliano.cursomc.services.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

//Esta classe e responsavel por fazer as requisicoes do Banco de Dados com a aplicacao, o CRUD
@RestController
@RequestMapping(value = "/clientes") //ENDPOINT
public class ClienteResource {

    @Autowired
    private ClienteService service;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Cliente> buscar(@PathVariable Integer id){
        /*Em vez de usar try/cath usar um obj ResourceExceptionHandler para tratar o erro em outra classe, nao e bom fazer
          esse tratamento nesta classe */
        Cliente obj = service.buscar(id);
        return ResponseEntity.ok().body(obj);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> inserir(@Valid @RequestBody ClienteNewDTO objDto){ //RequestBody converte o Json em obj java
        Cliente obj = service.fromDTO(objDto);
        obj = service.inserir(obj);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(obj.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    //metodo que permite atualizar objetos pelo Postman
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Void> atualizar(@Valid @RequestBody ClienteDTO objDto, @PathVariable Integer id){
        Cliente obj = service.fromDTO(objDto);
        obj.setId(id); //verifica a chave-primaria passada pelo Json
        obj = service.atualizar(obj);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Cliente> deletar(@PathVariable Integer id){
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value = "/email", method = RequestMethod.GET)
    public ResponseEntity<Cliente> buscar(@RequestParam(value = "value") String email){
        Cliente obj = service.buscarPorEmail(email);
        return ResponseEntity.ok().body(obj);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<ClienteDTO>> buscarTodos(){
        List<Cliente> list = service.buscarTodos();
        List<ClienteDTO> listDto = list.stream().map(
                obj -> new ClienteDTO(obj)).collect(Collectors.toList()); //Transforma a List<Cliente> para List<ClienteDTO>
        return ResponseEntity.ok().body(listDto);
    }

    @RequestMapping(value = "/picture", method = RequestMethod.POST)
    public ResponseEntity<Void> uploadFotoPerfil(@RequestParam(name = "file") MultipartFile file){
        URI uri = service.uploadProfilePicture(file);
        return ResponseEntity.created(uri).build();
    }

    //Controle de Paginacao -> determina ate quantos objetos serao carregados por consulta, evitando uso excessivo de memoria
    @RequestMapping(value ="/page", method = RequestMethod.GET)
    public ResponseEntity<Page<ClienteDTO>> buscarPagina( //As informacoes serao passadas como parametros na URL
                                                            @RequestParam(value = "page", defaultValue = "0") Integer page, //Ex: categorias/page?page=0&linesPerPage=20
                                                            @RequestParam(value = "linesPerPage", defaultValue = "24") Integer linesPerPage,
                                                            @RequestParam(value = "orderBy", defaultValue = "nome") String orderBy,
                                                            @RequestParam(value = "direction", defaultValue = "ASC") String direction){
        Page<Cliente> list = service.buscarPagina(page,linesPerPage,orderBy,direction);
        Page<ClienteDTO> listDto = list.map(obj -> new ClienteDTO(obj)); //Transforma a Page<Cliente> para List<ClienteDTO>
        return ResponseEntity.ok().body(listDto);
    }
}
