package com.juliano.cursomc.services;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.juliano.cursomc.services.exceptions.FileException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

@Service
public class AmazonS3Service {

    private Logger LOG = LoggerFactory.getLogger(AmazonS3Service.class);

    @Autowired
    private AmazonS3 s3client;

    @Value(("${s3.bucket}"))
    private String bucketName;

    public URI uploadFile(MultipartFile multipartFile) { //URL para o endpoint fazer upload
        try {
            String fileName = multipartFile.getOriginalFilename();
            InputStream is = multipartFile.getInputStream();
            String contentType = multipartFile.getContentType(); //tipo de arquivo a ser enviado
            return uploadFile(is, fileName, contentType);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro IO: "+e.getMessage());
        }
    }

    public URI uploadFile(InputStream is, String fileName, String contentType) {
        try {
            ObjectMetadata meta = new ObjectMetadata();
            LOG.info("Iniciando upload");
            s3client.putObject(bucketName, fileName, is, meta);
            LOG.info("Upload finalizado");
            return s3client.getUrl(bucketName, fileName).toURI();
        } catch (URISyntaxException e) {
            e.printStackTrace();
            throw new FileException("Erro ao converter URL para URI");
        }
    }
}
