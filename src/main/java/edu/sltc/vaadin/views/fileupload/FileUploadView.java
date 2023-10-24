package edu.sltc.vaadin.views.fileupload;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import edu.sltc.vaadin.views.MainLayout;
import edu.sltc.vaadin.views.setupexam.FileReceiver;
import jakarta.annotation.security.DeclareRoles;
import jakarta.annotation.security.RolesAllowed;



@PageTitle("File Upload")
@Route(value = "file_transfer", layout = MainLayout.class)
@RolesAllowed("USER")
public class FileUploadView extends HorizontalLayout {

    public FileUploadView() {
        VerticalLayout layout = new VerticalLayout();
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
        submitButton.setTooltipText("Submit Button");
        submitButton.setWidthFull();
        layout.add(upload,answerValidationCheckBox,submitButton);
        add(layout);


    }
    private static Upload getUpload() {

        Button uploadPDF = new Button("Upload PDF");
        uploadPDF.setTooltipText("You Can Select PDF file from here or You can Drag & Drop PDF FIle Directly");
        Upload upload = new Upload();
        // Define the file receiver that will handle the file upload
        upload.setReceiver(new FileReceiver());
        // Define the accepted file types. In this case, only PDF files are accepted.
        upload.setAcceptedFileTypes("application/pdf");
        Span dropLabel = new Span("Upload Answer Paper");
//        uploadPDF.getStyle().set("left","50%");
//        uploadPDF.getStyle().set("top","50%");
        upload.setDropLabel(dropLabel);
        upload.setUploadButton(uploadPDF);
        // Add a listener to the upload component that will be notified when the upload is finished
        upload.addFinishedListener(event -> {
            // Retrieve the uploaded file from the FileReceiver
            // Create a Notification class that displays the success message
            Notification notification = Notification
                    .show(event.getFileName()+" File uploaded successfully!");
            notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            notification.setPosition(Notification.Position.BOTTOM_END);
            notification.setDuration(2500);
        });
        return upload;
    }
}
