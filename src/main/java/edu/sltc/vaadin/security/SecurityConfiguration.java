package edu.sltc.vaadin.security;

import com.vaadin.flow.spring.security.VaadinWebSecurity;
import edu.sltc.vaadin.data.PasswordGenerator;
import edu.sltc.vaadin.models.PasswordPool;
import edu.sltc.vaadin.views.login.LoginView;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.client.OAuth2LoginConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.io.IOException;
import java.util.ArrayList;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration extends VaadinWebSecurity {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(
                authorize -> authorize.requestMatchers(new AntPathRequestMatcher("/images/*.png")).permitAll());

        // Icons from the line-awesome addon
        http.authorizeHttpRequests(authorize -> authorize
                .requestMatchers(new AntPathRequestMatcher("/line-awesome/**/*.svg")).permitAll());

        http.authorizeHttpRequests(authorize ->{
            authorize.requestMatchers(new AntPathRequestMatcher("/**")).permitAll();
        });

        super.configure(http);
        setLoginView(http, LoginView.class, "/");
    }
 @Bean
    UserDetailsManager userDetailsManager(){
     PasswordPool.getInstance().setAdminPasswords(PasswordGenerator.bulkPasswordForExaminers(2));
        return new InMemoryUserDetailsManager(
                User.withUsername("nuyunpabasara457@gmail.com").password(passwordEncoder().encode("nuyun123")).roles("ADMIN").build(),
                User.withUsername("nuyunpabasara@gmail.com").password(passwordEncoder().encode( new ArrayList<>(PasswordPool.getInstance().getAdminPasswords()).get(0))).roles("ADMIN").build()
        );
    }

}
