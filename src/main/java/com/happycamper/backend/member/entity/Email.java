package com.happycamper.backend.member.entity;

import lombok.Data;

@Data
public class Email {
    private int id;
    private String userEmail;   // 수신 이메일 주소
    private String title;   // 이메일 제목
    private String content; // 이메일 내용

    public Email(String userEmail, String title, String content) {
        this.userEmail = userEmail;
        this.title = title;
        this.content = content;
    }
}
