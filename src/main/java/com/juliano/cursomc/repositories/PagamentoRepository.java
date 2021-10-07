package com.juliano.cursomc.repositories;

import com.juliano.cursomc.domain.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository //Nao e necessario criar Repository das sub-classes
public interface PagamentoRepository extends JpaRepository<Pagamento,Integer>{

}
