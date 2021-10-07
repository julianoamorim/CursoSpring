package com.juliano.cursomc.domain.enums;

public enum Perfil { //Perfil de acesso a seguranca dos usuarios do sistema
    /*Toda classe de "Tipo" recomenda-se declarala como enum pois como possui strings
    pre-definidas pode-se substitui-las por valores, nao e necessario instaciar objeto. Ex: os tipos de clientes
    ja sao pre-definidos nao podendo ser alterados*/

    ADMIN(1,"ROLE_ADMIN"), //Perfil admnistrador para usar o  Spring Security
    CLIENTE(2,"ROLE_CLIENTE");

    private int cod;
    private String descricao;

    private Perfil(int cod, String descricao) {
        this.cod = cod;
        this.descricao = descricao;
    }

    public int getCod() {
        return cod;
    }

    public String getDescricao() {
        return descricao;
    }

    public static Perfil toEnum(Integer cod){ //metodo usado no Getter de Cliente, onde um inteiro retorna o objeto
        if(cod==null){
            return null;
        }
        for(Perfil x: Perfil.values()){ //percorre todos os valores procurando o cod do metodo
            if(cod.equals(x.getCod())){
                return x;
            }
        }
        throw new IllegalArgumentException("Id invalido: "+cod);
    }

}
