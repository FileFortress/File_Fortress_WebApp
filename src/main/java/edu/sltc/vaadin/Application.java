package edu.sltc.vaadin;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import edu.sltc.vaadin.models.PasswordPool;
import edu.sltc.vaadin.services.EmailExtractor;
import edu.sltc.vaadin.services.EmailSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.event.EventListener;
import org.springframework.security.provisioning.UserDetailsManager;

/**
 * The entry point of the Spring Boot application.
 * Use the @PWA annotation make the application installable on phones, tablets
 * and some desktop browsers.
 *
 */

@SpringBootApplication(exclude = ErrorMvcAutoConfiguration.class)
@Theme(value = "my-app", variant = Lumo.DARK)
@PropertySource("classpath:application.properties")
public class Application implements AppShellConfigurator {
    private final EmailSenderService senderService;
    private final UserDetailsManager userDetailsManager;
    @Autowired
    public Application(EmailSenderService senderService, UserDetailsManager userDetailsManager) {
        this.senderService = senderService;
        this.userDetailsManager = userDetailsManager;
    }

    public static void main(String[] args) {
      SpringApplication.run(Application.class, args);
//        SpringApplication application = new SpringApplication(Application.class);
//        application.setAdditionalProfiles("ssl");
//        application.run(args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void sendEmailsToAdmins(){
        senderService.sendBulkEmails(userDetailsManager, EmailExtractor.extractEmails("./admin_emails.txt"), "FileFortress Admin Mail Service");
    }
}
