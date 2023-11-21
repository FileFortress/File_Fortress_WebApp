//package edu.sltc.vaadin.security;
//
//import java.util.Collections;
//import java.util.List;
//
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import com.google.firebase.auth.FirebaseToken;
//
//import java.util.Optional;
//
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContext;
//import org.springframework.security.core.context.SecurityContextHolder;
//
//@Service
//public class UserDetailsServiceImpl implements UserDetailsService {
//
//
//    public UserDetailsServiceImpl() {
//    }
//
//    @Override
//    @Transactional
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//            return new org.springframework.security.core.userdetails.User(username, username,
//                    getAuthorities());
//    }
//
//    private static List<GrantedAuthority> getAuthorities() {
//        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + "ADMIN"));
//
//    }
//    public Optional<Authentication> getAuthentication() {
//        SecurityContext context = SecurityContextHolder.getContext();
//        return Optional.ofNullable(context.getAuthentication());
//    }
//    public Optional<FirebaseToken> get() {
//        Optional<Authentication> authentication = getAuthentication();
//        if(authentication.isEmpty()) {
//            return Optional.empty();
//        } else {
//            FirebaseToken token = (FirebaseToken) authentication.get().getDetails();
//            return Optional.ofNullable(token);
//        }
//    }
//
//}
