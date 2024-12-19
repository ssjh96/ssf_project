package sg.edu.nus.iss.ssf_project_potluck_shamus.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import sg.edu.nus.iss.ssf_project_potluck_shamus.service.CustomUserDetailsService;

@Configuration
@EnableWebSecurity 
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception 
    {
        // Customisation
        // For each request, if the requests is "/" or any url after /users/, leave unauthorised by using permitAll()
        // for main page and 
        http.authorizeHttpRequests(request -> request.requestMatchers("/","/users/**")
        .permitAll()
        

        // But for any other request, we require authentication, all other endpoints are secured.
        .anyRequest().authenticated())

        // specified login page url, permitAll() cause everyone should be able to access login page
        .formLogin(form -> form.loginPage("/users/login")
        .defaultSuccessUrl("/home")
        .failureUrl("/users/login?error=true")
        .permitAll())

        // specified logout page url, successful logout url, invalidate the httpsession upon logout, and all user able to logout 
        .logout(logout -> logout.logoutUrl("/users/logout")
        .logoutSuccessUrl("/")
        .invalidateHttpSession(true)
        .deleteCookies("JSESSIONID")
        .permitAll()); 

        return http.build();
    }
    
    @Bean
    public PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }

    @Bean // Spring should use the custom instead of predefined
    public UserDetailsService userDetailsService()
    {
        return new CustomUserDetailsService();
    }
    
    @Bean 
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception
    {
        AuthenticationManagerBuilder amb = http.getSharedObject(AuthenticationManagerBuilder.class);
        // Get authentication manager builder to build with (1) customUserDetailsService, & (2) BCryptPasswordEncoder
        amb.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder()); 
        
        
        return amb.build();
    }
}
