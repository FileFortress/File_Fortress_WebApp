package edu.sltc.vaadin.data;

import edu.sltc.vaadin.services.EmailExtractor;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;

import java.util.List;

/**
 * @author Nuyun-Kalamullage
 * @IDE IntelliJ IDEA
 * @date 21/11/2023
 * @package edu.sltc.vaadin.data
 * @project_Name File_Fortress_WebApp
 */
public class User {
    private static User instance;

    private String name;
    private String givenName;
    private String familyName;
    private String email;
    private String pictureUrl;
    private String accessToken; // New field for storing access token
    private String userRole; // New field for storing user role

    // Private constructor to prevent instantiation from outside
    private User() {
    }

    // Method to get the singleton instance
    public static User getInstance() {
        if (instance == null) {
            instance = new User();
        }
        return instance;
    }

    // Method to initialize user details using OAuth2AuthenticatedPrincipal
    public void init(OAuth2AuthenticatedPrincipal principal) {
        instance.name = principal.getAttribute("name");
        instance.givenName = principal.getAttribute("given_name");
        instance.familyName = principal.getAttribute("family_name");
        instance.email = principal.getAttribute("email");
        instance.pictureUrl = principal.getAttribute("picture");
        instance.accessToken = principal.getAttribute("at_hash");
    }

    //Method for set user role
    private void findUserRole() {
        List<String> emails = EmailExtractor.extractEmails("./admin_emails.txt");
        if (!emails.isEmpty()) {
            if (emails.contains(instance.getEmail())){
                instance.setUserRole("ROLE_ADMIN");
            } else {
                System.out.println(instance.getEmail());
                instance.setUserRole(EmailExtractor.getRoleForEmail(instance.getEmail()));
            }
        } else {
            System.out.println("No email addresses found in the document.");
        }
    }

    // Method to clear user details and reset the instance
    public void clearUserDetails() {
        instance.name = null;
        instance.givenName = null;
        instance.familyName = null;
        instance.email = null;
        instance.pictureUrl = null;
        instance.accessToken = null;
        instance.userRole = null;
        instance = null;
    }

    // Getters and Setters (you can generate these automatically in most IDEs)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getUserRole() {
        findUserRole();
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }
}
