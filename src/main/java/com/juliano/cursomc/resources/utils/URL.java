package com.juliano.cursomc.resources.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

public class URL {
    // Encode -> substituir espacos ou caracteres especiais no nome do objeto, e necessario qdo se trabalha com URI(URL)

    public static String decodeParam(String s){ //metodo para descodificar os parametros
        try {
            return URLDecoder.decode(s,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }

    public static List<Integer> decodeIntList(String s){
        String [] vet = s.split(","); //divide a string com base em um char definido -> ,
        List<Integer> list = new ArrayList<>();
        for(int i=0; i<vet.length; i++){
            list.add(Integer.parseInt(vet[i]));
        }
        return list;
        //return Arrays.asList(s.split(",")).stream().map(x -> Integer.parseInt()).collect(Collectors.toList()) -> esse comando equivale o todos os anterior
    }
}
