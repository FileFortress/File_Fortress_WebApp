package edu.sltc.vaadin.services;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * @author Nuyun-Kalamullage
 * @IDE IntelliJ IDEA
 * @date 22/11/2023
 * @package edu.sltc.vaadin.services
 * @project_Name File_Fortress_WebApp
 */

public class EmailExtractor {
    public static List<String> emails = new ArrayList<>();
    public static List<String> extractStudentsEmails(String filePath) {
        emails = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            Pattern emailPattern = Pattern.compile("\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}\\b");

            while ((line = reader.readLine()) != null) {
                Matcher matcher = emailPattern.matcher(line);
                while (matcher.find()) {
                    if (checkStudentEmail(matcher.group()))
                        emails.add(matcher.group());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return emails;
    }

    public static List<String> extractEmails(String filePath) {
        emails = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            Pattern emailPattern = Pattern.compile("\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}\\b");

            while ((line = reader.readLine()) != null) {
                Matcher matcher = emailPattern.matcher(line);
                while (matcher.find()) {
                    emails.add(matcher.group());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return emails;
    }
    private static boolean checkStudentEmail(String email){
        return email.endsWith("@sltc.ac.lk");
    }
}
