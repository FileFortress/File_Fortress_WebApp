package edu.sltc.vaadin.views.login;

import com.vaadin.flow.component.ClientCallable;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;

@AnonymousAllowed
@PageTitle("Login")
@Route(value = "login")
@CssImport("/styles/login-view.css")
@JsModule("./loginView.js")
public class LoginView extends VerticalLayout implements BeforeEnterObserver {
    private LoginOverlay loginOverlay;
    private final PasswordEncoder passwordEncoder;

    public LoginView(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
        LoginI18n i18n = LoginI18n.createDefault();
        i18n.setHeader(new LoginI18n.Header());
        i18n.getHeader().setTitle("File Fortress");
        i18n.getHeader().setDescription("Login using Your Campus Email and Given Password");
        i18n.setAdditionalInformation(null);
        loginOverlay = new LoginOverlay(i18n);
        loginOverlay.setForgotPasswordButtonVisible(false);
        loginOverlay.setOpened(true);
        loginOverlay.setAction("login");
        loginOverlay.addLoginListener(loginEvent -> {
            System.out.println("User Clicked Login!!");
        });
        UI.getCurrent().getPage().executeJs("ns.init($0)",this);
    }
    @ClientCallable
    public String encryptPassword (String plainText){
        System.out.println("badu weda!!");
        return passwordEncoder.encode(plainText);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (!event.getLocation().getQueryParameters().getParameters().getOrDefault("error", Collections.emptyList()).isEmpty()){
            loginOverlay.setError(event.getLocation().getQueryParameters().getParameters().containsKey("error"));
        }
    }

}
