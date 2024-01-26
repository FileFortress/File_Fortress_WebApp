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
import com.vaadin.flow.shared.communication.PushMode;
import com.vaadin.flow.theme.lumo.LumoUtility.Margin;
import edu.sltc.vaadin.data.ConnectedStudentsListener;
import edu.sltc.vaadin.data.OTPListener;
import edu.sltc.vaadin.data.StudentsAnswerSubmissionListener;
import edu.sltc.vaadin.data.WifiListener;
import edu.sltc.vaadin.models.ExamModel;
import edu.sltc.vaadin.services.CheckConnectedStudent;
import edu.sltc.vaadin.services.CheckSubmittedAnswers;
import edu.sltc.vaadin.services.CurrentWifiHandler;
import edu.sltc.vaadin.services.OTPGenerator;
import edu.sltc.vaadin.timer.SimpleTimer;
import edu.sltc.vaadin.timer.TimerConstants;
import edu.sltc.vaadin.views.MainLayout;
import edu.sltc.vaadin.views.fileupload.FileUploadView;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.security.core.session.SessionRegistry;

import java.time.LocalDateTime;
import java.util.Optional;
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
    private OTPListener otpListener;
    private WifiListener wifiListener;
    private ConnectedStudentsListener connectedStudentsListener;
    private StudentsAnswerSubmissionListener studentsAnswerSubmissionListener;
    private SimpleTimer timer;
    public AdminDashboardView() {
        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        ui = UI.getCurrent();
        ExamModel examModel = ExamModel.getInstance();
        Div firstModule = new Div();
        firstModule.setMaxWidth("800px");
        firstModule.addClassNames(Margin.Top.MEDIUM, Margin.Bottom.MEDIUM);
        add(firstModule);
        FormLayout formLayoutOne = new FormLayout();
        formLayoutOne.getElement().setAttribute("id", "my-top-form-layout");
        firstModule.add(formLayoutOne);
        Div timer1 = createTimerLayout(examModel.getEndDateTime());
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
            // Execute below method to change color after 3 seconds
            new Timer(true).schedule(new TimerTask() {
                @Override
                public void run() {ui.access(()->{
                   otp1.getComponentAt(1).getElement().getStyle().setColor("red");
                    // Push an empty update to trigger a background refresh
                    ui.push();
                });}
            },3000);
        };
        OTPGenerator.getInstance().addListener(otpListener);
        ui.getPage().executeJs("setContentView()");
//        timer.getElement().getChild(0).setText("00:00:00");
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
        JoinedStudentsTextField.setValue(CheckConnectedStudent.getStudentCount());
        submissionCountTextField.setValue(CheckSubmittedAnswers.getAnswerCount());
        connectedStudentsListener = studentCount->{
            ui.access(()->{
                JoinedStudentsTextField.setValue(studentCount);
            });
        };
        CheckConnectedStudent.getInstance().addListener(connectedStudentsListener);
        studentsAnswerSubmissionListener = answerCount->{
            ui.access(()->{
                submissionCountTextField.setValue(answerCount);
            });
        };
        CheckSubmittedAnswers.getInstance().addListener(studentsAnswerSubmissionListener);

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

    private Div createTimerLayout(Optional<LocalDateTime> endTime) {
        Div layout = firstDivFlexbox();
        timer = TimerConstants.getRemainingTimerLayout(endTime);
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
    @Override
    protected void onAttach(AttachEvent attachEvent) {
        ui.getPushConfiguration().setPushMode(PushMode.AUTOMATIC);
        super.onAttach(attachEvent);
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        OTPGenerator.getInstance().removeListener(otpListener);
        CurrentWifiHandler.getInstance().removeListeners(wifiListener);
        CheckConnectedStudent.getInstance().removeListener(connectedStudentsListener);
        CheckSubmittedAnswers.getInstance().removeListener(studentsAnswerSubmissionListener);
        super.onDetach(detachEvent);
    }
}
