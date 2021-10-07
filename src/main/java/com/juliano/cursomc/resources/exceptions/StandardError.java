package com.juliano.cursomc.resources.exceptions;

import java.io.Serializable;

//Classe para a padronizacao dos erros/excecao da aplicacao, traducao para o portugues
public class StandardError implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long timestamp;
    private Integer status;
    private String msg;
    private String error;
    private String path;

    public StandardError() {
    }

    public StandardError(Long timestamp, Integer status, String msg, String error, String path) {
        this.timestamp = timestamp;
        this.status = status;
        this.msg = msg;
        this.error = error;
        this.path = path;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
