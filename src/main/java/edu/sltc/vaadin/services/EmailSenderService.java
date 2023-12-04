package edu.sltc.vaadin.services;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.List;

/**
 * @author Nuyun-Kalamullage
 * @IDE IntelliJ IDEA
 * @date 03/12/2023
 * @package edu.sltc.vaadin.services
 * @project_Name File_Fortress_WebApp
 */
@Service
public class EmailSenderService {


        @Autowired
        private JavaMailSender javaMailSender;

        public void sendEmail(String toEmail, String subject, String body) {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
            message.setSubject(subject);
            message.setText(body);
            javaMailSender.send(message);
            System.out.println("Mail Sending Successful!!");
        }

        public void sendBulkEmails(List<String> toEmails, String subject, List<String> body) {
            int i = 0;
            for (String toEmail : toEmails) {
                SimpleMailMessage message = new SimpleMailMessage();
                message.setTo(toEmail);
                message.setSubject(subject);
                message.setText(body.get(i++));
                javaMailSender.send(message);
            }
            System.out.println("Mails Sending Successful!!");
        }


}
