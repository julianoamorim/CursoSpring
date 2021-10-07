package com.juliano.cursomc.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@Entity //A Classe possui algum erro, verificar com versao do curso
public class Produto implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String nome;
    private Double preco;


    @JsonIgnore //omite a referencia, deixa apenas para a categoria
    @ManyToMany
    @JoinTable(name = "PRODUTO_CATEGORIA", //nova tabela criada para unir as tabelas PRODUTO e  CATEGORIA
            joinColumns = @JoinColumn(name = "produto_id"), //chave estrangeira do PRODUTO
            inverseJoinColumns = @JoinColumn(name = "categoria_id"))//chave estrangeira da CATEGORIA
    private List<Categoria> categorias = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "id.produto")
    private Set<ItemPedido> itens = new HashSet<>();

    @JsonIgnore //evita referencia ciclica
    public List<Pedido> getPedidos(){
        List<Pedido> lista = new ArrayList<>();
        for(ItemPedido x : itens){ //percorre todos os elementos da Lista<ItemPedido> itens
            lista.add(x.getPedido()); //adiciona o novo elemento(pedido) na Lista itens
        }
        return lista;
    }

    public Produto() {
        super();
    }

    public Produto(Integer id, String nome, Double preco) {
        super();
        this.id = id;
        this.nome = nome;
        this.preco = preco;
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

    public Double getPreco() {
        return preco;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }

    public List<Categoria> getCategorias() {
        return categorias;
    }

    public void setCategorias(List<Categoria> categorias) {
        this.categorias = categorias;
    }

    public Set<ItemPedido> getItens() { return itens; }

    public void setItens(Set<ItemPedido> itens) { this.itens = itens; }


    @Override //equals retirado do codigo do curso, o gerado pelo Intellij estava dando erro de NullPointerException
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Produto other = (Produto) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }


}
