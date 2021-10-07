package com.juliano.cursomc.dto;

import com.juliano.cursomc.domain.Estado;

import javax.validation.constraints.NotEmpty;

public class EstadoDTO {
    private static final long serialVersionUID = 1L;

    private Integer id;

    @NotEmpty(message = "Preenchimento obrigatorio")
    private String nome;

    public EstadoDTO() {
    }


    public EstadoDTO(Estado obj) {
        this.id = obj.getId();
        this.nome = obj.getNome();
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
