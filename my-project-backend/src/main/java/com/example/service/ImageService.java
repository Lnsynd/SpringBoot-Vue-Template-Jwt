package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.minio.errors.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.OutputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by 刘千山 on 2023/12/7/007-22:14
 */
public interface ImageService {
    String uploadAvatar(MultipartFile file,int id) throws IOException;
    void fetchImageFromMinio(OutputStream outputStream,String imagePath) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException;
}
