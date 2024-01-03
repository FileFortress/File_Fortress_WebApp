package edu.sltc.vaadin.views;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.VaadinServletRequest;
import com.vaadin.flow.server.auth.AccessAnnotationChecker;
import com.vaadin.flow.theme.lumo.LumoUtility;
import edu.sltc.vaadin.data.GenerateKeyPair;
import edu.sltc.vaadin.views.about.AboutView;
import edu.sltc.vaadin.views.admindashboard.AdminDashboardView;
import edu.sltc.vaadin.views.fileupload.FileUploadView;
import edu.sltc.vaadin.views.setupexam.SetupExamView;
import edu.sltc.vaadin.views.studentdashboard.StudentDashboardView;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.vaadin.lineawesome.LineAwesomeIcon;

import java.nio.file.attribute.UserPrincipal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;

/**
 * The main view is a top-level placeholder for other views.
 */

@JsModule("./clientKeyExchange.js")
public class MainLayout extends AppLayout {
    private static final String LOGOUT_SUCCESS_URL = "/login";
    private H2 viewTitle;
    private AccessAnnotationChecker accessChecker;
    private Authentication authentication;

    public MainLayout(AccessAnnotationChecker accessChecker) {
        this.accessChecker = accessChecker;
        this.authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        Object credentials = authentication.getCredentials();
        System.out.println("Principal : " + principal);
        System.out.println("Credentials : " + credentials);
        System.out.println("Authorities: " + authentication.getAuthorities().stream().toList().get(0));
        setPrimarySection(Section.DRAWER);
        addDrawerContent();
        addHeaderContent();
        sendServerPublicKeyToUser(authentication.getAuthorities().stream().toList().get(0));
    }
    private void addHeaderContent() {
        DrawerToggle toggle = new DrawerToggle();
        toggle.setAriaLabel("Menu toggle");
        viewTitle = new H2();
        viewTitle.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);
        addToNavbar(true, toggle, viewTitle);
    }

    private void addDrawerContent() {
        H2 appName = new H2("File Fortress");
        // Create a Style tag and set the custom CSS
        Image image = new Image("images/logo_placeholder.png", "placeholder plant");
        image.setWidth("60px");
        appName.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);
        Header header = new Header(image, appName);
        Scroller scroller = new Scroller(createNavigation());
        addToDrawer(header, scroller, createFooter());
    }

    private SideNav createNavigation() {
        SideNav nav = new SideNav();
        if (accessChecker.hasAccess(AdminDashboardView.class)) {
            nav.addItem(new SideNavItem("Admin Dashboard", AdminDashboardView.class, LineAwesomeIcon.CHART_AREA_SOLID.create()));
        }
        if (accessChecker.hasAccess(SetupExamView.class)) {
            nav.addItem(new SideNavItem("Setup Exam", SetupExamView.class, LineAwesomeIcon.FILE_CONTRACT_SOLID.create()));
        }
        if (accessChecker.hasAccess(StudentDashboardView.class)) {
            nav.addItem(new SideNavItem("Student Dashboard", StudentDashboardView.class, LineAwesomeIcon.CHALKBOARD_TEACHER_SOLID.create()));
        }
        if (accessChecker.hasAccess(FileUploadView.class)) {
            nav.addItem(new SideNavItem("File Upload", FileUploadView.class, LineAwesomeIcon.FILE_UPLOAD_SOLID.create()));
        }
        if (accessChecker.hasAccess(AboutView.class)) {
            nav.addItem(new SideNavItem("About", AboutView.class, LineAwesomeIcon.INFO_SOLID.create()));
        }
        return nav;
    }

    private Footer createFooter() {
        Footer layout = new Footer();
        if ( authentication.getName() != "anonymousUser" ) {
            Avatar avatar = new Avatar(authentication.getName());
            avatar.setThemeName("xsmall");
            avatar.getElement().setAttribute("tabindex", "-1");
            avatar.getStyle().setMargin("10px");

            Icon icon = new Icon("lumo", "dropdown");
            icon.getStyle().setMargin("10px");

            MenuBar userMenu = new MenuBar();
            userMenu.setThemeName("tertiary-inline contrast");

            MenuItem userName = userMenu.addItem("");
            Div div = new Div();
            //<theme-editor-local-classname>
            div.addClassName("main-layout-div-1");
            div.add(avatar);
            div.add(authentication.getName());
            div.add(icon);
            div.getElement().getStyle().set("display", "flex");
            div.getElement().getStyle().set("align-items", "center");
            userName.add(div);
            userName.getSubMenu().addItem("Sign out", e -> {
                logout();
            });

            layout.add(userMenu);
        } else {
            Anchor loginLink = new Anchor("login", "Sign in");
            layout.add(loginLink);
        }

        return layout;
    }
    public void logout() {
        UI.getCurrent().getPage().setLocation(LOGOUT_SUCCESS_URL);
        SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
        logoutHandler.logout(VaadinServletRequest.getCurrent().getHttpServletRequest(), null, null);
    }
    // Method to extract Google token from OAuth2AuthenticationToken
    @Override
    protected void afterNavigation() {
        super.afterNavigation();
        viewTitle.setText(getCurrentPageTitle());
    }

    private String getCurrentPageTitle() {
        PageTitle title = getContent().getClass().getAnnotation(PageTitle.class);
        return title == null ? "" : title.value();
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
    }
    private void navigateBasedOnUserRole(String userRole) {
        if ("ADMIN".equals(userRole)) {
            UI.getCurrent().navigate(AdminDashboardView.class);
        } else if ("STUDENT".equals(userRole)) {
            UI.getCurrent().navigate(StudentDashboardView.class);
        } else {
            // Handle other roles or redirect to a default view
            UI.getCurrent().navigate(AboutView.class);
        }
    }
    private void sendServerPublicKeyToUser(GrantedAuthority grantedAuthority) {
        UI.getCurrent().getPage().executeJs("getServerPublic($0);", Base64.getEncoder().encodeToString(GenerateKeyPair.getInstanceKeyPair().getPublic().getEncoded()));
        System.out.println("Server Public : " + GenerateKeyPair.getInstanceKeyPair().getPublic());
        if ("ADMIN".equals(grantedAuthority.getAuthority())) {
            UI.getCurrent().navigate(AdminDashboardView.class);
            UI.getCurrent().getPage().executeJs("ns.getServerPublic($0)", GenerateKeyPair.getInstanceKeyPair().getPublic());
            System.out.println("Server Public : " + GenerateKeyPair.getInstanceKeyPair().getPublic());
        } else if ("USER".equals(grantedAuthority.getAuthority())){
            UI.getCurrent().navigate(StudentDashboardView.class);
            //send the server diffie hellman public key to user via execute JS functions
//            GenerateKeyPair.getInstanceKeyPair().getPublic();
            UI.getCurrent().getPage().executeJs("console.log(\"Server send a Key: \", $0);", GenerateKeyPair.getInstanceKeyPair().getPublic());
            System.out.println("Server Public : " + GenerateKeyPair.getInstanceKeyPair().getPublic());
        }
    }
}
