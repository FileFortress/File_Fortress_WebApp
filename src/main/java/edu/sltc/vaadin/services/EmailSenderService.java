package edu.sltc.vaadin.services;

import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Nuyun-Kalamullage
 * @IDE IntelliJ IDEA
 * @date 03/12/2023
 * @package edu.sltc.vaadin.services
 * @project_Name File_Fortress_WebApp
 */
@Service(value = "-101")
public class EmailSenderService {
    private final JavaMailSender javaMailSender;
    private final ServletWebServerApplicationContext webServerAppCtxt;
    @Autowired
    public EmailSenderService(JavaMailSender javaMailSender, ServletWebServerApplicationContext webServerAppCtxt) {
        this.javaMailSender = javaMailSender;
        this.webServerAppCtxt = webServerAppCtxt;
    }

    public void sendEmail(String toEmail, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(body);
        javaMailSender.send(message);
        System.out.println("Mail Sending Successful!!");
    }

    public void sendBulkEmails(List<String> toEmails, String subject, Set<String> body) {
        AtomicInteger i = new AtomicInteger();
        ArrayList<String> passwordList = new ArrayList<>(body);

        // Set the number of threads based on your requirements
        int numberOfThreads = toEmails.size(); // Adjust as needed
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);

        for (String toEmail : toEmails) {
            executorService.submit(() -> {
                try {
                    SimpleMailMessage message = new SimpleMailMessage();
                    message.setTo(toEmail);
                    message.setSubject(subject);
                    String mailBody = "Hosted Url is " + "https://" + CurrentWifiHandler.getWlanIpAddress().get(CurrentWifiHandler.getWifiDescription()) + ":" + webServerAppCtxt.getWebServer().getPort() +
                            "\nUserName is " + toEmail +
                            "\nPassword is " + passwordList.get(i.getAndIncrement()) + " ";
                    message.setText(mailBody);
                    javaMailSender.send(message);
                    System.out.println(toEmail + " E-Mail Sending Successful!!");
                } catch (Exception e) {
                    System.err.println("Error sending email to " + toEmail + ": " + e.getMessage());
                }
            });
        }
        // Shutdown the executor and wait for all tasks to complete
        executorService.shutdown();
        try {
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("Mails Sending Successful!!");
    }
}
