package com.gker.gkerlove.bean;

import lombok.Data;

import java.io.Serializable;

@Data
public class MailRequest implements Serializable {
    private String sendTo; // 接收人
    private String subject = "果壳之恋"; // 主题
    private String text; // 内容
}