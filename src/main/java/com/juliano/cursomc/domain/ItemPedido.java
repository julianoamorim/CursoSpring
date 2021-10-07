package com.juliano.cursomc.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import java.io.Serializable;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Objects;

@Entity
//classe de associacao, nao possui chave primaria propria, depende o id de Produto e Pedido
public class ItemPedido implements Serializable {
    private static final long serialVersionUID = 1L; //garante compatibilidade de versoes diferentes do app

    @JsonIgnore
    @EmbeddedId //id auxiliar
    private ItemPedidoPK id = new ItemPedidoPK();

    private Double desconto;
    private Integer quantidade;
    private Double preco;

    public ItemPedido() {
        super();
    }

    public ItemPedido(Pedido pedido,Produto produto, Double desconto, Integer quantidade, Double preco) {
        super();
        id.setPedido(pedido); //para criar a chave-primaria do ItemPedido e necessario as chaves do Produto e Pedido
        id.setProduto(produto); //estrategia para preecher a chave-composta
        this.desconto = desconto;
        this.quantidade = quantidade;
        this.preco = preco;
    }


    //calcula o valor de varios do mesmo item
    public double getSubTotal(){
        return (preco - desconto)*quantidade;
    }

    @JsonIgnore //evita referencia ciclica
    public Pedido getPedido(){ //com esse metodo nao e necessario ter acesso ao id para saber o pedido/produto
        return id.getPedido();
    }

    public void setPedido(Pedido pedido){ //possibilita instanciar um Pedido ao ItemPedido
        id.setPedido(pedido);
    }

    public Produto getProduto(){
        return id.getProduto();
    }

    public void setProduto(Produto produto){ //possibilita instanciar um Produto ao ItemPedido
        id.setProduto(produto);
    }

    public ItemPedidoPK getId() {
        return id;
    }

    public void setId(ItemPedidoPK id) {
        this.id = id;
    }

    public Double getDesconto() {
        return desconto;
    }

    public void setDesconto(Double desconto) {
        this.desconto = desconto;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public Double getPreco() {
        return preco;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ItemPedido)) return false;
        ItemPedido that = (ItemPedido) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(); // torna o toString mais eficiente nas requisicoes e concatenacao
        NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("pt","BR")); //formata a numeracao para o Real
        builder.append(getProduto().getNome());
        builder.append(", Qtde: ");
        builder.append(getQuantidade());
        builder.append(", Preço unitário: ");
        builder.append(nf.format(getPreco()));
        builder.append(", Subtotal: ");
        builder.append(nf.format(getSubTotal()));
        builder.append("\n");
        return builder.toString();
    }
}
