//package edu.sltc.vaadin.services;
//
//import com.vaadin.flow.server.ServiceInitEvent;
//import com.vaadin.flow.server.VaadinServiceInitListener;
//import com.vaadin.flow.server.VaadinSession;
//import org.springframework.stereotype.Component;
//
//import java.util.HashSet;
//import java.util.Set;
//@Component
//public class UserSessionListener implements VaadinServiceInitListener {
//    private static final Set<VaadinSession> connectedSessions = new HashSet<>();
//    @Override
//    public void serviceInit(ServiceInitEvent event) {
//        event.getSource().addSessionInitListener(sessionInitEvent -> {
//            VaadinSession session = sessionInitEvent.getSession();
//            connectedSessions.add(session);
//            System.out.println("Session Count : "+connectedSessions.size());
//            session.getService().addSessionDestroyListener(sessionDestroyEvent -> {
//                // Remove the user's email from the set when the session is destroyed
//                connectedSessions.remove(sessionDestroyEvent.getSession());
//            });
//        });
//    }
//    public Set<VaadinSession> getConnectedSessions() {
//        return connectedSessions;
//    }
//}
//
