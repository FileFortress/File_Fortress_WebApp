package edu.sltc.vaadin.data;

import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;

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
        this.name = principal.getAttribute("name");
        this.givenName = principal.getAttribute("given_name");
        this.familyName = principal.getAttribute("family_name");
        this.email = principal.getAttribute("email");
        this.pictureUrl = principal.getAttribute("picture");
        this.accessToken = principal.getAttribute("at_hash");
    }

    // Method to clear user details and reset the instance
    public void clearUserDetails() {
        this.name = null;
        this.givenName = null;
        this.familyName = null;
        this.email = null;
        this.pictureUrl = null;
        this.accessToken = null;
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
}
