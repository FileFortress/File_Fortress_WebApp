package edu.sltc.vaadin.security;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import org.springframework.stereotype.Service;

/**
 * @author Nuyun-Kalamullage
 * @IDE IntelliJ IDEA
 * @date 13/11/2023
 * @package edu.sltc.vaadin.security
 * @project_Name File_Fortress_WebApp
 */
@Service
public class FirebaseAuthenticationService {
    public FirebaseToken authenticateWithFirebase(String idToken) throws FirebaseAuthException {
        return FirebaseAuth.getInstance().verifyIdToken(idToken);
    }
}
