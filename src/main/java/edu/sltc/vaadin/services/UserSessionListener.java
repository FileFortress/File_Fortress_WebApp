package edu.sltc.vaadin.services;

import com.vaadin.flow.server.*;

/**
 * @author Nuyun-Kalamullage
 * @IDE IntelliJ IDEA
 * @date 25/01/2024
 * @package edu.sltc.vaadin.services
 * @project_Name File_Fortress_WebApp
 */
import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
@Component
public class UserSessionListener implements VaadinServiceInitListener {
    private static Set<String> connectedEmails = new HashSet<>();
    @Override
    public void serviceInit(ServiceInitEvent event) {
        event.getSource().addSessionInitListener(sessionInitEvent -> {
            VaadinSession session = sessionInitEvent.getSession();
            if (getUserEmailFromSecurityContext().isPresent()){
                String userEmail = getUserEmailFromSecurityContext().get();
                // Add the user's email to the set of connected emails
                connectedEmails.add(userEmail);
                System.out.println(connectedEmails);
                session.getService().addSessionDestroyListener(sessionDestroyEvent -> {
                    // Remove the user's email from the set when the session is destroyed
                    connectedEmails.remove(userEmail);
                });
            } else{
                System.out.println("Else Part Executed");
            }


        });
    }

    private Optional<String> getUserEmailFromSecurityContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            // Adjust this based on how your UserDetails contain the user's email and role
            Object principal = authentication.getPrincipal();
            if (principal instanceof org.springframework.security.core.userdetails.User userDetails) {
                // Check if the user has the role ROLE_USER
                if (userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_USER"))) {
                    // If the user has the role ROLE_USER, return the email
                    return Optional.ofNullable(userDetails.getUsername());
                }
            }
        }
        return Optional.empty();
    }

    public static Set<String> getConnectedEmails() {
        return connectedEmails;
    }
}

