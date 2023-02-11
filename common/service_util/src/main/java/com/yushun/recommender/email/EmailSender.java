package com.yushun.recommender.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class EmailSender {
    @Value("${spring.mail.username}")
    private static String adminEmail;

    @Autowired
    private JavaMailSender mailSender;

    public boolean sendMail(String email, String code) {
        if(email == null) {
            return false;
        }

        String subject = "Recommender System Verification Code";

        SimpleMailMessage msg = new SimpleMailMessage();

        msg.setFrom(adminEmail);
        msg.setTo(email);
        msg.setSubject(subject);
        msg.setText(code);

        try {
            mailSender.send(msg);

            return true;
        }catch (Exception e) {
            System.out.println(e);
            return false;
        }
    }
}
