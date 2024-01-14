package edu.sltc.vaadin.views.admindashboard;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinRequest;
import com.vaadin.flow.shared.communication.PushMode;
import com.vaadin.flow.theme.lumo.LumoUtility.Margin;
import edu.sltc.vaadin.data.OTPListener;
import edu.sltc.vaadin.data.WifiListener;
import edu.sltc.vaadin.services.CurrentWifiHandler;
import edu.sltc.vaadin.services.OTPGenerator;
import edu.sltc.vaadin.timer.SimpleTimer;
import edu.sltc.vaadin.views.MainLayout;
import jakarta.annotation.security.RolesAllowed;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Timer;
import java.util.TimerTask;


@PageTitle("Admin Dashboard")
@Route(value = "admin_dashboard", layout = MainLayout.class)
@RolesAllowed("ADMIN")
@JsModule("./adminDashboard.js")
@CssImport("./styles/admin-dashboard.css")
public class AdminDashboardView extends VerticalLayout {
    private Div timerLayout;
    private TextField WifiTextField, ServerUrlTextField, JoinedStudentsTextField, submissionCountTextField;
    private UI ui;
    private Timer timer, wifiTimer, otpTimer;
    private OTPListener otpListener;
    private WifiListener wifiListener;
    public AdminDashboardView() {
        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        ui = UI.getCurrent();
        Div firstModule = new Div();
        firstModule.setMaxWidth("800px");
        firstModule.addClassNames(Margin.Top.MEDIUM, Margin.Bottom.MEDIUM);
        add(firstModule);
        FormLayout formLayoutOne = new FormLayout();
        formLayoutOne.getElement().setAttribute("id", "my-top-form-layout");
        firstModule.add(formLayoutOne);
        Div timer1 = createTimerLayout();
        formLayoutOne.add(timer1);
        formLayoutOne.setColspan(timer1, 1);
        Div otp1 = otpViewer();
        formLayoutOne.add(otp1);
        formLayoutOne.setColspan(otp1, 1);
        formLayoutOne.setResponsiveSteps(new FormLayout.ResponsiveStep("200", 1),
                new FormLayout.ResponsiveStep("400px", 4));
        setSpacing(false);
        otp1.getComponentAt(1).getElement().setText(OTPGenerator.getInstance().getOTP());
        otpListener = (otpValue)->{
            ui.access(()->{
                otp1.getComponentAt(1).getElement().getStyle().setColor("white");
                otp1.getComponentAt(1).getElement().setText(otpValue);
                // Push an empty update to trigger a background refresh
                ui.push();
            });
            // Execute below method to change color after 7 seconds
            new Timer(true).schedule(new TimerTask() {
                @Override
                public void run() {ui.access(()->{
                    otp1.getComponentAt(1).getElement().getStyle().setColor("red");
                    // Push an empty update to trigger a background refresh
                    ui.push();
                });}
            },7000);
        };
        OTPGenerator.getInstance().addListener(otpListener);
        ui.getPage().executeJs("setContentView()");
        // -------------------------------------------------------------------------------------------------------------
        Div moduleDetails = new Div();
//        moduleDetails.setMaxWidth("800px");
        moduleDetails.addClassNames(Margin.Top.SMALL, Margin.Bottom.LARGE);
        add(moduleDetails);

        FormLayout formLayout = new FormLayout();
        formLayout.setMaxWidth("800px");
        moduleDetails.add(formLayout);

        WifiTextField = new TextField("Connected Wifi Network");
        WifiTextField.setReadOnly(true);
        formLayout.add(WifiTextField);

        ServerUrlTextField = new TextField("Server URL");
        ServerUrlTextField.setReadOnly(true);
        formLayout.add(ServerUrlTextField);

        JoinedStudentsTextField = new TextField("Joined Students");
        JoinedStudentsTextField.setReadOnly(true);
        formLayout.add(JoinedStudentsTextField);

        submissionCountTextField = new TextField("Answer Submission Count");
        submissionCountTextField.setReadOnly(true);
        formLayout.add(submissionCountTextField);

        formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("200", 1),
                new FormLayout.ResponsiveStep("400px", 2));

        add(new Paragraph("It’s a place where you can grow your own UI 🤗"));
        getStyle().set("text-align", "center");

        WifiTextField.setValue(CurrentWifiHandler.getWifiSSID());
        ServerUrlTextField.setValue("https://" + CurrentWifiHandler.getWlanIpAddress().get(CurrentWifiHandler.getWifiDescription()) + ":4444" );
        JoinedStudentsTextField.setValue("25");
        submissionCountTextField.setValue("10");
        wifiListener = (wifiSSID)->{
            ui.access(() ->{
                WifiTextField.setValue(wifiSSID);
                ServerUrlTextField.setValue("https://" + CurrentWifiHandler.getWlanIpAddress().get(CurrentWifiHandler.getWifiDescription()) + ":4444" );
                // Push an empty update to trigger a background refresh
                ui.push();
            });
        };
        CurrentWifiHandler.getInstance().setListeners(wifiListener);
    }
    private Div firstDivFlexbox(){
        Div layout = new Div();
        layout.getStyle().set("font-size", "30px");
        layout.getStyle().set("color", "#333");
        layout.getStyle().set("margin-top", "10px");
        layout.getStyle().set("margin-left", "0px");
        layout.getStyle().set("margin-right", "0px");
        layout.getStyle().set("margin-bottom", "20px");
        layout.getStyle().set("padding-left", "55px");
        layout.getStyle().set("padding-right", "55px");
        layout.getStyle().set("padding-top", "25px");
        layout.getStyle().set("padding-bottom", "25px");
        layout.getStyle().set("border", "5px solid white");
        layout.getStyle().set("border-radius", "25px");
        return layout;
    }

    private Div createTimerLayout() {
        Div layout = firstDivFlexbox();
        SimpleTimer timer = getRemainingTimerLayout();
        timer.getStyle().setColor("white");
        timer.setFractions(false);
        timer.setHours(true);
        timer.setMinutes(true);
        timer.setCountUp(false);
        timer.start();
        H4 remainingTime = new H4("Remaining Time");
        layout.add(remainingTime);
        layout.add(timer);
        return layout;
    }
    private Div otpViewer(){
        Div layout = firstDivFlexbox();
        Span t1 = new Span(OTPGenerator.getInstance().getOTP());
        t1.getStyle().setColor("white");
        H4 otpViewer = new H4("Current OTP");
        layout.add(otpViewer);
        layout.add(t1);
        return layout ;
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
    private void stopOtpTimer() {
//            if (otpTimer != null) {
//                otpTimer.cancel();
//                otpTimer.purge();
//            }
        OTPGenerator.getInstance().removeListener(otpListener);
    }
    private void stopWifiTimer() {
//        if (wifiTimer != null) {
//            wifiTimer.cancel();
//            wifiTimer.purge();
//        }
        CurrentWifiHandler.getInstance().removeListeners(wifiListener);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        ui.getPushConfiguration().setPushMode(PushMode.AUTOMATIC);
        super.onAttach(attachEvent);
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        stopOtpTimer();
        stopWifiTimer();
        super.onDetach(detachEvent);
    }
}
