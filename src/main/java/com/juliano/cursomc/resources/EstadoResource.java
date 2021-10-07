package com.juliano.cursomc.resources;

import com.juliano.cursomc.domain.Cidade;
import com.juliano.cursomc.domain.Estado;
import com.juliano.cursomc.dto.CidadeDTO;
import com.juliano.cursomc.dto.EstadoDTO;
import com.juliano.cursomc.services.CidadeService;
import com.juliano.cursomc.services.EstadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/estados") //ENDPOINT
public class EstadoResource {

    @Autowired
    private EstadoService service;

    @Autowired
    private CidadeService cidadeService;

    //mostra lista com todos os estados que o usuario tem acesso
    @RequestMapping( method = RequestMethod.GET)
    public ResponseEntity<List<EstadoDTO>> buscarTodos(){
       List<Estado> list = service.buscarTodos();
       List<EstadoDTO> listDto = list.stream().map(obj -> new EstadoDTO(obj)).collect(Collectors.toList());
       return ResponseEntity.ok().body(listDto);
    }

    //Resource do CidadeRepository
    @RequestMapping(value = "/{estadoId}/cidades", method = RequestMethod.GET)
    public ResponseEntity<List<CidadeDTO>> buscarCidades(@PathVariable Integer estadoId){
        List<Cidade> list = cidadeService.buscarPorEstado(estadoId);
        List<CidadeDTO> listDto = list.stream().map(obj -> new CidadeDTO(obj)).collect(Collectors.toList());
        return ResponseEntity.ok().body(listDto);
    }

}
