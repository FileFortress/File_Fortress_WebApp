package edu.sltc.vaadin.views.admindashboard;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.theme.lumo.LumoUtility.Margin;
import edu.sltc.vaadin.views.MainLayout;
import jakarta.annotation.security.RolesAllowed;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


@PageTitle("Admin Dashboard")
@Route(value = "admin_dashboard", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
@RolesAllowed("ADMIN")
public class AdminDashboardView extends VerticalLayout {
    public AdminDashboardView() {
        setSpacing(false);

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
        Div timerLayout = createTimerLayout();
        add(timerLayout);

        Button resetTimeButton = new Button("Reset Time", event -> updateRemainingTime(timerLayout));
        resetTimeButton.getElement().setAttribute("theme", "primary");
        moduleDetails.add(resetTimeButton);
        add(new Paragraph("It’s a place where you can grow your own UI 🤗"));

        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        getStyle().set("text-align", "center");
    }
    private Div createTimerLayout() {
        Div layout = new Div();
        layout.getStyle().set("font-size", "24px");
        layout.getStyle().set("color", "#333");
        layout.getStyle().set("margin-top", "100px");
        updateRemainingTime(layout);
        return layout;
    }

    private void updateRemainingTime(Div layout) {
        String[] remainingTimeParts = getRemainingTime().split(" ");
        List<String> remainingTimeList = Arrays.asList(remainingTimeParts);
        Div timeLayout = new Div();
        timeLayout.setWidthFull();
        H1 timeHeader = new H1(remainingTimeList.get(0) + " : " + remainingTimeList.get(1));
        layout.getStyle().set("font-size", "24px");
        layout.getStyle().set("color", "#333");
        layout.getStyle().set("margin-bottom", "20px");
        layout.removeAll();
        layout.add(timeHeader);
        layout.setVisible(true);
    }

    private String getRemainingTime() {
        // Calculate the remaining time and return it as a string
        // Define the target date and time
        LocalDateTime targetDateTime = LocalDateTime.of(2023, 10, 24, 2, 0);

        // Get the current date and time
        LocalDateTime currentDateTime = LocalDateTime.now();

        // Calculate the difference between the current and target date and time
        long days = ChronoUnit.DAYS.between(currentDateTime, targetDateTime);
        long hours = ChronoUnit.HOURS.between(currentDateTime, targetDateTime);
        long minutes = ChronoUnit.MINUTES.between(currentDateTime, targetDateTime);
        long seconds = ChronoUnit.SECONDS.between(currentDateTime, targetDateTime);

        // Return the remaining time as a string
        return hours + " " + seconds/60 ;
    }
}
