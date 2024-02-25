package edu.sltc.vaadin.services;

import edu.sltc.vaadin.data.PasswordGenerator;
import edu.sltc.vaadin.models.PasswordPool;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
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

    public void sendBulkEmails(UserDetailsManager userDetailsManager, List<String> toEmails, String subject) {
        AtomicInteger i = new AtomicInteger();
        // Set the number of threads based on your requirements
        int numberOfThreads = toEmails.size(); // Adjust as needed
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        PasswordPool.getInstance().setAdminPasswords(PasswordGenerator.bulkPasswordForExaminers(numberOfThreads));
        ArrayList<String> passwordList = new ArrayList<>(PasswordPool.getInstance().getAdminPasswords());

        for (String toEmail : toEmails) {

            executorService.submit(() -> {
                int index = i.getAndIncrement();
                if (userDetailsManager.userExists(toEmail)){
                    userDetailsManager.deleteUser(toEmail);
                    System.out.println("Exist User Removed");
                }
                userDetailsManager.createUser(User.withUsername(toEmail)
                        .password(new BCryptPasswordEncoder().encode(passwordList.get(index)))
                        .roles("ADMIN")
                        .build());
                try {
                    SimpleMailMessage message = new SimpleMailMessage();
                    message.setTo(toEmail);
                    message.setSubject(subject);

                    String mailBody = "Hosted Url is " + "https://" + CurrentWifiHandler.getWlanIpAddress().get(CurrentWifiHandler.getWifiDescription()) + ":" + webServerAppCtxt.getWebServer().getPort() +
                            "\nUserName is " + toEmail +
                            "\nPassword is " + passwordList.get(index) + " \n\n"+
                            "Best regards,\n" +
                            "File Fortress Team";
//                    String mailBody = "## Application Details\n" +
//                            "- **Name:** File Fortress Web App\n" +
//                            "- **Logo:** [YourLogo](https://nuyun-kalamullage.github.io/File_Fortress_WebApp/src/main/resources/META-INF/resources/images/logo_placeholder.png)\n\n" +
//                            "## User Credentials\n" +
//                            "- **UserName:** " + toEmail +
//                            "- **Password:** " + passwordList.get(i.getAndIncrement()) + "\n\n" +
//                            "## Hosted URL\n" +
//                            "[Hosted URL](https://" + CurrentWifiHandler.getWlanIpAddress().get(CurrentWifiHandler.getWifiDescription()) + ":" + webServerAppCtxt.getWebServer().getPort() + ")\n\n" +
//                            "Best regards,\n" +
//                            "File Fortress Team";
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
