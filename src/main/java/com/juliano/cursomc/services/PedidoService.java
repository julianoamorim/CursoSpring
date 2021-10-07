package com.juliano.cursomc.services;

import com.juliano.cursomc.domain.Cliente;
import com.juliano.cursomc.domain.ItemPedido;
import com.juliano.cursomc.domain.PagamentoComBoleto;
import com.juliano.cursomc.domain.Pedido;
import com.juliano.cursomc.domain.enums.EstadoPagamento;
import com.juliano.cursomc.repositories.ClienteRepository;
import com.juliano.cursomc.repositories.ItemPedidoRepository;
import com.juliano.cursomc.repositories.PagamentoRepository;
import com.juliano.cursomc.repositories.PedidoRepository;
import com.juliano.cursomc.security.UsuarioSpringSecurity;
import com.juliano.cursomc.services.exceptions.AutorizacaoExcecao;
import com.juliano.cursomc.services.exceptions.ObjetoNaoEncontradoExcecao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class PedidoService {

    @Autowired //objeto e instanciado automaticamente
    private PedidoRepository repo; //repositorio de Pedidos

    @Autowired
    private BoletoService boletoService;

    @Autowired
    private PagamentoRepository pagamentoRepository;

    @Autowired
    private ProdutoService produtoService;

    @Autowired
    private ItemPedidoRepository itemPedidoRepository;

    @Autowired
    private ClienteService clienteService; //Da acesso ao repositorio do Cliente

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private EmailService emailService;

    public Pedido buscar(Integer id) {
        Optional<Pedido> obj = repo.findById(id);
        if(obj==null) {
            obj.orElseThrow(() -> new ObjetoNaoEncontradoExcecao( //tratamento caso a id nao exista
                    "Objeto não encontrado! Id: " + id + ", Tipo: " + Pedido.class.getName()));
        }
        return obj.orElse(null);
    }

    public Pedido inserir(Pedido obj){
        obj.setId(null); //garante que esta sendo inserido um novo pedido
        obj.setInstante(new Date()); //nova data de pagamento
        obj.setCliente(clienteService.buscar(obj.getCliente().getId())); //pega o ID do Cliente para buscar o objeto Cliente no repositorio, que sera mostrado no toString()
        obj.getPagamento().setEstado(EstadoPagamento.PENDENTE);
        obj.getPagamento().setPedido(obj);
        if(obj.getPagamento() instanceof PagamentoComBoleto){ //se o obj tiver pagamento com boleto entao faca:
            PagamentoComBoleto pagto = (PagamentoComBoleto) obj.getPagamento();
            boletoService.preencherPagamentoComBoleto(pagto, obj.getInstante()); //preeche a data de vencimento, uma semana apos a criacao
        }
        obj = repo.save(obj);
        pagamentoRepository.save(obj.getPagamento()); //salva o pagamento no repositorio
        for(ItemPedido ip : obj.getItens()){
            ip.setDesconto(0.0);
            ip.setProduto(produtoService.buscar(ip.getProduto().getId())); //pega o ID do Produto para buscar o objeto Produto no repositorio, que sera mostrado no toString()
            ip.setPreco(ip.getProduto().getPreco()); //coloca o preco do pedido igual ao preco do produto
            ip.setPedido(obj);
        }
        itemPedidoRepository.saveAll(obj.getItens());
        emailService.mandarEmailConfirmacao(obj);

        return obj;
    }

    //Controle de Paginacao -> determina ate quantos objetos serao carregados por consulta, evitando uso excessivo de memoria
    public Page<Pedido> buscarPagina(Integer page, Integer linesPerPage, String orderBy, String direction){
        UsuarioSpringSecurity user = UsuarioServico.autenticado();
        if(user==null){
            throw new AutorizacaoExcecao("Acesso negado");
        }
        PageRequest pageRequest = PageRequest.of(page,linesPerPage, Sort.Direction.valueOf(direction),orderBy);
        Cliente cliente = clienteService.buscar(user.getId());
        return repo.findByCliente(cliente,pageRequest);
    }

}