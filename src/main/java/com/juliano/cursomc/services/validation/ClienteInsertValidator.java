package com.juliano.cursomc.services.validation;

import com.juliano.cursomc.domain.Cliente;
import com.juliano.cursomc.domain.enums.TipoCliente;
import com.juliano.cursomc.dto.ClienteNewDTO;
import com.juliano.cursomc.repositories.ClienteRepository;
import com.juliano.cursomc.resources.exceptions.FieldMessage;
import com.juliano.cursomc.services.validation.utils.BR;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;

//Classe para validacao customizada na criacao de objeto
public class ClienteInsertValidator implements ConstraintValidator<ClienteInsert, ClienteNewDTO> {

    @Autowired
    private ClienteRepository repo;

    @Override
    public void initialize(ClienteInsert ann) {
    }

    @Override
    public boolean isValid(ClienteNewDTO objDto, ConstraintValidatorContext context) {
        List<FieldMessage> list = new ArrayList<>();

        if(objDto.getTipo().equals(TipoCliente.PESSOAFISICA.getCod()) && !BR.isCPF(objDto.getCpfOuCnpj())){
            list.add(new FieldMessage("cpfOuCnpj","CPF invalido"));
        }
        if(objDto.getTipo().equals(TipoCliente.PESSOAJURIDICA.getCod()) && !BR.isCNPJ(objDto.getCpfOuCnpj())){
            list.add(new FieldMessage("cpfOuCnpj","CNPJ invalido"));
        }
        if(objDto.getTipo()==null){ //validacao personalizada, com os erros personalizados
            list.add(new FieldMessage("tipo","Tipo não pode ser nulo"));
        }
        Cliente aux = repo.findByEmail(objDto.getEmail());
        if(aux != null){ //caso o email passado ja esteja cadastrado e gerado um erro
            list.add(new FieldMessage("email","Email já existente"));
        }

        // inclua os testes aqui, inserindo erros na lista
        for (FieldMessage e : list) { //percorre toda lista de validacao personalizada e transfere para a lista do framework
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(e.getMessage())
                    .addPropertyNode(e.getFieldName()).addConstraintViolation();
        }
        return list.isEmpty(); //se a lista de validacao (com erros) for vazia retorna verdadeiro, se houver algum erro retorna falso
    }
}
