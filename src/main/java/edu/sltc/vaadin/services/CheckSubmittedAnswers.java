package edu.sltc.vaadin.services;

import edu.sltc.vaadin.data.ConnectedStudentsListener;
import edu.sltc.vaadin.data.StudentsAnswerSubmissionListener;

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
public class CheckSubmittedAnswers {
    private static CheckSubmittedAnswers instance;
    private static volatile Set<String> answerSubmittedEmails = new HashSet<>();
    private static volatile int answerCount = answerSubmittedEmails.size();
    private static final CopyOnWriteArrayList<StudentsAnswerSubmissionListener> checkSubmittedAnswerslisteners = new CopyOnWriteArrayList<>();
    private static Timer timer;

    private CheckSubmittedAnswers() {
    }
    public void StartTimerThread(){
        if(timer == null)
            startTimer();
    }
    public void StopTimerThread(){
        if(timer != null)
            timer = null;
    }
    public static CheckSubmittedAnswers getInstance() {
        if (instance == null) {
            synchronized (CheckSubmittedAnswers.class) {
                if (instance == null) {
                    instance = new CheckSubmittedAnswers();
                }
            }
        }
        return instance;
    }
    public void addListener(StudentsAnswerSubmissionListener listener) {
        checkSubmittedAnswerslisteners.add(listener);
    }

    public void removeListener(StudentsAnswerSubmissionListener listener) {
        checkSubmittedAnswerslisteners.remove(listener);
    }
    public void addStudentEmail(String email){
        answerSubmittedEmails.add(email);
    }
    public static String getAnswerCount() {
        return String.format("%02d", answerCount);
    }

    private static void notifyListeners() {
        for (StudentsAnswerSubmissionListener listener : checkSubmittedAnswerslisteners) {
            listener.onAnswerCountChanged(String.format("%02d", answerCount));
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
        if (answerSubmittedEmails.size() != answerCount){
            answerCount = answerSubmittedEmails.size();
            notifyListeners();
        }
    }
}
