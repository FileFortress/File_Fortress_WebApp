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
        // Calculate the remaining time and return it as a string
        // Get the current date and time
        LocalDateTime currentDateTime = LocalDateTime.now();

        if (dateTime.isPresent()){
//            System.out.println(dateTime.get());
            // Calculate the difference between the current and target date and time
            long seconds = ChronoUnit.SECONDS.between(currentDateTime, dateTime.get());
//            System.out.println("seconds : "+seconds);
            if (seconds <= 0){
                Notification notification = new Notification("     Exam Time is Over!!    ", 2500, Notification.Position.TOP_CENTER);
                notification.addThemeVariants(NotificationVariant.LUMO_CONTRAST);
                notification.open();
                return new SimpleTimer(0);
            }
            return new SimpleTimer(seconds);
        }
        return new SimpleTimer(0);
        // Return the remaining time as a string
//        return String.format("%02d",hours%24) + " " + String.format("%02d",seconds%60+1) ;
    }
}
