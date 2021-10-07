package com.juliano.cursomc.services;

import com.juliano.cursomc.domain.Estado;
import com.juliano.cursomc.repositories.EstadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EstadoService {

    @Autowired
    EstadoRepository repo;

   public List<Estado> buscarTodos(){
       return  repo.findAllByOrderByNome();
   }

}
