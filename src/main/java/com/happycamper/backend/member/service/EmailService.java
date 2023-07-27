package com.happycamper.backend.member.service;

import com.happycamper.backend.member.entity.Email;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {
    @Autowired
    private final JavaMailSender emailSender;
    @Value("${spring.mail.username}")
    String adminEmail;

    // 인증코드를 포함한 이메일 전송
    public String sendEmail(Email email) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(adminEmail);
        message.setTo(email.getUserEmail());
        message.setSubject(email.getTitle());
        message.setText(email.getContent());
        emailSender.send(message);

        return message.getText();
    }

    // 이메일 생성
    public Email createEmail(String userEmail, int randomCode) {
        String title = "Happy Camper 인증 코드";
        String content = String.valueOf(randomCode);

        Email email = new Email(userEmail, title, content);

        return email;
    }
}
