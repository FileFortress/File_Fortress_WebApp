package edu.sltc.vaadin.views.admindashboard;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.crud.Crud;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.theme.lumo.LumoUtility.Margin;
import edu.sltc.vaadin.timer.SimpleTimer;
import edu.sltc.vaadin.views.MainLayout;
import jakarta.annotation.security.RolesAllowed;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.TimerTask;
import java.util.Timer;


@PageTitle("Admin Dashboard")
@Route(value = "admin_dashboard", layout = MainLayout.class)
@RolesAllowed("ADMIN")
public class AdminDashboardView extends VerticalLayout {
    private Div timerLayout;
    private Timer timer;
    public AdminDashboardView() {
        setSpacing(false);
        H2 remainingTime = new H2("Remaining Time");
        add(remainingTime);
        add(createTimerLayout());

        Div moduleDetails = new Div();
        moduleDetails.setMaxWidth("800px");
        moduleDetails.addClassNames(Margin.Top.SMALL, Margin.Bottom.LARGE);
        add(moduleDetails);

        FormLayout formLayout = new FormLayout();
        formLayout.setMaxWidth("600px");
        moduleDetails.add(formLayout);

        TextField WifiTextField = new TextField("Connected Wifi Network");
        WifiTextField.setValue("Hacker's Fiber");
        WifiTextField.setReadOnly(true);
        formLayout.add(WifiTextField);

        TextField ServerUrlTextField = new TextField("Server URL");
        ServerUrlTextField.setValue("192.168.8.1:8080");
        ServerUrlTextField.setReadOnly(true);
        formLayout.add(ServerUrlTextField);

        TextField JoinedStudentsTextField = new TextField("Joined Students");
        JoinedStudentsTextField.setValue("25");
        JoinedStudentsTextField.setReadOnly(true);
        formLayout.add(JoinedStudentsTextField);

        TextField submissionCountTextField = new TextField("Answer Submission Count");
        submissionCountTextField.setValue("10");
        submissionCountTextField.setReadOnly(true);
        formLayout.add(submissionCountTextField);

        formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1),
                new FormLayout.ResponsiveStep("350px", 2));


        add(new Paragraph("It’s a place where you can grow your own UI 🤗"));
        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        getStyle().set("text-align", "center");
    }
    private Div createTimerLayout() {
        Div layout = new Div();
        layout.getStyle().set("font-size", "30px");
        layout.getStyle().set("color", "#333");
        layout.getStyle().set("margin-top", "10px");
        layout.getStyle().set("margin-bottom", "20px");
        layout.getStyle().set("padding-left", "55px");
        layout.getStyle().set("padding-right", "55px");
        layout.getStyle().set("padding-top", "25px");
        layout.getStyle().set("padding-bottom", "25px");
        layout.getStyle().set("border", "5px solid white");
        layout.getStyle().set("border-radius", "25px");
        SimpleTimer timer = getRemainingTimerLayout();
        timer.getStyle().setColor("white");
        timer.setFractions(false);
        timer.setHours(true);
        timer.setMinutes(true);
        timer.setCountUp(false);
        timer.start();
        layout.add(timer);
        return layout;
    }

    private SimpleTimer getRemainingTimerLayout() {
        // Calculate the remaining time and return it as a string
//        // Define the target date and time
//        LocalDateTime targetDateTime = LocalDateTime.of(2023, 10, 31, 23, 30);

        // Get the current date and time
        LocalDateTime currentDateTime = LocalDateTime.now();

        // Define the target date and time
        // Add 3 hours to the current time
        LocalDateTime targetDateTime = currentDateTime.plusHours(3);

        // Calculate the difference between the current and target date and time
        long days = ChronoUnit.DAYS.between(currentDateTime, targetDateTime);
        long hours = ChronoUnit.HOURS.between(currentDateTime, targetDateTime);
        long minutes = ChronoUnit.MINUTES.between(currentDateTime, targetDateTime);
        long seconds = ChronoUnit.SECONDS.between(currentDateTime, targetDateTime);

        // Return the remaining time as a string
//        return String.format("%02d",hours%24) + " " + String.format("%02d",seconds%60+1) ;
        return new SimpleTimer(seconds);
    }

}
