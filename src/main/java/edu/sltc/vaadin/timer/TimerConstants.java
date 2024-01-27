package edu.sltc.vaadin.timer;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

/**
 * @author Nuyun-Kalamullage
 * @IDE IntelliJ IDEA
 * @date 23/01/2024
 * @package edu.sltc.vaadin.timer
 * @project_Name File_Fortress_WebApp
 */
public class TimerConstants {

    public static SimpleTimer getRemainingTimerLayout(Optional<LocalDateTime> dateTime) {
        // Get the current date and time
        LocalDateTime currentDateTime = LocalDateTime.now();
        if (dateTime.isPresent()){
            // Calculate the difference between the current and target date and time
            long seconds = ChronoUnit.SECONDS.between(currentDateTime, dateTime.get());
            if (seconds <= 0){
                return new SimpleTimer(0);
            }
            return new SimpleTimer(seconds);
        }
        return new SimpleTimer(0);
    }
}
