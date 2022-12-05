package com.bug_tracker.email;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailServices {
    private final JavaMailSender javaMailSender;
    private final String frontendUrl;

    public MailServices(JavaMailSender javaMailSender,
                        @Value("${application.frontend.default-url}")  String frontendUrl) {
        this.javaMailSender = javaMailSender;
        this.frontendUrl = frontendUrl;
    }

    public void sendForgotMassage(String email, String token, String baseURL){
        String url = baseURL != null ? baseURL : frontendUrl;

        SimpleMailMessage massage = new SimpleMailMessage();
        massage.setFrom("bugtracker@mail.com");
        massage.setTo(email);
        massage.setSubject("Reset your password");
        massage.setText(String.
                format("click <a href=\"%s/reset/%s\">here</a> to reset your password", url,token));

        //javaMailSender.send(massage);

    }
}
