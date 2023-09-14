package sg.edu.nus.iss.miniprojectserver.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import sg.edu.nus.iss.miniprojectserver.service.UserDetailsSvc;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtFilter jwtFilter;
    @Autowired
    private UserDetailsSvc userDetailsSvc;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsSvc).passwordEncoder(passwordEncoder());
    }

    // private static final String[] WHITE_LIST_URLS = {
    //         "/hello",
    //         "/api/login",
    //         "/api/register",
    //         "/api/verifyRegistration",
    //         "/api/guest/add", 
    //         "/api/logout",
    //         "/api/success",
    //         "/api/logout/oauth2",
    //         "/api/products",
    //         "api/product/{id}",
    //         "/api/oauth2/authorization/google"

    // };

    @Bean
    SecurityFilterChain jwtSecurityFilterChain(HttpSecurity http) throws Exception {
             http
                .csrf()
                .disable()
                .cors()
                .and()
                .authorizeHttpRequests() 
                // .requestMatchers("/api/user/**").hasRole("USER")
                // .requestMatchers("/api/admin/**").hasRole("ADMIN")
                // .requestMatchers("api/logout").authenticated() //must be authenticated
                .anyRequest().permitAll()
                .and()
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .oauth2Login()
                .successHandler(Oauth2Handler());
                // .defaultSuccessUrl("/api/success", true);

            return http.build();
    }

      @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(11);
    }

    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return new ProviderManager(Arrays.asList(daoAuthenticationProvider()));
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(userDetailsSvc);
        return provider;
    }

    @Bean
    public Oauth2Handler Oauth2Handler() {
        return new Oauth2Handler();
    }

    

}