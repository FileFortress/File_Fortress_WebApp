package edu.sltc.vaadin.services;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;

/**
 * @author Nuyun-Kalamullage
 * @IDE IntelliJ IDEA
 * @date 27/01/2024
 * @package edu.sltc.vaadin.services
 * @project_Name File_Fortress_WebApp
 */
public class OTPUIService {
    public static void showOtpDialog() {
        // Create dialog layout
        Dialog dialog = createDialogLayout();
        // Show the dialog
        dialog.open();
    }

    private static Dialog createDialogLayout() {

        Dialog dialog = new Dialog();
        dialog.getElement().setAttribute("aria-label", "Enter Exam OTP");
        dialog.setCloseOnOutsideClick(false);
        dialog.setCloseOnEsc(false);
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
            performLogin(otpField, dialog);
        });
        loginButton.addClickShortcut(Key.ENTER);
        loginButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        dialogLayout.add(loginButton);
        dialog.add(dialogLayout);
        return dialog;
    }

    private static void performLogin(TextField otpField, Dialog dialog) {
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
        }catch (NumberFormatException e){
            Notification notification = new Notification("Enter Valid OTP!", 5000, Notification.Position.MIDDLE);
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            notification.open();
        } catch (Exception e){
            System.out.println(e.getMessage());
        } finally {
            otpField.focus();
        }
    }
}
