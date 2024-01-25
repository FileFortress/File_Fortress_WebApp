package edu.sltc.vaadin.views.setupexam;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.timepicker.TimePicker;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility.Margin;
import edu.sltc.vaadin.data.GenerateKeyPair;
import edu.sltc.vaadin.data.PasswordGenerator;
import edu.sltc.vaadin.models.ExamModel;
import edu.sltc.vaadin.models.PasswordPool;
import edu.sltc.vaadin.models.PublicKeyHolder;
import edu.sltc.vaadin.services.*;
import edu.sltc.vaadin.views.MainLayout;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@PageTitle("Setup Exam")
@Route(value = "host_exam", layout = MainLayout.class)
@RolesAllowed("ADMIN")
@JsModule("./fileUploader.js")
//load jquery
@JavaScript("https://cdnjs.cloudflare.com/ajax/libs/jquery/3.4.1/jquery.min.js")
public class SetupExamView extends VerticalLayout {

    private final EmailSenderService senderService;

    private final InMemoryUserDetailsManager userDetailsManager;
    private TextField moduleCode, moduleName;
    private TextArea moduleDescription, studentEmailList;
    private  RadioButtonGroup<String> lateSubmission;
    private TimePicker startTimePicker, endTimePicker;
    private Button startServer;

    public SetupExamView(EmailSenderService senderService, InMemoryUserDetailsManager userDetailsManager) {
        this.senderService = senderService;
        this.userDetailsManager = userDetailsManager;
        setSpacing(false);
        ExamModel examModel = ExamModel.getInstance(); // Get the ExamModel instance
        H2 header = new H2("Exam Paper Registration");
        header.addClassNames(Margin.Top.LARGE, Margin.Bottom.MEDIUM);
        add(header);

        Div moduleDetails = new Div();
        moduleDetails.setMaxWidth("800px");
        moduleDetails.addClassNames(Margin.Top.SMALL, Margin.Bottom.LARGE);
        add(moduleDetails);

        FormLayout formLayout = new FormLayout();
        formLayout.setMaxWidth("600px");
        moduleDetails.add(formLayout);

        moduleCode = new TextField("Module Code");
        formLayout.add(moduleCode);

        moduleName = new TextField("Module Name");
        formLayout.add(moduleName);

        moduleDescription = new TextArea("Module Description");
        moduleDescription.setHeight("100px");
        formLayout.add(moduleDescription);
        formLayout.setColspan(moduleDescription, 2);

        lateSubmission =  new RadioButtonGroup<>();
        lateSubmission.setLabel("Late Submission");
        lateSubmission.setItems("NO", "10 Minutes", "15 Minutes", "20 Minutes", "25 Minutes","30 Minutes");
        lateSubmission.setValue("NO");
        formLayout.add(lateSubmission);
        formLayout.setColspan(lateSubmission, 2);

        startTimePicker = new TimePicker("Start Time");
        endTimePicker = new TimePicker("End Time");
        endTimePicker.addClassNames(Margin.Top.SMALL, Margin.Bottom.SMALL);
        formLayout.add(startTimePicker, endTimePicker);

        Upload studentListUpload = getStudentListUpload();
        studentListUpload.addClassNames(Margin.Top.SMALL, Margin.Bottom.MEDIUM);
        formLayout.add(studentListUpload);
        formLayout.setColspan(studentListUpload, 2);

        Upload upload = getPaperUpload();
        // Add the upload component to the layout of the UI
        formLayout.add(upload);
        formLayout.setColspan(upload, 2);

        // Set ExamModel data to the view
        startServer = new Button("Start Server");
        startServer.addThemeVariants(ButtonVariant.LUMO_PRIMARY,
                ButtonVariant.LUMO_SUCCESS);
        startServer.addClickListener(e -> {
            ExamModel.serverIsRunning = !ExamModel.serverIsRunning;
            if (ExamModel.serverIsRunning){
                startServer();
                CheckConnectedStudent.getInstance().StartTimerThread();
                CheckSubmittedAnswers.getInstance().StartTimerThread();
                startServer.setText("Stop Server");
                startServer.addThemeVariants(ButtonVariant.LUMO_ERROR);
            }else{
                examModel.resetInstance();
                CheckConnectedStudent.getInstance().StopTimerThread();
                CheckSubmittedAnswers.getInstance().StopTimerThread();
                //remove access from Student with Server.
                removeNewStudentsAccess();
                startServer.setText("Start Server");
                startServer.removeThemeVariants(ButtonVariant.LUMO_ERROR);
                startServer.addThemeVariants(ButtonVariant.LUMO_PRIMARY,
                        ButtonVariant.LUMO_SUCCESS);
            }
            System.out.println("Server State is "+ExamModel.serverIsRunning);
        });
        setExamModelData(examModel);
        add(startServer);

        formLayout.setResponsiveSteps(new ResponsiveStep("0", 1),
                new ResponsiveStep("350px", 2));

        setSizeFull();
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        getStyle().set("text-align", "center");

        // Execute JavaScript code when the view is attached to the UI
        UI.getCurrent().getPage().executeJs("uploadFile($0);uploadFile($1);","myVaadinUpload","myStudentUpload");
    }



    private static Upload getPaperUpload() {
        Button uploadPDF = new Button("Upload PDF");
        Upload upload = new Upload();
        upload.setId("myVaadinUpload");
        // Access the underlying HTML element of the Upload component
        // Define the file receiver that will handle the file upload
        MemoryBuffer memoryBuffer = new MemoryBuffer();
        upload.setReceiver(memoryBuffer);

        // Define the accepted file types. In this case, only PDF files are accepted.
        upload.setAcceptedFileTypes("application/pdf");
        Span dropLabel = new Span("Upload Exam Paper");
        upload.setDropLabel(dropLabel);
        upload.setUploadButton(uploadPDF);
        // Add a listener to the upload component that will be notified when the upload is finished
        upload.addSucceededListener(event -> {
//           FileEncryptionService.encryptFile(memoryBuffer.getInputStream(), "src/main/resources/examFile.pdf");
            if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof User user) {
                FileEncryptionService.decryptFile(memoryBuffer.getInputStream(), "Uploads/examFile_"+user.getUsername().split("@")[0]+".pdf", GenerateKeyPair.generateSharedSecret(PublicKeyHolder.getInstance().get(user.getUsername())));
                ExamModel.getInstance().setExamPaperName("examFile_" + user.getUsername().split("@")[0] + ".pdf");
            }
            // Retrieve the uploaded file from the FileReceiver
            // Create a Notification class that displays the success message
            Notification notification = Notification.show(event.getFileName()+" File uploaded successfully!");
            notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            notification.setPosition(Notification.Position.BOTTOM_END);
            notification.setDuration(2500);
        });
        return upload;
    }
    private static Upload getStudentListUpload() {
        Button uploadPDF = new Button("Upload Student List");
        Upload upload = new Upload();
        upload.setId("myStudentUpload");
        // Access the underlying HTML element of the Upload component
        // Define the file receiver that will handle the file upload
        MemoryBuffer memoryBuffer = new MemoryBuffer();
        upload.setReceiver(memoryBuffer);

        // Define the accepted file types. In this case, only PDF files are accepted.
        upload.setAcceptedFileTypes("text/plain");
        Span dropLabel = new Span("Upload Student List");
        upload.setDropLabel(dropLabel);
        upload.setUploadButton(uploadPDF);
        // Add a listener to the upload component that will be notified when the upload is finished
        upload.addSucceededListener(event -> {
            if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof User user) {
                FileEncryptionService.decryptFile(memoryBuffer.getInputStream(), "user_emails.txt", GenerateKeyPair.generateSharedSecret(PublicKeyHolder.getInstance().get(user.getUsername())));
            }
            // Retrieve the uploaded file from the FileReceiver
            // Create a Notification class that displays the success message
            Notification notification = Notification.show(event.getFileName() + " File uploaded successfully!");
            notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            notification.setPosition(Notification.Position.BOTTOM_END);
            notification.setDuration(1500);
        });
        return upload;
    }

    private void setExamModelData(ExamModel examModel) {
        // Check if ExamPaperName is not null and show saved details
        // Set ExamModel data to the view
        moduleCode.setValue(examModel.getModuleCode().orElse(" "));
        moduleName.setValue(examModel.getModuleName().orElse(" "));
        moduleDescription.setValue(examModel.getModuleDescription().orElse(" "));
        lateSubmission.setValue(examModel.getLateSubmission().orElse("NO"));
        startTimePicker.setValue(examModel.getStartTime().orElse(null));
        endTimePicker.setValue(examModel.getEndTime().orElse(null));
        if (ExamModel.serverIsRunning){
            startServer.setText("Stop Server");
            startServer.addThemeVariants(ButtonVariant.LUMO_ERROR);
        }

    }

    private void startServer() {
        // Implement your server start logic here
        // Obtain user input data
        String moduleCodeValue = moduleCode.getValue();
        String moduleNameValue = moduleName.getValue();
        String moduleDescriptionValue = moduleDescription.getValue();
        String lateSubmissionValue = lateSubmission.getValue();
        LocalTime startTimeValue = startTimePicker.getValue();
        LocalTime endTimeValue = endTimePicker.getValue();

        ExamModel examModel = ExamModel.getInstance();
        // Save data to the ExamModel
        examModel.setModuleCode(moduleCodeValue);
        examModel.setModuleName(moduleNameValue);
        examModel.setModuleDescription(moduleDescriptionValue);
        examModel.setLateSubmission(lateSubmissionValue);
        examModel.setStartTime( LocalDateTime.of(LocalDate.now(),startTimeValue));
        examModel.setEndTime(LocalDateTime.of(LocalDate.now(),endTimeValue));

        System.out.println(examModel);
        // Display success message or navigate to the student dashboard
        Notification.show("Exam details saved successfully!");

        //give access to students and have to add user to InMemoryUserDetailsManager
        List<String> emails = EmailExtractor.extractEmails("./user_emails.txt");
        PasswordPool.getInstance().setStudentPasswords(PasswordGenerator.bulkPasswordForStudents(emails.size(),10));
        if (!emails.isEmpty()) {
            ExecutorService executorService = Executors.newFixedThreadPool(emails.size());
            int i = 0;
            for (String email : emails) {
                String password = PasswordPool.getInstance().getStudentPasswords().stream().toList().get(i++);
                executorService.submit(() -> {
                    if (userDetailsManager.userExists(email)){
                        userDetailsManager.deleteUser(email);
                    }
                    senderService.sendEmail(email, "User Password", "Your User Password is " + password);
                    userDetailsManager.createUser(User.withUsername(email)
                            .password(new BCryptPasswordEncoder().encode(password))
                            .roles("USER")
                            .build());
                });
            }
            executorService.shutdown();
        }
    }
    private void removeNewStudentsAccess() {
        List<String> emails = EmailExtractor.extractEmails("./user_emails.txt");
        if (!emails.isEmpty()) {
            ExecutorService executorService = Executors.newFixedThreadPool(emails.size());
            for (String email : emails) {executorService.submit(() -> {
                    if (userDetailsManager.userExists(email)){
                        userDetailsManager.deleteUser(email);
                        System.out.println(email + " User Access removed from the Server!");
                        //TODO: have to clear the session & reload student end to login
                    }
                });
            }
            executorService.shutdown();
        }
    }
}
