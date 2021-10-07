package com.juliano.cursomc.resources;

import com.juliano.cursomc.domain.Produto;
import com.juliano.cursomc.dto.ProdutoDTO;
import com.juliano.cursomc.resources.utils.URL;
import com.juliano.cursomc.services.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//Esta classe e responsavel por fazer as requisicoes do Banco de Dados com a aplicacao, o CRUD
@RestController
@RequestMapping(value = "/produtos")
public class ProdutoResource {

    @Autowired
    private ProdutoService service;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Produto> find(@PathVariable Integer id){
        /*Em vez de usar try/cath usar um obj ResourceExceptionHandler para tratar o erro em outra classe, nao e bom fazer
          esse tratamento nesta classe */
        Produto obj = service.buscar(id);
        return ResponseEntity.ok().body(obj);
    }

    //Controle de Paginacao -> determina ate quantos objetos serao carregados por consulta, evitando uso excessivo de memoria
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Page<ProdutoDTO>> buscarPagina( //As informacoes serao passadas como parametros na URL
                                                          @RequestParam(value = "nome", defaultValue = "") String nome,
                                                          @RequestParam(value = "categorias", defaultValue = "") String categorias,
                                                          @RequestParam(value = "page", defaultValue = "0") Integer page, //Ex: categorias/page?page=0&linesPerPage=20
                                                          @RequestParam(value = "linesPerPage", defaultValue = "24") Integer linesPerPage,
                                                          @RequestParam(value = "orderBy", defaultValue = "nome") String orderBy,
                                                          @RequestParam(value = "direction", defaultValue = "ASC") String direction){
        String nomeDecoded = URL.decodeParam(nome);
        List<Integer> ids = URL.decodeIntList(categorias);
        Page<Produto> list = service.buscarPagina(nomeDecoded,ids,page,linesPerPage,orderBy,direction);
        Page<ProdutoDTO> listDto = list.map(obj -> new ProdutoDTO(obj)); //Transforma a Page<Categoria> para List<CategoriaDTO>
        return ResponseEntity.ok().body(listDto);
    }

}
