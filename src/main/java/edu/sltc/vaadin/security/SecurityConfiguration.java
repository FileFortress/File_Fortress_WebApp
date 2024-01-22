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
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
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
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

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
        http.csrf(CsrfConfigurer ->
                CsrfConfigurer.ignoringRequestMatchers(
                        new MvcRequestMatcher(
                                new HandlerMappingIntrospector(), "/fortress/**")));
        super.configure(http);
        setLoginView(http, LoginView.class, "/");
    }
    @Bean
    UserDetailsManager userDetailsManager(){
     PasswordPool.getInstance().setAdminPasswords(PasswordGenerator.bulkPasswordForExaminers(2));
        return new InMemoryUserDetailsManager(
                User.withUsername("nuyunpabasara457@gmail.com").password(passwordEncoder().encode("nuyun123")).roles("ADMIN").build(),
//                User.withUsername("nuyun457@gmail.com").password(passwordEncoder().encode("harindu123")).roles("USER").build(),
                User.withUsername("nuyunpabasara@gmail.com").password(passwordEncoder().encode( new ArrayList<>(PasswordPool.getInstance().getAdminPasswords()).get(0))).roles("ADMIN").build()
//                ,User.withUsername("eviefyre4k@gmail.com").password(passwordEncoder().encode( new ArrayList<>(PasswordPool.getInstance().getAdminPasswords()).get(1))).roles("ADMIN").build()
                );
    }

}
