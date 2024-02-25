package edu.sltc.vaadin.data;

import java.security.SecureRandom;
import java.util.HashSet;
import java.util.Set;
/**
 * @author Nuyun-Kalamullage
 * @IDE IntelliJ IDEA
 * @date 06/12/2023
 * @package edu.sltc.vaadin.data
 * @project_Name File_Fortress_WebApp
 */

public class PasswordGenerator {

    private static String generateRandomPassword(int passwordLength ) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()-_=+";
        SecureRandom secureRandom = new SecureRandom();
        StringBuilder password = new StringBuilder();
        for (int i = 0; i < passwordLength; i++) {
            int randomIndex = secureRandom.nextInt(characters.length());
            password.append(characters.charAt(randomIndex));
        }
        return password.toString();
    }
    public static Set<String> bulkPasswordForStudents(int numberOfStudents, int passwordLength ){
        Set<String> generatedPasswords = new HashSet<>();
        while (generatedPasswords.size() < numberOfStudents) {
            String randomPassword = generateRandomPassword(passwordLength);
            generatedPasswords.add(randomPassword);
        }
        System.gc();
        return generatedPasswords;
    }
    public static Set<String> bulkPasswordForExaminers(int numberOfExaminers){
        Set<String> generatedPasswords = new HashSet<>();
        while (generatedPasswords.size() < numberOfExaminers) {
            String randomPassword = generateRandomPassword(16);
            generatedPasswords.add(randomPassword);
        }
        return generatedPasswords;
    }
    public static void main(String[] args) {
        int numberOfUsers = 50;

        Set<String> generatedPasswords = new HashSet<>();

        while (generatedPasswords.size() < numberOfUsers) {
            String randomPassword = generateRandomPassword(12);
            generatedPasswords.add(randomPassword);
        }
        System.out.println(generatedPasswords);
    }
}

