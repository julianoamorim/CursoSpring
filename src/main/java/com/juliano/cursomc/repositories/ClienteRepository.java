package com.juliano.cursomc.repositories;

import com.juliano.cursomc.domain.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente,Integer>{

    @Transactional(readOnly =true) //Como e uma consulta nao e necessario fazer uma transacao
    Cliente findByEmail(String email); //metodo no repositorio que procura o email do Cliente

}
