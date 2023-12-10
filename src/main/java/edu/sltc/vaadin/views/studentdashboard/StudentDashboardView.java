package edu.sltc.vaadin.views.studentdashboard;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import edu.sltc.vaadin.models.ExamModel;
import edu.sltc.vaadin.timer.SimpleTimer;
import edu.sltc.vaadin.views.MainLayout;
import jakarta.annotation.security.RolesAllowed;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@PageTitle("Student Dashboard")
@Route(value = "student_dashboard", layout = MainLayout.class)
//@RouteAlias(value = "", layout = MainLayout.class)
@RolesAllowed("USER")
//@JsModule()
public class StudentDashboardView extends VerticalLayout {
    private TextField otpField;
    private final int otp = 2045;
    private Dialog dialog;

    public StudentDashboardView() {
        setSpacing(false);
        // Obtain the ExamModel instance
        ExamModel examModel = ExamModel.getInstance();
        if (examModel.getExamPaperName() != null){
            /*
             * module name
             */
            H1 header_one = new H1(examModel.getModuleName());
            header_one.addClassNames(LumoUtility.Margin.Top.MEDIUM, LumoUtility.Margin.Bottom.XSMALL);
            header_one.setWidthFull();
            add(header_one);
            /*
             * module code
             */
            H2 header_two = new H2(examModel.getModuleCode());
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

            /*
             * start time
             */
            Span start_time = new Span("Start Time : " + examModel.getStartTime());
            formLayout.add(start_time);

            /*
             * end time
             */
            Span end_time = new Span("End Time : " + examModel.getEndTime());
            formLayout.add(end_time);

            /*
             * Timer
             */
            Div remainingTimeDiv = new Div();
            H2 remainingTime = new H2("Remaining Time");
            add(remainingTime);
            add(createTimerLayout());

            /*
             * Exam Instructions
             */
            TextArea examInstructions = new TextArea("Exam Instructions");
            examInstructions.setHeight("500px");
            examInstructions.setMaxWidth("1000px");
            examInstructions.setValue(examModel.getModuleDescription());
            examInstructions.setReadOnly(true);
            examInstructions.setWidthFull();
            add(examInstructions);

            formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1),
                    new FormLayout.ResponsiveStep("350px", 2));

            /*
             * Late Submission
             */
            Span Late_submission = new Span("No Late Submission Allowed");
            Late_submission.addClassNames(LumoUtility.Margin.Top.MEDIUM);
            add(Late_submission);
            formLayout.setColspan(Late_submission, 2);
        } else {
            H2 errorHeader = new H2("Please Wait Until Exam paper is Uploaded");
            add(errorHeader);
        }

        showOtpDialog();
        setSizeFull();
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
        otpField = new TextField("OTP");
        VerticalLayout fieldLayout = new VerticalLayout(otpField);
        fieldLayout.setSpacing(false);
        fieldLayout.setPadding(false);
        fieldLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
        VerticalLayout dialogLayout = new VerticalLayout(headline, fieldLayout);
        dialogLayout.setPadding(false);
        dialogLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
        dialogLayout.getStyle().set("width", "300px").set("max-width", "100%");
        // Add "Login" button to the dialog
        Button loginButton = new Button("Login", e -> {
            // Perform login action here
            performLogin();
        });
        loginButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        dialogLayout.add(loginButton);

        return dialogLayout;
    }

    private void performLogin() {
        // Implement your login logic here
        // Check OTP and proceed with login
        int enteredOtp = Integer.parseInt(otpField.getValue());
        if (enteredOtp == otp) {
            // Continue with login logic
            dialog.close();
        } else {
            // Display an error message for incorrect OTP
            otpField.setErrorMessage("Enter Valid OTP!");
        }
    }

}