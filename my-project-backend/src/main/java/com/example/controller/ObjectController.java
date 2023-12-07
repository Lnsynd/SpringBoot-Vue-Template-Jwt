package com.example.controller;

import com.example.entity.RestBean;
import com.example.service.ImageService;
import io.minio.errors.*;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by 刘千山 on 2023/12/8/008-00:20
 */
@Slf4j
@RestController
public class ObjectController {

    @Resource
    ImageService imageService;

    @GetMapping("/images/avatar/**")
    public void imageFetch(HttpServletRequest request, HttpServletResponse response) throws Exception {
        this.fetchImage(request,response);
    }

    /**
     * 获取头像
     */
    private void fetchImage(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String imagePath = request.getServletPath().substring(7);
        ServletOutputStream outputStream = response.getOutputStream();
        if (imagePath.length() <= 13) {
            outputStream.println(RestBean.failure(404, "Not Found").toString());
        } else {
            try {
                imageService.fetchImageFromMinio(outputStream, imagePath);
                response.setHeader("Cache-Control","max-age=2592000");
            } catch (ErrorResponseException e) {
                if (e.response().code() == 404) {
                    response.setStatus(404);
                    outputStream.println(RestBean.failure(404, "Not Found").toString());
                } else {
                    log.error("从Minio获取图片异常:" + e.getMessage(), e);
                }
            }
        }
    }

}
