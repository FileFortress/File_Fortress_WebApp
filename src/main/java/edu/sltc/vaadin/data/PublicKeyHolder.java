package edu.sltc.vaadin.data;

import java.util.HashMap;

/**
 * @author Nuyun-Kalamullage
 * @IDE IntelliJ IDEA
 * @date 02/01/2024
 * @package edu.sltc.vaadin.data
 * @project_Name File_Fortress_WebApp
 */

public class PublicKeyHolder extends HashMap<String, String> {

    // Eagerly initialized singleton instance
    private static final PublicKeyHolder instance = new PublicKeyHolder();

    // Private constructor to prevent instantiation
    private PublicKeyHolder() {
        // Initialize if needed
    }

    // Method to get the singleton instance
    public static PublicKeyHolder getInstance() {
        return instance;
    }
}
