package com.juliano.cursomc.resources.exceptions;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.juliano.cursomc.services.exceptions.AutorizacaoExcecao;
import com.juliano.cursomc.services.exceptions.ExcecaoIntegridadeDados;
import com.juliano.cursomc.services.exceptions.FileException;
import com.juliano.cursomc.services.exceptions.ObjetoNaoEncontradoExcecao;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

//Classe para fazer o tratamento de erro gerando um JSON na pag. HTML
@ControllerAdvice
public class ResourceExceptionHandler {
    //metodo que controla respostas HTTP na pagina, caso ocorra um erro de requisicao mostra ao usuario
    @ExceptionHandler(ObjetoNaoEncontradoExcecao.class) //trata excecoes especificas da classe instanciada
    public ResponseEntity<StandardError> objectNotFound(ObjetoNaoEncontradoExcecao e, HttpServletRequest request){
        StandardError err = new StandardError(System.currentTimeMillis(),HttpStatus.NOT_FOUND.value(),"Não Encontrado",e.getMessage(),request.getRequestURI());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);
    }

    @ExceptionHandler(ExcecaoIntegridadeDados.class) //trata excecoes especificas da classe instanciada
    public ResponseEntity<StandardError> dataIntegrity(ExcecaoIntegridadeDados e, HttpServletRequest request){
        StandardError err = new StandardError(System.currentTimeMillis(),HttpStatus.BAD_REQUEST.value(),"Integridade de Dados",e.getMessage(),request.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class) //cria um erro customizavel em vez da mensagem padrao do Spring
    public ResponseEntity<StandardError> dataIntegrity(MethodArgumentNotValidException e, HttpServletRequest request){
        ValidationError err = new ValidationError(System.currentTimeMillis(),HttpStatus.UNPROCESSABLE_ENTITY.value(),"Erro de validação",e.getMessage(),request.getRequestURI());
        for(FieldError x: e.getBindingResult().getFieldErrors()){ //pega todos os erros acusado na excecao
            err.addError(x.getField(),x.getDefaultMessage());
        }
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(err);
    }

    @ExceptionHandler(AutorizacaoExcecao.class) //Codigo HTTP para acesso negado
    public ResponseEntity<StandardError> objectNotFound(AutorizacaoExcecao e, HttpServletRequest request){
        StandardError err = new StandardError(System.currentTimeMillis(),HttpStatus.FORBIDDEN.value(),"Acesso negado",e.getMessage(),request.getRequestURI());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(err);
    }

    @ExceptionHandler(FileException.class)
    public ResponseEntity<StandardError> file(FileException e, HttpServletRequest request) {

        StandardError err = new StandardError(System.currentTimeMillis(),HttpStatus.BAD_REQUEST.value(),"Erro de arquivo",e.getMessage(),request.getRequestURI());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
    }

    @ExceptionHandler(AmazonServiceException.class) //Excecao especifica do AWS
    public ResponseEntity<StandardError> amazonService(AmazonServiceException e, HttpServletRequest request) {

        HttpStatus code = HttpStatus.valueOf(e.getErrorCode());
        StandardError err = new StandardError(System.currentTimeMillis(),code.value(),"Erro Amazon Service",e.getMessage(),request.getRequestURI());
        return ResponseEntity.status(code).body(err);
    }

    @ExceptionHandler(AmazonClientException.class)
    public ResponseEntity<StandardError> amazonClient(AmazonClientException e, HttpServletRequest request) {

        StandardError err = new StandardError(System.currentTimeMillis(),HttpStatus.BAD_REQUEST.value(),"Erro Amazon Client",e.getMessage(),request.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
    }

    @ExceptionHandler(AmazonS3Exception.class)
    public ResponseEntity<StandardError> amazonS3(AmazonClientException e, HttpServletRequest request) {

        StandardError err = new StandardError(System.currentTimeMillis(),HttpStatus.BAD_REQUEST.value(),"Erro AmazonS3",e.getMessage(),request.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
    }
}
