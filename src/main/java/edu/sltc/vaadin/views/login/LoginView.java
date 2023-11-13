package edu.sltc.vaadin.views.login;

import com.google.api.client.util.Value;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.internal.RouteUtil;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import edu.sltc.vaadin.security.AuthenticatedUser;
import edu.sltc.vaadin.security.FirebaseAuthenticationService;

@AnonymousAllowed
@PageTitle("Login")
@Route(value = "login")
@CssImport("/styles/login-view.css")
public class LoginView extends VerticalLayout implements BeforeEnterObserver{
    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;
    private static final String OAUTH_URL = "/oauth2/authorization/google";
    private static final String REDIRECT_URL = "/login";
//    private final AuthenticatedUser authenticatedUser;
//    private final FirebaseAuthenticationService firebaseAuthenticationService;

    public LoginView(){
        Anchor loginLink = new Anchor(OAUTH_URL, "Login with Google");
        // Set router-ignore attribute so that Vaadin router doesn't handle the login request
        loginLink.getElement().setAttribute("router-ignore", true);
        add(loginLink);
        getStyle().set("padding", "200px");
        setAlignItems(FlexComponent.Alignment.CENTER);
    }

//    public LoginView(AuthenticatedUser authenticatedUser, FirebaseAuthenticationService firebaseAuthenticationService) {
//        this.authenticatedUser = authenticatedUser;
//        this.firebaseAuthenticationService = firebaseAuthenticationService;
//        setSizeFull();
//        setAlignItems(Alignment.CENTER);
//        setJustifyContentMode(JustifyContentMode.CENTER);
//        // Your existing code for Vaadin login view
//
//        // Add Google login button
//        Button googleLoginButton = new Button("Login with Google");
//        // Your existing code for Vaadin login view
//        googleLoginButton.addClickListener(e -> {
//            // Handle Google authentication here
//            // Redirect the user to Firebase authentication page or handle it using Firebase SDK
//            Notification.show("Google authentication clicked!");
//            // Replace 'googleIdToken' with the actual Google ID token obtained from the frontend
//            String googleIdToken = obtainGoogleIdToken(); // Implement this method to get the Google ID token
//
//            try {
//                FirebaseToken firebaseToken = firebaseAuthenticationService.authenticateWithFirebase(googleIdToken);
//
//                // Now you have the Firebase token, you can use it to identify the user or perform further actions
//
//                // Example: Get user UID
//                String uid = firebaseToken.getUid();
//
//                // Example: Check if the user is an anonymous user
//                boolean isAnonymous = !firebaseToken.isEmailVerified();
//
//                // Add your logic here to handle the authenticated user
//                // For example, set the user as authenticatedUser and forward to the next view
//
//            } catch (FirebaseAuthException ex) {
//                ex.printStackTrace();
//                Notification.show("Firebase authentication failed");
//            }
//        });
//
//        // Add components to the layout
//        add(googleLoginButton);
//    }
//    private String obtainGoogleIdToken() {
//        // Assume you have a JavaScript function that retrieves the Google ID token
//        String script = "function getGoogleIdToken() {" +
//                "  // Implement your logic to get the Google ID token using the Google Sign-In API" +
//                "  var auth2 = gapi.auth2.getAuthInstance();" +
//                "  var user = auth2.currentUser.get();" +
//                "  var idToken = user.getAuthResponse().id_token;" +
//                "  return idToken;" +
//                "}";
//
//        // Execute the script to get the Google ID token
//        String googleIdToken = executeScript(script);
//
//        return googleIdToken;
//    }
//
//    // This method executes JavaScript on the frontend
//    private String executeScript(String script) {
//        // Get the current UI
//        UI ui = UI.getCurrent();
//
//        // Execute the script and get the result
//        String result = "ui.getPage().executeJavaScript(script).toString()";
//
//        return result;
//    }
@Override
public void beforeEnter(BeforeEnterEvent event) {
    if (event.getLocation().getPath().equals(REDIRECT_URL)) {
        // This request is the callback from Google after successful authentication
        String accessToken = event.getLocation()
                .getQueryParameters()
                .getParameters()
                .get("access_token").get(0);

        if (accessToken != null && !accessToken.isEmpty()) {
            // Do something with the access token, e.g., store it or use it for further actions
            Notification.show("Google Access Token: " + accessToken);
            // Redirect to a different view or perform other actions as needed
            UI.getCurrent().navigate("/about");
        } else {
            // Handle the case where the access token is not present
            Notification.show("Failed to retrieve Google Access Token");
            UI.getCurrent().navigate("/login"); // Redirect back to the login view or handle appropriately
        }
    }

    if (event.getLocation().getQueryParameters().getParameters().containsKey("error")) {
        // Handle errors if needed
        event.forwardTo("/");
    }
}
}
