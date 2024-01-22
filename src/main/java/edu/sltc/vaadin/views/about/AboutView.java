package edu.sltc.vaadin.views.about;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.theme.lumo.LumoUtility.Margin;
import edu.sltc.vaadin.views.MainLayout;

@PageTitle("About")
@Route(value = "about", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
@AnonymousAllowed
public class AboutView extends VerticalLayout {

    public AboutView() {
        setSpacing(false);

        H1 header = new H1("File Fortress Web Application");
        header.addClassNames(Margin.Top.SMALL, Margin.Bottom.MEDIUM);
        add(header);

        Image img = new Image("images/logo_placeholder.png", "Company Logo");
        img.setWidth("200px");
        add(img);

//        H3 subHeader = new H3("We are a File Transfer Application company passionate about File Sharing. Founded in 2023 by Nuyun Pabasara & Harindu Mansaka, our mission is to achieve Secure Fast Reliable File Transfer Platform");
//        subHeader.addClassNames(Margin.Top.XLARGE, Margin.Bottom.MEDIUM);
//        add(subHeader);

        Paragraph aboutUs = new Paragraph("We are a File Transfer Application company passionate about File Sharing.");
        aboutUs.addClassNames(Margin.Bottom.SMALL);
        add(aboutUs);

        Paragraph aboutUsTwo = new Paragraph("Founded by Nuyun Pabasara & Harindu Mansaka in 2023, Our mission is to achieve Secure Fast Reliable File Transfer Platform");
        aboutUsTwo.addClassNames(Margin.Bottom.LARGE);
        add(aboutUsTwo);

        Paragraph values = new Paragraph("| Secure 🔐 | Fast 🎢 | Reliable 🏆 |");
        values.addClassNames(Margin.Bottom.LARGE);
        add(values);

        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        getStyle().set("text-align", "center");
    }

}
