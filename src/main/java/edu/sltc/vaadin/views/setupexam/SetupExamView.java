package edu.sltc.vaadin.views.setupexam;

import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
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
import com.vaadin.flow.component.upload.FinishedEvent;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility.Margin;
import edu.sltc.vaadin.views.MainLayout;
import jakarta.annotation.security.RolesAllowed;

@PageTitle("Setup Exam")
@Route(value = "host_exam", layout = MainLayout.class)
@RolesAllowed("ADMIN")
public class SetupExamView extends VerticalLayout {

    public SetupExamView() {
        setSpacing(false);

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

        TextField moduleCode = new TextField("Module Code");
        formLayout.add(moduleCode);

        TextField moduleName = new TextField("Module Name");
        formLayout.add(moduleName);

        TextArea moduleDescription = new TextArea("Module Description");
        moduleDescription.setHeight("100px");
        formLayout.add(moduleDescription);
        formLayout.setColspan(moduleDescription, 2);

        RadioButtonGroup<String> lateSubmission =  new RadioButtonGroup<>();
        lateSubmission.setLabel("Late Submission");
        lateSubmission.setItems("NO", "10 Minutes", "15 Minutes", "20 Minutes", "25 Minutes","30 Minutes");
        lateSubmission.setValue("15 Minutes");
        formLayout.add(lateSubmission);
        formLayout.setColspan(lateSubmission, 2);


        TimePicker startTimePicker = new TimePicker("Start Time");
        TimePicker endTimePicker = new TimePicker("End Time");

        endTimePicker.addClassNames(Margin.Top.SMALL, Margin.Bottom.SMALL);

        formLayout.add(startTimePicker, endTimePicker);
        Upload upload = getUpload();
        // Add the upload component to the layout of the UI
        formLayout.add(upload);
        formLayout.setColspan(upload, 2);

        Button startServer = new Button("Start Server");
        startServer.addClickListener(e -> startServer());
        add(startServer);

        formLayout.setResponsiveSteps(new ResponsiveStep("0", 1),
                new ResponsiveStep("350px", 2));

        setSizeFull();
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        getStyle().set("text-align", "center");
    }

    private static Upload getUpload() {
        Button uploadPDF = new Button("Upload PDF");
        Upload upload = new Upload();
        // Define the file receiver that will handle the file upload
        upload.setReceiver(new FileReceiver());
        // Define the accepted file types. In this case, only PDF files are accepted.
        upload.setAcceptedFileTypes("application/pdf");
        Span dropLabel = new Span("Upload Exam Paper");
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

    private void startServer() {
        // Implement your server start logic here
    }

}
