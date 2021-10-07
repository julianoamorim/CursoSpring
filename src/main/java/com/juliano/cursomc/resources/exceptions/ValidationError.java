package com.juliano.cursomc.resources.exceptions;

import java.util.ArrayList;
import java.util.List;
//Classe usada para customizar o MethodArgumentNotValidException do BeanValidation
public class ValidationError extends StandardError {
    private static final long serialVersionUID = 1L;

    private List<FieldMessage> erros = new ArrayList<>();

    public ValidationError() {
    }

    public ValidationError(Long timestamp, Integer status, String msg, String error, String path) {
        super(timestamp, status, msg, error, path);
    }

    public List<FieldMessage> getErrors() {
        return erros;
    }

    public void addError(String fieldName, String message){
        erros.add(new FieldMessage(fieldName,message));
    }
}
