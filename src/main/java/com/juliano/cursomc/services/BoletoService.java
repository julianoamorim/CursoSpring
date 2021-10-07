package com.juliano.cursomc.services;

import com.juliano.cursomc.domain.PagamentoComBoleto;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;

@Service
public class BoletoService { //metodo que define o vencimento do boleto 7 dias apos a compra
    public void preencherPagamentoComBoleto(PagamentoComBoleto pagto, Date instantePedido){
        Calendar cal = Calendar.getInstance();
        cal.setTime(instantePedido);
        cal.add(Calendar.DAY_OF_MONTH,7);
        pagto.setDataVencimento(cal.getTime());
    }
}
