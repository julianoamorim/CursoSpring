package com.juliano.cursomc.repositories;

import com.juliano.cursomc.domain.Categoria;
import com.juliano.cursomc.domain.Produto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto,Integer>{

    @Transactional(readOnly =true) //Como e uma consulta nao e necessario fazer uma transacao
    @Query("SELECT DISTINCT obj FROM Produto obj INNER JOIN obj.categorias cat WHERE obj.nome LIKE %:nome% AND cat IN :categorias") //JQPL para consulta no banco de dados -> passa um nome ou parte dele e tentar encontrar o produto
    Page<Produto> buscar(@Param("nome") String nome,@Param("categorias") List<Categoria> categorias, Pageable pageRequest);

    /* metodo com mesma funcao que a Query acima, usando padrao de nomes do Spring boot
    Page<Produto> findDistinctByNomeContainingAndCategoriasIn(String nome,List<Categoria> categorias, Pageable pageRequest);
    Documentacao Spring: https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.bootstrap-mode.recommendations */

}
