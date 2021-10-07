package com.juliano.cursomc.services.validation;

import com.juliano.cursomc.domain.Cliente;
import com.juliano.cursomc.dto.ClienteDTO;
import com.juliano.cursomc.repositories.ClienteRepository;
import com.juliano.cursomc.resources.exceptions.FieldMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//Classe para validacao customizada de update do objeto
public class ClienteUpdateValidator implements ConstraintValidator<ClienteUpdate, ClienteDTO> {
    @Autowired
    private HttpServletRequest request; //requisicao para pegar o id da URI

    @Autowired
    private ClienteRepository repo;

    @Override
    public void initialize(ClienteUpdate ann) {
    }

    @Override
    public boolean isValid(ClienteDTO objDto, ConstraintValidatorContext context) {
        //requisicao para pegar o id da URI(URL) e comparar com o do Banco de Dados
        Map<String,String> map = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        Integer uriId = Integer.parseInt(map.get("id")); //converte a Id de String para inteiro

        List<FieldMessage> list = new ArrayList<>();

        Cliente aux = repo.findByEmail(objDto.getEmail());
        if(aux != null && !aux.getId().equals(uriId)){ //verifica se o email passado esta cadastrado em outro Cliente
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
