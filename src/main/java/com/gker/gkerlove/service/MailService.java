package com.gker.gkerlove.service;

import com.gker.gkerlove.bean.MailRequest;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Date;

@Service
public class MailService {

    private static final Logger logger = LoggerFactory.getLogger(MailService.class);
    //注入邮件工具类
    @Resource
    private JavaMailSender javaMailSender;
    @Value("${spring.mail.nickname}")
    private String nickname;
    @Value("${spring.mail.username}")
    private String username;

    public void checkMail(MailRequest mailRequest) {
        Assert.notNull(mailRequest, "邮件请求不能为空");
        Assert.notNull(mailRequest.getSendTo(), "邮件收件人不能为空");
        Assert.notNull(mailRequest.getText(), "邮件内容不能为空");
    }

    public void sendSimpleMail(MailRequest mailRequest) {
        SimpleMailMessage message = new SimpleMailMessage();
        checkMail(mailRequest);
        message.setFrom(nickname + '<' + username + '>');
        message.setTo(mailRequest.getSendTo().split(","));
        message.setSubject(mailRequest.getSubject());
        message.setText(mailRequest.getText());
        message.setSentDate(new Date());
        javaMailSender.send(message);
        logger.info("发送邮件成功: {}->{}", nickname, mailRequest.getSendTo());
    }
}