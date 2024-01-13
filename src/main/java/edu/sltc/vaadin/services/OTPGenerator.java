package edu.sltc.vaadin.services;

import edu.sltc.vaadin.data.OTPListener;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Nuyun-Kalamullage
 * @IDE IntelliJ IDEA
 * @date 13/01/2024
 * @package edu.sltc.vaadin.services
 * @project_Name File_Fortress_WebApp
 */


public class OTPGenerator {
    private static volatile OTPGenerator instance;
    private static String currentOTP;
    private static final CopyOnWriteArrayList<OTPListener> listeners = new CopyOnWriteArrayList<>();

    private OTPGenerator() {
        // Private constructor to prevent instantiation
        startTimer();
    }

    public static OTPGenerator getInstance() {
        if (instance == null) {
            synchronized (OTPGenerator.class) {
                if (instance == null) {
                    instance = new OTPGenerator();
                }
            }
        }
        return instance;
    }
    public void addListener(OTPListener listener) {
        listeners.add(listener);
    }

    public void removeListener(OTPListener listener) {
        listeners.remove(listener);
    }
    public String getOTP() {
        return currentOTP;
    }

    private static void generateOTP() {
        Random random = new Random();
        int otpValue = 10000 + random.nextInt(90000);
        currentOTP = String.valueOf(otpValue);
        notifyListeners();
    }

    private static void notifyListeners() {
        for (OTPListener listener : listeners) {
            listener.onOTPGenerated(currentOTP);
        }
    }
    private static void startTimer() {
        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                generateOTP();
//                System.out.println("New OTP generated: " + currentOTP);
            }
        }, 0, 10000); // 10000 milliseconds = 10 seconds
    }
}

