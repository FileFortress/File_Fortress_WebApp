package edu.sltc.vaadin.services;

import edu.sltc.vaadin.data.ConnectedStudentsListener;
import edu.sltc.vaadin.data.OTPListener;
import edu.sltc.vaadin.models.ExamModel;

import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Nuyun-Kalamullage
 * @IDE IntelliJ IDEA
 * @date 25/01/2024
 * @package edu.sltc.vaadin.services
 * @project_Name File_Fortress_WebApp
 */
public class CheckConnectedStudent {
    private static CheckConnectedStudent instance;
    private static volatile Set<String> connectedEmails = new HashSet<>();
    private static volatile int studentCount = connectedEmails.size();
    private static final CopyOnWriteArrayList<ConnectedStudentsListener> checkConnectedStudentslisteners = new CopyOnWriteArrayList<>();
    private static Timer timer;

    private CheckConnectedStudent() {
    }
    public void StartTimerThread(){
        if(timer == null)
            startTimer();
    }
    public void StopTimerThread(){
        if(timer != null)
            timer = null;
    }
    public static CheckConnectedStudent getInstance() {
        if (instance == null) {
            synchronized (CheckConnectedStudent.class) {
                if (instance == null) {
                    instance = new CheckConnectedStudent();
                }
            }
        }
        return instance;
    }
    public void addListener(ConnectedStudentsListener listener) {
        checkConnectedStudentslisteners.add(listener);
    }

    public void removeListener(ConnectedStudentsListener listener) {
        checkConnectedStudentslisteners.remove(listener);
    }
    public void addStudentEmail(String email){
        connectedEmails.add(email);
    }
    public void removeStudentEmail(String email){
        connectedEmails.remove(email);

    }

    public static String getStudentCount() {
        if (ExamModel.serverIsRunning)
            return String.format("%02d", studentCount);
        else
            return "00";
    }

    private static void notifyListeners() {
        for (ConnectedStudentsListener listener : checkConnectedStudentslisteners) {
            listener.onStudentCountChanged(String.format("%02d", studentCount));
        }
    }
    private static void startTimer() {
        timer = new Timer(true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                checkConnectedStudents();
            }
        }, 0, 3000); // 3000 milliseconds = 03 seconds
    }

    private static void checkConnectedStudents() {
        if (connectedEmails.size() != studentCount){
            studentCount = connectedEmails.size();
            notifyListeners();
        }
    }
}
