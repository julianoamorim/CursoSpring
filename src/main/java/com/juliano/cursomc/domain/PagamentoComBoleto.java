package com.juliano.cursomc.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.juliano.cursomc.domain.enums.EstadoPagamento;

import javax.persistence.Entity;
import java.util.Date;

@Entity
@JsonTypeName("pagamentoComBoleto") //como o objeto sera exportado para o FrontEnd
public class PagamentoComBoleto extends Pagamento {
    private static final long serialVersionUID = 1L; //garante compatibilidade de versoes diferentes do app
    @JsonFormat(pattern = "dd/MM/yyy") //mascara para a data do pedido na URL do Json
    private Date dataVencimento;
    @JsonFormat(pattern = "dd/MM/yyy") //mascara para a data do pedido na URL do Json
    private Date dataPagamento;

    public PagamentoComBoleto() {
    }

    public PagamentoComBoleto(Integer id, EstadoPagamento estado, Pedido pedido, Date dataVencimento, Date dataPagamento) {
        super(id, estado, pedido);
        this.dataVencimento = dataVencimento;
        this.dataPagamento = dataPagamento;
    }

    public Date getDataVencimento() {
        return dataVencimento;
    }

    public void setDataVencimento(Date dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    public Date getDataPagamento() {
        return dataPagamento;
    }

    public void setDataPagamento(Date dataPagamento) {
        this.dataPagamento = dataPagamento;
    }
}
