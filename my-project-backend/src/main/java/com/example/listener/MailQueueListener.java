package com.example.listener;

import jakarta.annotation.Resource;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 邮件队列监听器
 * Created by 刘千山 on 2023/8/11/011-16:16
 */
@Component
@RabbitListener(queues = "mail")
public class MailQueueListener {

    @Resource
    JavaMailSender sender;

    @Value("${spring.mail.username}")
    String username;


    @RabbitHandler
    public void sendMailMessage(Map<String, Object> data) {
        String email = (String) data.get("email");
        Integer code = (Integer) data.get("code");
        String type = (String) data.get("type");
        SimpleMailMessage message = switch (type) {
            case "register" -> createMessage("欢迎注册我们的网站",
                    "您的邮件注册验证码为:" + code + ",有效时间为3分钟，请勿向他人泄露验证码。", email);
            case "reset" -> createMessage("您的密码重置邮件",
                    "您好，您正在进行密码重置操作，验证码:" + code + ",有效时间为3分钟，请勿向他人泄露验证码。", email);
            default -> null;
        };
        if (message == null) return;
        sender.send(message);
    }


    private SimpleMailMessage createMessage(String title, String content, String email) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setSubject(title);
        mailMessage.setText(content);
        mailMessage.setTo(email);
        mailMessage.setFrom(username);
        return mailMessage;
    }

}
