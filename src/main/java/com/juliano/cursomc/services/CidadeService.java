package com.juliano.cursomc.services;

import com.juliano.cursomc.domain.Cidade;
import com.juliano.cursomc.repositories.CidadeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CidadeService {

    @Autowired
    private CidadeRepository repo;

    public List<Cidade> buscarPorEstado(Integer estadoId){
        return repo.findCidades(estadoId);
    }
}
