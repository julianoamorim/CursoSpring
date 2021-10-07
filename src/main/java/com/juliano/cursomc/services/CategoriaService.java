package com.juliano.cursomc.services;

import com.juliano.cursomc.domain.Categoria;
import com.juliano.cursomc.dto.CategoriaDTO;
import com.juliano.cursomc.repositories.CategoriaRepository;
import com.juliano.cursomc.services.exceptions.ExcecaoIntegridadeDados;
import com.juliano.cursomc.services.exceptions.ObjetoNaoEncontradoExcecao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoriaService {

    @Autowired //objeto e instanciado automaticamente
    private CategoriaRepository repo;

    public Categoria buscar(Integer id) {
        Optional<Categoria> obj = repo.findById(id);
        return obj.orElseThrow(() -> new ObjetoNaoEncontradoExcecao( //excecao personalizada informando qdo nao se encontra a chave-primaria
                "Objeto não encontrado! Id: " + id + ", Tipo: " + Categoria.class.getName()));
    }

    public Categoria inserir(Categoria obj) {
        obj.setId(null); //evita que objetos ja existentes sejam inseridos novamente
        return repo.save(obj);
    }

    public Categoria atualizar(Categoria obj){
        buscar(obj.getId()); //procura o objeto e lanca uma excecao caso nao encontre
        Categoria newObj = buscar(obj.getId());
        atualizarData(newObj,obj); //pega o objeto do banco de dados e atualiza o nome e email
        return repo.save(newObj);
    }

    public void deletar(Integer id){
        buscar(id);
        try {
            repo.deleteById(id);
        }
        catch (DataIntegrityViolationException e){
            throw new ExcecaoIntegridadeDados("Não é possivel excluir uma Categoria que possui Produtos");
        }
    }

    public List<Categoria> buscarTodos(){
        return repo.findAll();
    }

    //Controle de Paginacao -> determina ate quantos objetos serao carregados por consulta, evitando uso excessivo de memoria
    public Page<Categoria> buscarPagina(Integer page, Integer linesPerPage, String orderBy, String direction){
        PageRequest pageRequest = PageRequest.of(page,linesPerPage, Sort.Direction.valueOf(direction),orderBy);
        return repo.findAll(pageRequest);
    }

    public Categoria fromDTO(CategoriaDTO objDto){
        return new Categoria(objDto.getId(),objDto.getNome());
    }

    private void atualizarData(Categoria newObj, Categoria obj){
        newObj.setNome(obj.getNome());
    }
}
