package edu.sltc.vaadin.views.fileupload;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import edu.sltc.vaadin.views.MainLayout;
import jakarta.annotation.security.DeclareRoles;
import jakarta.annotation.security.RolesAllowed;

@PageTitle("File Upload")
@Route(value = "file_transfer", layout = MainLayout.class)
@RolesAllowed("USER")
public class FileUploadView extends HorizontalLayout {

    private TextField name;
    private Button sayHello;

    public FileUploadView() {
        name = new TextField("Your name");
        sayHello = new Button("Say hello");
        sayHello.addClickListener(e -> {
            Notification.show("Hello " + name.getValue());
        });
        sayHello.addClickShortcut(Key.ENTER);

        setMargin(true);
        setVerticalComponentAlignment(Alignment.END, name, sayHello);

        add(name, sayHello);
    }

}
