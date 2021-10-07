package com.juliano.cursomc.domain.enums;

public enum  TipoCliente {
    /*Toda classe de "Tipo" recomenda-se declarala como enum pois como possui strings
    pre-definidas pode-se substitui-las por valores. Ex: os tipos de clientes ja sao pre-definidos nao podendo ser
    alterados*/

    PESSOAFISICA(1,"Pessoa Física"),
    PESSOAJURIDICA(2,"Pessoa Jurídica");

    private int cod;
    private String descricao;

    private TipoCliente(int cod, String descricao) {
        this.cod = cod;
        this.descricao = descricao;
    }

    public int getCod() {
        return cod;
    }

    public String getDescricao() {
        return descricao;
    }

    //esse metodo possibilita definir o tipo de cliente informando seu codigo
    public static TipoCliente toEnum(Integer cod){ //metodo usado no Getter de Cliente, onde um inteiro retorna o objeto
        if(cod==null){
            return null;
        }
        for(TipoCliente x: TipoCliente.values()){ //percorre todos os valores procurando o cod do metodo
            if(cod.equals(x.getCod())){
                return x;
            }
        }
        throw new IllegalArgumentException("Id invalido: "+cod);
    }
}
