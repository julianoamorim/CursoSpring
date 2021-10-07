package com.juliano.cursomc.domain.enums;

public enum EstadoPagamento {
    /*Toda classe de "Tipo" recomenda-se declarala como enum pois como possui strings
    pre-definidas pode-se substitui-las por valores. Ex: os tipos de clientes ja sao pre-definidos nao podendo ser
    alterados*/

    PENDENTE(1,"Pendente"),
    QUITADO(2,"Quitado"),
    CANCELADO(3,"Cancelado");

    private int cod;
    private String descricao;

    private EstadoPagamento(int cod, String descricao) {
        this.cod = cod;
        this.descricao = descricao;
    }

    public int getCod() {
        return cod;
    }

    public String getDescricao() {
        return descricao;
    }

    public static EstadoPagamento toEnum(Integer cod){ //metodo usado no Getter de Cliente, onde um inteiro retorna o objeto
        if(cod==null){
            return null;
        }
        for(EstadoPagamento x: EstadoPagamento.values()){ //percorre todos os valores procurando o cod do metodo
            if(cod.equals(x.getCod())){
                return x;
            }
        }
        throw new IllegalArgumentException("Id invalido: "+cod);
    }

}
