package com.juliano.cursomc.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.juliano.cursomc.domain.enums.Perfil;
import com.juliano.cursomc.domain.enums.TipoCliente;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

@Entity
public class Cliente implements Serializable {
    private static final long serialVersionUID = 1L; //garante compatibilidade de versoes diferentes do app

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String nome;
    private String imageUrl;

    @Column(unique = true) //impede de se cadastrar dois emails iguais
    private String email;

    private String cpfOuCnpj;
    private Integer tipo; //Substiuir o objeto TipoCliente por um Integer, pois tem valores pre-definidos

    @JsonIgnore //omite a senha do cliente qdo se pesquisa suas informacoes
    private String senha;


    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL) //deletar um Cliente tb deleta em cascata seus enderecos
    private List<Endereco> enderecos = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "TELEFONE")//substitui uma tabela fraca(sem id) por uma colecao Set, nao permite repeticao
    private Set<String> telefones = new HashSet<>();

    @ElementCollection(fetch = FetchType.EAGER) //FetchType.EAGER->Quando um obj Cliente e chamado, o enum Peril e chamado junto
    @CollectionTable(name = "PERFIS")
    private Set<Integer> perfis = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "cliente")
    private List<Pedido> pedidos = new ArrayList<>();

    public Cliente() {
        addPerfil(Perfil.CLIENTE); //Novos usuarios tem permisao de acesso CLIENTE
    }

    public Cliente(Integer id, String nome, String email, String cpfOuCnpj, TipoCliente tipo, String senha) {
        this.senha = senha;
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.cpfOuCnpj = cpfOuCnpj;
        this.tipo = (tipo == null) ? null : tipo.getCod(); //operador ternario->caso o tipo for nulo a variavel assume varlor
                                                          //nulo, pois o getCod() nao pode retornar valor nulo
        addPerfil(Perfil.CLIENTE); //Novos usuarios tem permisao de acesso CLIENTE

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCpfOuCnpj() {
        return cpfOuCnpj;
    }

    public void setCpfOuCnpj(String cpfOuCnpj) {
        this.cpfOuCnpj = cpfOuCnpj;
    }

    public TipoCliente getTipo() {
        return TipoCliente.toEnum(tipo);
    }

    public void setTipo(TipoCliente tipo) {
        this.tipo = tipo.getCod();
    }

    public List<Endereco> getEnderecos() {
        return enderecos;
    }

    public void setEnderecos(List<Endereco> enderecos) {
        this.enderecos = enderecos;
    }

    public Set<String> getTelefones() {
        return telefones;
    }

    public void setTelefones(Set<String> telefones) {
        this.telefones = telefones;
    }

    public List<Pedido> getPedidos() { return pedidos; }

    public void setPedidos(List<Pedido> pedidos) { this.pedidos = pedidos; }

    public String getSenha() {
        return senha;
    }

    public Set<Perfil> getPerfis() { //passado o cod do Perfil retorna o objeto, usando o metodo toEnum com lambda
        return perfis.stream().map(x -> Perfil.toEnum(x)).collect(Collectors.toSet());
    }

    public void addPerfil(Perfil perfil){
        perfis.add(perfil.getCod());
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Cliente)) return false;
        Cliente cliente = (Cliente) o;
        return id.equals(cliente.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
