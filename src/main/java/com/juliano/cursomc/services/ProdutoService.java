package com.juliano.cursomc.services;

import com.juliano.cursomc.domain.Categoria;
import com.juliano.cursomc.domain.Produto;
import com.juliano.cursomc.repositories.CategoriaRepository;
import com.juliano.cursomc.repositories.ProdutoRepository;
import com.juliano.cursomc.services.exceptions.ObjetoNaoEncontradoExcecao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProdutoService {

    @Autowired //objeto e instanciado automaticamente
    private ProdutoRepository repo;

    @Autowired
    private CategoriaRepository categoriaRepository;

    public Produto buscar(Integer id) {
        Optional<Produto> obj = repo.findById(id);
        if(obj==null) {
            obj.orElseThrow(() -> new ObjetoNaoEncontradoExcecao( //tratamento caso a id nao exista
                    "Objeto não encontrado! Id: " + id + ", Tipo: " + Produto.class.getName()));
        }
        return obj.orElse(null);
    }

    //busca paginada
    public Page<Produto> buscarPagina(String nome, List<Integer> ids, Integer page, Integer linesPerPage, String orderBy, String direction){
        PageRequest pageRequest = PageRequest.of(page,linesPerPage, Sort.Direction.valueOf(direction),orderBy);
        List<Categoria> categorias = categoriaRepository.findAllById(ids);
        return  repo.buscar(nome,categorias,pageRequest);
    }

}