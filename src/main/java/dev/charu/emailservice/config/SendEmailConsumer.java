package dev.charu.emailservice.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.charu.emailservice.dto.SendEmailDto;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;


import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import java.util.Properties;
@Service
public class SendEmailConsumer {
    private ObjectMapper objectMapper;
    private EmailUtil emailUtil;

    public SendEmailConsumer(ObjectMapper objectMapper,
                             EmailUtil emailUtil) {
        this.objectMapper = objectMapper;
        this.emailUtil = emailUtil;
    }
    @KafkaListener(topics = "sendEmail", groupId = "emailService")
    public void handleSendEmailMessage(String message) {

        SendEmailDto sendEmailDto = null;
        try {
            sendEmailDto = objectMapper.readValue(message, SendEmailDto.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }


        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");


        Authenticator auth = new Authenticator() {

            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("putyourusername", "apppassword");
            }
        };
        Session session = Session.getInstance(props, auth);

        emailUtil.sendEmail(
                session,
                sendEmailDto.getTo(),
                sendEmailDto.getSubject(),
                sendEmailDto.getBody()
        );
    }


}
