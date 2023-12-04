package edu.sltc.vaadin.views.login;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
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
public class LoginView extends LoginOverlay implements BeforeEnterObserver {

    public LoginView() {
        LoginI18n i18n = LoginI18n.createDefault();
        i18n.setHeader(new LoginI18n.Header());
        i18n.getHeader().setTitle("File Fortress");
        i18n.getHeader().setDescription("Login using Your Campus Email and Given Password");
        i18n.setAdditionalInformation(null);
        setI18n(i18n);
        setForgotPasswordButtonVisible(false);
        setOpened(true);
        setAction("login");

//        addLoginListener(new ComponentEventListener<LoginEvent>() {
//            @Override
//            public void onComponentEvent(LoginEvent loginEvent) {
//
//            }
//        });
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (!event.getLocation().getQueryParameters().getParameters().getOrDefault("error", Collections.emptyList()).isEmpty()){
            setError(event.getLocation().getQueryParameters().getParameters().containsKey("error"));
        }
    }

}
