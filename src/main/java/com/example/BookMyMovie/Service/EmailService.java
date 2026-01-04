package com.example.BookMyMovie.Service;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    Logger logger= LoggerFactory.getLogger(EmailService.class);

    public void sendMail(String to,String subject,String body)
    {
        try {
            logger.info("Email Service Started");
            SimpleMailMessage mail = new SimpleMailMessage();
            mail.setTo(to);
            mail.setSubject(subject);
            mail.setText(body);
            javaMailSender.send(mail);
            logger.info("Email sent");
        } catch (Exception e) {
            logger.info("Email service failed due to {}",e.getMessage());
            log.info("Unable to send mail due to : {}",e.getMessage());
        }
    }
}
