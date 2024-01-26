package edu.sltc.vaadin.views.studentdashboard;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.theme.lumo.LumoUtility;
import edu.sltc.vaadin.models.ExamModel;
import edu.sltc.vaadin.services.OTPGenerator;
import edu.sltc.vaadin.timer.SimpleTimer;
import edu.sltc.vaadin.timer.TimerConstants;
import edu.sltc.vaadin.views.MainLayout;
import jakarta.annotation.security.RolesAllowed;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@PageTitle("Student Dashboard")
@Route(value = "student_dashboard", layout = MainLayout.class)
//@RouteAlias(value = "", layout = MainLayout.class)
@RolesAllowed("USER")
@JsModule("./clientDecrypt.js")
public class StudentDashboardView extends VerticalLayout {
    //    private TextField otpField;
//    private final int otp = 2045;
    private Dialog dialog;

    public StudentDashboardView() {
        setSpacing(false);
        // Obtain the ExamModel instance
        ExamModel examModel = ExamModel.getInstance();

        examModel.getExamPaperName().ifPresent((s)->{
            System.out.println("Paper Name : " + s);
        });
        if (ExamModel.serverIsRunning){
            /*
             * module name
             */
            H1 header_one = new H1(examModel.getModuleName().orElse(""));
            header_one.addClassNames(LumoUtility.Margin.Top.MEDIUM, LumoUtility.Margin.Bottom.XSMALL);
            header_one.setWidthFull();
            add(header_one);
            /*
             * module code
             */
            H2 header_two = new H2(examModel.getModuleCode().orElse(" "));
            header_two.addClassNames(LumoUtility.Margin.Top.XSMALL, LumoUtility.Margin.Bottom.MEDIUM);
            header_two.setWidthFull();
            add(header_two);

            Div moduleDetails = new Div();
            moduleDetails.setMaxWidth("800px");
            moduleDetails.addClassNames(LumoUtility.Margin.Top.SMALL, LumoUtility.Margin.Bottom.LARGE);
            add(moduleDetails);

            FormLayout formLayout = new FormLayout();
            formLayout.setMaxWidth("600px");
            moduleDetails.add(formLayout);

            // Inline "Start Time" and "End Time" components
            HorizontalLayout timeLayout = new HorizontalLayout();
            timeLayout.setAlignItems(Alignment.CENTER); // Align items vertically
            timeLayout.setWidthFull(); //set full width of component

            // Start time
            Span start_time = new Span("Start Time: " + examModel.getStartTime().orElse(null));
            timeLayout.add(start_time);

            // Spacer
            timeLayout.add(new HorizontalLayout(new Span(" "))); // Add a spacer

            // End time
            Span end_time = new Span("End Time: " + examModel.getEndTime().orElse(null));
            timeLayout.add(end_time);

            // Add the time layout to the form layout
            formLayout.add(timeLayout);

            /*
             * Timer
             */
//          Div remainingTimeDiv = new Div();
            H2 remainingTime = new H2("Remaining Time");
            add(remainingTime);
            add(createTimerLayout(examModel.getEndDateTime()));

            /*
             * Exam Instructions
             */
            TextArea examInstructions = new TextArea("Exam Instructions");
            examInstructions.setHeight("500px");
            examInstructions.setMaxWidth("1000px");
            examInstructions.setValue(examModel.getModuleDescription().orElse(""));
            examInstructions.setReadOnly(true);
            examInstructions.setWidthFull();
            add(examInstructions);

            formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1),
                    new FormLayout.ResponsiveStep("350px", 2));

            /*
             * Late Submission
             */

            String lateSubmission = examModel.getLateSubmission().orElse("NO");
            Span Late_submission = new Span(lateSubmission + " Late Submission Allowed");
            Late_submission.addClassNames(LumoUtility.Margin.Top.MEDIUM);
            add(Late_submission);
            formLayout.setColspan(Late_submission, 2);

            /*
             * Download Button
             */
            String examFileName = ExamModel.getInstance().getExamPaperName().orElseGet(()->"");
            StreamResource streamResource = new StreamResource(examFileName,
                    () -> {
                        try {
                            return new FileInputStream("Uploads/" + examFileName);
                        } catch (FileNotFoundException e) {
                            throw new RuntimeException(e);
                        }
                    });
            Anchor downloadLink = new Anchor(streamResource, "" );
            downloadLink.getElement().setAttribute("download", true);
            downloadLink.getElement().getStyle().set("display", "none");
            Button downloadBtn = new Button("Download Paper");
            downloadBtn.addClickListener(event -> downloadLink.getElement().callJsFunction("click"));
            add(downloadBtn, downloadLink);

        } else {
            H2 errorHeader = new H2("Please Wait Until Exam paper is Uploaded");
            add(errorHeader);
        }

        showOtpDialog();
        setSizeFull();
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        getStyle().set("text-align", "center");
    }

    private Div createTimerLayout(Optional<LocalDateTime> endTime) {
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
        SimpleTimer timer = TimerConstants.getRemainingTimerLayout(endTime);
        timer.getStyle().setColor("white");
        timer.setFractions(false);
        timer.setHours(true);
        timer.setMinutes(true);
        timer.setCountUp(false);
        timer.start();
        layout.add(timer);
        return layout;
    }
    private void showOtpDialog() {
        dialog = new Dialog();
        dialog.getElement().setAttribute("aria-label", "Enter Exam OTP");
        // Create dialog layout
        VerticalLayout dialogLayout = createDialogLayout();
        dialog.add(dialogLayout);
        dialog.setCloseOnOutsideClick(false);
        dialog.setCloseOnEsc(false);
        // Show the dialog
        dialog.open();
    }

    private VerticalLayout createDialogLayout() {
        H2 headline = new H2("Enter Exam OTP");
        headline.getStyle().set("margin", "var(--lumo-space-m) 0 0 0")
                .set("font-size", "1.5em").set("font-weight", "bold");

        // Add an OTP input field
        TextField otpField = new TextField("OTP");
        VerticalLayout fieldLayout = new VerticalLayout(otpField);
        fieldLayout.setSpacing(false);
        fieldLayout.setPadding(false);
        fieldLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
        VerticalLayout dialogLayout = new VerticalLayout(headline, fieldLayout);
        dialogLayout.setPadding(false);
        dialogLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
        dialogLayout.getStyle().set("width", "300px").set("max-width", "100%");
        // Add "Login" button to the dialog
        Button loginButton = new Button("Submit", e -> {
            // Perform login action here
            performLogin(otpField);
        });
        loginButton.addClickShortcut(Key.ENTER);
        loginButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        dialogLayout.add(loginButton);

        return dialogLayout;
    }

    private void performLogin(TextField otpField) {
        // Implement your login logic here
        // Check OTP and proceed with login
        try {
            int enteredOtp = Integer.parseInt(otpField.getValue());
            if (OTPGenerator.getInstance().getOTP().equals(String.valueOf(enteredOtp))) {
                // Continue with login logic
                dialog.close();
            } else {
                // Display an error message for incorrect OTP
                Notification notification = new Notification("Entered Incorrect OTP!", 5000, Notification.Position.MIDDLE);
                notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                notification.open();
            }
        } catch (NumberFormatException e){
            Notification notification = new Notification("Enter Valid OTP!", 5000, Notification.Position.MIDDLE);
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            notification.open();
        } catch (Exception e){
            e.getMessage();
        }
        otpField.focus();
    }

}