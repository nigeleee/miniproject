package sg.edu.nus.iss.miniprojectserver.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import sg.edu.nus.iss.miniprojectserver.entity.User;
import sg.edu.nus.iss.miniprojectserver.service.Oauth2Service;

public class Oauth2Handler implements AuthenticationSuccessHandler {

@Autowired
private Oauth2Service service;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {


        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
        OAuth2User oAuth2User = token.getPrincipal();
        String email = (String) oAuth2User.getAttributes().get("email");
        String firstName = (String) oAuth2User.getAttributes().get("given_name");
        String lastName = (String) oAuth2User.getAttributes().get("family_name");

        // Find or create the user.
        User user = service.findOrCreateUser(email, firstName, lastName);

        System.out.println("--------------------->User found: " + user);

        HttpSession session = httpServletRequest.getSession();
        session.setAttribute("OAUTH2_USER", user);
        session.setAttribute("OAUTH2_AUTHENTICATION", "SUCCESS");
        
        httpServletResponse.sendRedirect("https://kimchiproject-production.up.railway.app/#/products");
        // httpServletResponse.sendRedirect("http://localhost:4200/products");

    }

}
