package edu.sltc.vaadin.views.login;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import edu.sltc.vaadin.views.about.AboutView;

@AnonymousAllowed
@PageTitle("Login")
@Route(value = "login")
@CssImport("/styles/login-view.css")
public class LoginView extends VerticalLayout implements BeforeEnterObserver{
    private static final String OAUTH_URL = "/oauth2/authorization/google";
    public LoginView(){
        // Header
        Div header = new Div();
        header.setText("Welcome to File Fortress");
        header.getStyle().set("font-size", "24px");
        add(header);

        // Slogan
        Div slogan = new Div();
        slogan.setText("LAN Exam File Transfer System");
        slogan.getStyle().set("font-size", "18px");
        add(slogan);

        // Description
        Div description = new Div();
        description.setText("Login with Your Google Account to Continue");
        description.getStyle().set("font-size", "16px");
        add(description);

        // Login button
        Button loginButton = new Button("Login with Google");
        loginButton.setTooltipText("You Can Sign In to Application using your Google Account");
        loginButton.addClassName("login-button");
        loginButton.addClickListener(event -> {
            // Redirect to OAUTH_URLAnchor
            UI.getCurrent().getPage().setLocation(OAUTH_URL);
        });

        add(loginButton);

        // Styling
        getStyle().set("padding", "20px");
        setAlignItems(FlexComponent.Alignment.CENTER);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
//    UI.getCurrent().navigate(AboutView.class);
    }

}
