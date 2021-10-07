package com.juliano.cursomc.services;

import com.juliano.cursomc.domain.Cidade;
import com.juliano.cursomc.domain.Cliente;
import com.juliano.cursomc.domain.Endereco;
import com.juliano.cursomc.domain.enums.Perfil;
import com.juliano.cursomc.domain.enums.TipoCliente;
import com.juliano.cursomc.dto.ClienteDTO;
import com.juliano.cursomc.dto.ClienteNewDTO;
import com.juliano.cursomc.repositories.ClienteRepository;
import com.juliano.cursomc.repositories.EnderecoRepository;
import com.juliano.cursomc.security.UsuarioSpringSecurity;
import com.juliano.cursomc.services.exceptions.AutorizacaoExcecao;
import com.juliano.cursomc.services.exceptions.ExcecaoIntegridadeDados;
import com.juliano.cursomc.services.exceptions.ObjetoNaoEncontradoExcecao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.awt.image.BufferedImage;
import java.net.URI;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    @Value("${default.sender}") //atributo sender pega o valor do default.sender do application.properties
    private String sender;

    @Autowired //objeto e instanciado automaticamente, repositorio para salvar os clientes no banco de dados
    private ClienteRepository repo;

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private BCryptPasswordEncoder pe;
    //pe.encode(objDto.getSenha()) -> a senha passada pelo usuario na URI vai criptografada para o Banco de Dados

    @Autowired
    AmazonS3Service s3Service;

    @Autowired
    ImageService imageService;

    @Autowired
    private EmailService emailService;

    @Value("${img.prefix.client.profile}")
    private String prefix;

    public Cliente buscar(Integer id) {
        UsuarioSpringSecurity user = UsuarioServico.autenticado(); //busca o usuario logado
        if(user==null || !user.possuiPerfil(Perfil.ADMIN) && !id.equals(user.getId())) { //se o usuario for nulo ou nao tiver perfil ADMIN e se o ID buscado nao pertencer ao usuario logado
            throw new AutorizacaoExcecao("Acesso negado");
        }
        Optional<Cliente> obj = repo.findById(id);
        return obj.orElseThrow(() -> new ObjetoNaoEncontradoExcecao( //excecao personalizada informando qdo nao se encontra a chave-primaria
                "Objeto não encontrado! Id: " + id + ", Tipo: " + Cliente.class.getName()));
    }

    @Transactional
    public Cliente inserir(Cliente obj) {
        obj.setId(null); //evita que objetos ja existentes sejam inseridos novamente
        obj = repo.save(obj);
        enderecoRepository.saveAll(obj.getEnderecos()); //salva o endereco junto ao cliente na criacao
        SimpleMailMessage sm = new SimpleMailMessage();
        sm.setTo(obj.getEmail()); //destinatario
        sm.setSubject("Cadastro no Aplicativo");
        sm.setSentDate(new Date(System.currentTimeMillis())); //email e mandado informando a hora do servidor
        sm.setText("Cadastro realizado com sucesso!!!\n" +
                "Parabens "+obj.getNome()+"!!!\n" +
                "Usuario: "+obj.getEmail()+"\n"); //corpo do Email, o toString do Pedido
        sm.setFrom(sender);
        emailService.mandarEmail(sm);
        return obj;
    }

    public Cliente atualizar(Cliente obj){
        buscar(obj.getId()); //procura o objeto e lanca uma excecao caso nao encontre
        Cliente newObj = buscar(obj.getId());
        atualizarData(newObj,obj); //pega o objeto do banco de dados e atualiza o nome e email
        return repo.save(newObj);
    }

    public void deletar(Integer id){
        buscar(id);
        try {
            repo.deleteById(id);
        }
        catch (DataIntegrityViolationException e){
            throw new ExcecaoIntegridadeDados("Não é possivel excluir porque há entidades relacionadas");
        }
    }

    public List<Cliente> buscarTodos(){
        return repo.findAll();
    }

    public Cliente buscarPorEmail(String email){
        UsuarioSpringSecurity user = UsuarioServico.autenticado(); //buscar por usuario autenticado
        if (user == null || !user.possuiPerfil(Perfil.ADMIN) && !email.equals(user.getUsername())) { //se o usuario for nulo ou nao for ADMIN ou email nao for dele
            throw new AutorizacaoExcecao("Acesso negado");
        }

        Cliente obj = repo.findByEmail(email);
        if (obj == null) {
            throw new ObjetoNaoEncontradoExcecao(
                    "Objeto não encontrado! Id: " + user.getId() + ", Tipo: " + Cliente.class.getName());
        }
        return obj;
    }

    //Controle de Paginacao -> determina ate quantos objetos serao carregados por consulta, evitando uso excessivo de memoria
    public Page<Cliente> buscarPagina(Integer page, Integer linesPerPage, String orderBy, String direction){
        PageRequest pageRequest = PageRequest.of(page,linesPerPage, Sort.Direction.valueOf(direction),orderBy);
        return repo.findAll(pageRequest);
    }

    public Cliente fromDTO(ClienteDTO objDto){
        return new Cliente(objDto.getId(),objDto.getNome(),objDto.getEmail(),null,null,null);
    }

    public Cliente fromDTO(ClienteNewDTO objDto){
        Cliente cli = new Cliente(null,objDto.getNome(),objDto.getEmail(),objDto.getCpfOuCnpj(), TipoCliente.toEnum(objDto.getTipo()),pe.encode(objDto.getSenha()));
        Cidade cid = new Cidade(objDto.getCidadeId(), null, null);
        Endereco end = new Endereco(null,objDto.getLogradouro(),objDto.getNumero(),objDto.getBairro(),objDto.getComplemento(),objDto.getCep(),cli,cid);
        cli.getEnderecos().add(end); //endereco adicionado ao cliente
        cli.getTelefones().add(objDto.getTelefone1()); //cadastra o primeiro telefone->obrigatorio
        if(objDto.getTelefone2()!=null){
            cli.getTelefones().add(objDto.getTelefone2());
        }
        if(objDto.getTelefone3()!=null){
            cli.getTelefones().add(objDto.getTelefone3());
        }
        return cli;
    }


    private void atualizarData(Cliente newObj, Cliente obj){
        newObj.setNome(obj.getNome());
        newObj.setEmail(obj.getEmail());
    }

    public URI uploadProfilePicture(MultipartFile multipartFile){ //faz o upload da foto do cliente no AWS
        UsuarioSpringSecurity user = UsuarioServico.autenticado(); //procura um usuario logado
        if(user==null){
            throw new AutorizacaoExcecao("Acesso negado");
        }

        BufferedImage jpgImagem = imageService.getJpgImageFromFile(multipartFile);//extrai o jpg do arquivo MultipartFile
        String fileName = prefix + user.getId() + ".jpg"; //nome q sera dado a imagem
        return s3Service.uploadFile(imageService.getInputStream(jpgImagem,"jpg"),fileName,"image");
    }


}
