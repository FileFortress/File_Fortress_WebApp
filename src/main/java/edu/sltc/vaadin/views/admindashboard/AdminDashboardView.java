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
@RouteAlias(value = "", layout = MainLayout.class)
@RolesAllowed("ADMIN")
public class AdminDashboardView extends VerticalLayout {
    private Div timerLayout;
    private Timer timer;
    final TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            // Update the remaining time on the UI
//            getUI().ifPresent(ui -> ui.access(this::updateRemainingTime));
            UI.getCurrent().access(() -> updateRemainingTime());
            System.out.println("Hello");
        }
        private void updateRemainingTime() {
            String[] remainingTimeParts = getRemainingTime().split(" ");
            List<String> remainingTimeList = Arrays.asList(remainingTimeParts);
            H1 timeHeader = new H1(remainingTimeList.get(0) + " : " + remainingTimeList.get(1));
            if (timerLayout.getChildren().findAny().isPresent()){
                Component previousTimeHeader = timerLayout.getComponentAt(0);
                timerLayout.replace(previousTimeHeader, timeHeader);
            }else {
                timerLayout.add(timeHeader);
            }
            timerLayout.setVisible(true);
        }
    };
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
        H2 remainingTime = new H2("Remaining Time");
        add(remainingTime);
        timerLayout = createTimerLayout();
        add(timerLayout);
        // Schedule a timer to update the remaining time every second
        timer = new Timer();
//        timer.scheduleAtFixedRate(timerTask, 0, 2000); // Update every second
//        timer.schedule(timerTask, 5000);
//        Button resetTimeButton = new Button("Reset Time", event -> updateRemainingTime(timerLayout));
//        resetTimeButton.getElement().setAttribute("theme", "primary");
//        moduleDetails.add(resetTimeButton);
        add(new Paragraph("It’s a place where you can grow your own UI 🤗"));
//        addListener();
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
        updateRemainingTime(layout);
        return layout;
    }

    private void updateRemainingTime(Div layout) {
        String[] remainingTimeParts = getRemainingTime().split(" ");
        List<String> remainingTimeList = Arrays.asList(remainingTimeParts);
        H1 timeHeader = new H1(remainingTimeList.get(0) + " : " + remainingTimeList.get(1));
        if (layout.getChildren().findAny().isPresent()){
            Component previousTimeHeader = layout.getComponentAt(0);
            layout.replace(previousTimeHeader, timeHeader);
        }else {
            layout.add(timeHeader);
        }
        layout.setVisible(true);
    }

    private String getRemainingTime() {
        // Calculate the remaining time and return it as a string
        // Define the target date and time
        LocalDateTime targetDateTime = LocalDateTime.of(2023, 10, 24, 22, 0);

        // Get the current date and time
        LocalDateTime currentDateTime = LocalDateTime.now();

        // Calculate the difference between the current and target date and time
        long days = ChronoUnit.DAYS.between(currentDateTime, targetDateTime);
        long hours = ChronoUnit.HOURS.between(currentDateTime, targetDateTime);
        long minutes = ChronoUnit.MINUTES.between(currentDateTime, targetDateTime);
        long seconds = ChronoUnit.SECONDS.between(currentDateTime, targetDateTime);

        // Return the remaining time as a string
        return String.format("%02d",hours%24) + " " + String.format("%02d",seconds%60+1) ;
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        timer.cancel();
        super.onDetach(detachEvent);
    }
}
