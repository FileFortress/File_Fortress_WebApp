package edu.sltc.vaadin.views.login;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import edu.sltc.vaadin.views.about.AboutView;

import java.util.Collections;

@AnonymousAllowed
@PageTitle("Login")
@Route(value = "login")
@CssImport("/styles/login-view.css")
public class LoginView extends VerticalLayout implements BeforeEnterObserver {

    // Add a field for OTP input
    private TextField otpField;
    private final int otp = 2045;
    private LoginOverlay loginOverlay;
    private Dialog dialog;
    public LoginView() {
        LoginI18n i18n = LoginI18n.createDefault();
        i18n.setHeader(new LoginI18n.Header());
        i18n.getHeader().setTitle("File Fortress");
        i18n.getHeader().setDescription("Login using Your Campus Email and Given Password");
        i18n.setAdditionalInformation(null);
        loginOverlay = new LoginOverlay(i18n);
        loginOverlay.setForgotPasswordButtonVisible(false);
        loginOverlay.setOpened(true);
        loginOverlay.setAction("login");
        showOtpDialog();
    }
    private void showOtpDialog() {
        dialog = new Dialog();
        dialog.getElement().setAttribute("aria-label", "Enter Exam OTP");

        // Create dialog layout
        VerticalLayout dialogLayout = createDialogLayout(dialog);
        dialog.add(dialogLayout);

        // Add "Login" button to the dialog
        Button loginButton = new Button("Login", e -> {
            // Perform login action here
            performLogin();
        });
        loginButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        dialogLayout.add(loginButton);
        dialog.setCloseOnOutsideClick(false);
        dialog.setCloseOnEsc(false);
        // Show the dialog
        dialog.open();
    }

    private VerticalLayout createDialogLayout(Dialog dialog) {
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
    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (!event.getLocation().getQueryParameters().getParameters().getOrDefault("error", Collections.emptyList()).isEmpty()){
            loginOverlay.setError(event.getLocation().getQueryParameters().getParameters().containsKey("error"));
        }
    }

}
