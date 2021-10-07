package com.juliano.cursomc.repositories;

import com.juliano.cursomc.domain.Categoria;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria,Integer>{

}
