package edu.sltc.vaadin.models;

import java.util.Set;

/**
 * @author Nuyun-Kalamullage
 * @IDE IntelliJ IDEA
 * @date 06/12/2023
 * @package edu.sltc.vaadin.models
 * @project_Name File_Fortress_WebApp
 */
public class PasswordPool {
    private static PasswordPool instance;
    private Set<String> adminEmails, studentEmails;
    private Set<String> adminPasswords, studentPasswords;
    private PasswordPool() {
    }
    public static PasswordPool getInstance(){
        if (instance == null){
            synchronized (PasswordPool.class){
                if (instance == null){
                    instance = new PasswordPool();
                }
            }
        }
        return instance;
    }

    public Set<String> getAdminPasswords() {
        return adminPasswords;
    }

    public void setAdminPasswords(Set<String> adminPasswords) {
        this.adminPasswords = adminPasswords;
    }

    public Set<String> getStudentPasswords() {
        return studentPasswords;
    }

    public void setStudentPasswords(Set<String> studentPasswords) {
        this.studentPasswords = studentPasswords;
    }

    public Set<String> getAdminEmails() {
        return adminEmails;
    }

    public void setAdminEmails(Set<String> adminEmails) {
        this.adminEmails = adminEmails;
    }

    public Set<String> getStudentEmails() {
        return studentEmails;
    }

    public void setStudentEmails(Set<String> studentEmails) {
        this.studentEmails = studentEmails;
    }

    public static void clearPasswordPool(){
        instance = null;
        System.gc();
    }
}
