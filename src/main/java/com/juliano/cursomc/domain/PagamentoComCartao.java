package com.juliano.cursomc.domain;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.juliano.cursomc.domain.enums.EstadoPagamento;

import javax.persistence.Entity;

@Entity
@JsonTypeName("pagamentoComCartao") //como o objeto sera exportado para o FrontEnd
public class PagamentoComCartao extends Pagamento {
     private static final long serialVersionUID = 1L; //garante compatibilidade de versoes diferentes do app
     private Integer numeroParcelas;

     public PagamentoComCartao(){
     }

     public PagamentoComCartao(Integer id, EstadoPagamento estado, Pedido pedido, Integer numeroParcelas) {
          super(id, estado, pedido);
          this.numeroParcelas = numeroParcelas;
     }

     public Integer getNumeroParcelas() {
          return numeroParcelas;
     }

     public void setNumeroParcelas(Integer numeroParcelas) {
          this.numeroParcelas = numeroParcelas;
     }
}
