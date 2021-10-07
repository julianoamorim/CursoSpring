package com.juliano.cursomc.dto;

import com.juliano.cursomc.domain.Categoria;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

//Classes DTO(Objeto de Transferencia de Dados) gerenciam os dados a serem trafegados em operacoes de CRUD
//Junto com esse pacote sera colocada a camada de validacao
public class CategoriaDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    //Quando houver uma requisicao Json da Categoria, so ira ser retornado o id e o nome dela, as outras informacoes serao omitidas
    private Integer id;
    @NotEmpty(message = "Preenchimento obrigatorio") //verifica a validacao dos dados a serem inseridos -> @Valid
    @Length(min=5, max = 80, message = "O tamanho deve ser entre 5 a 80 caracteres")
    private String nome;

    public CategoriaDTO() {
    }

    //metodo que converte um objeto Categoria em CategoriaDTO
    public CategoriaDTO(Categoria obj){
        id = obj.getId();
        nome = obj.getNome();
    }

    public CategoriaDTO(Integer id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
