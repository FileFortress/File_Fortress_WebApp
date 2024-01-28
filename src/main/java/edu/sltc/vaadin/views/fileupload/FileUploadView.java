package edu.sltc.vaadin.views.fileupload;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import edu.sltc.vaadin.data.GenerateKeyPair;
import edu.sltc.vaadin.models.ExamModel;
import edu.sltc.vaadin.models.PublicKeyHolder;
import edu.sltc.vaadin.services.CheckSubmittedAnswers;
import edu.sltc.vaadin.services.FileEncryptionService;
import edu.sltc.vaadin.services.OTPGenerator;
import edu.sltc.vaadin.views.MainLayout;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

import static edu.sltc.vaadin.services.OTPUIService.showOtpDialog;


@PageTitle("File Upload")
@Route(value = "file_transfer", layout = MainLayout.class)
@RolesAllowed("USER")
@JsModule("./fileUploader.js")
public class FileUploadView extends HorizontalLayout {
    private final MemoryBuffer memoryBuffer = new MemoryBuffer();
    private String answerPaperName;

    public FileUploadView() {
        VerticalLayout layout = new VerticalLayout();
        if (ExamModel.serverIsRunning && ExamModel.getInstance().getStartTime().orElse(LocalTime.MAX).isBefore(LocalTime.now())){
            layout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);

            Upload upload = getUpload();
            upload.setWidthFull();
            upload.getStyle().set("display","flex");
            upload.getStyle().set("justify-content","center");
            upload.getStyle().set("align-items","center");
            upload.setMinHeight("330px");

            /*
             * checkBox Component
             */
            Checkbox answerValidationCheckBox = new Checkbox();
            answerValidationCheckBox.setLabel("I Acknowledged that these submission is honest work from me");
            answerValidationCheckBox.getStyle().setMargin("20px");

            /*
             * Button component
             */
            Button submitButton = new Button("Submit");
            submitButton.setId("SubmitButton");
            submitButton.setTooltipText("Submit Button");
            submitButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
                @Override
                public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                    if (answerValidationCheckBox.getValue() && isSubmissionOnTime() && memoryBuffer.getInputStream() != null){
                        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof User user) {
                            FileEncryptionService.decryptFile(memoryBuffer.getInputStream(),
                                    "Uploads/answers/"+user.getUsername().split("@")[0]+"_"+ExamModel.getInstance().getModuleCode()+".pdf",
                                    GenerateKeyPair.generateSharedSecret(PublicKeyHolder.getInstance().get(user.getUsername())));

                            CheckSubmittedAnswers.getInstance().addStudentEmail(user.getUsername());
                        }
                        Notification notification = Notification
                                .show(answerPaperName + " File Uploaded with Success!");
                        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                        notification.setPosition(Notification.Position.BOTTOM_END);
                        notification.setDuration(2500);
                    }else if (!answerValidationCheckBox.getValue()){
                        Notification notification = new Notification("Please make sure You have Check the Acknowledge.", 2000, Notification.Position.BOTTOM_CENTER);
                        notification.addThemeVariants(NotificationVariant.LUMO_PRIMARY);
                        notification.open();
                    } else if (!isSubmissionOnTime()){
                        Notification notification = new Notification("Sorry!!, Exam Time Over Please Contact Examiner", 2000, Notification.Position.MIDDLE);
                        notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                        notification.open();
                    } else {
                        Notification notification = new Notification("Make Sure You have upload and submit your answers correctly!", 2000, Notification.Position.MIDDLE);
                        notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                        notification.open();
                    }
                }
            });
            submitButton.setWidthFull();
            layout.add(upload,answerValidationCheckBox,submitButton);
        }
        else {
            H2 errorHeader = new H2("Please Wait Until Exam is Started");
            layout.add(errorHeader);
            layout.setDefaultHorizontalComponentAlignment(Alignment.CENTER);

        }
        add(layout);
        showOtpDialog();
        // Execute JavaScript code when the view is attached to the UI
        UI.getCurrent().getPage().executeJs("uploadFile($0)","myVaadinUpload");
    }

    private Upload getUpload() {

        Button uploadPDF = new Button("Upload PDF");
        uploadPDF.setTooltipText("You Can Select PDF file from here or You can Drag & Drop PDF FIle Directly");
        Upload upload = new Upload();
        upload.setId("myVaadinUpload");
        // Define the file receiver that will handle the file upload
        upload.setReceiver(memoryBuffer);
        // Define the accepted file types. In this case, only PDF files are accepted.
        upload.setAcceptedFileTypes("application/pdf");
        Span dropLabel = new Span("Upload Answer Paper");
        upload.setDropLabel(dropLabel);
        upload.setUploadButton(uploadPDF);
        // Add a listener to the upload component that will be notified when the upload is finished
        upload.addFinishedListener(event -> {
            // Retrieve the uploaded file from the FileReceiver
            // Create a Notification class that displays the success message
            answerPaperName = event.getFileName();
            Notification notification = Notification.show(answerPaperName + " File Added To Waiting List!");
            notification.addThemeVariants(NotificationVariant.LUMO_PRIMARY);
            notification.setPosition(Notification.Position.BOTTOM_END);
            notification.setDuration(1500);
        });
        return upload;
    }

    private boolean isSubmissionOnTime(){
        ExamModel examModel = ExamModel.getInstance();
        if (examModel.getEndTime().isPresent() && examModel.getStartTime().isPresent() && examModel.getLateSubmission().isPresent()) {
            if (examModel.getEndTime().get().plusMinutes(examModel.getLateSubmissionValue().orElse(0))
                    .isAfter(LocalTime.now())
                    && examModel.getStartTime().get().isBefore(LocalTime.now()))
                return true;
        }
        return false;
    }
}
