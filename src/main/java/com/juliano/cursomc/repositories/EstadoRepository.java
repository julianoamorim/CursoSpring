package com.juliano.cursomc.repositories;

import com.juliano.cursomc.domain.Estado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface EstadoRepository extends JpaRepository<Estado,Integer>{

    @Transactional(readOnly =true) //Como e uma consulta nao e necessario fazer uma transacao
    List<Estado> findAllByOrderByNome(); //metodo para buscar todos os Estados

}
