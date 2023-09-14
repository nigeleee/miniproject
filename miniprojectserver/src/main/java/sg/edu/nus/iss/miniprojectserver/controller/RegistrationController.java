package sg.edu.nus.iss.miniprojectserver.controller;

import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import sg.edu.nus.iss.miniprojectserver.entity.User;
import sg.edu.nus.iss.miniprojectserver.model.AuthenticationResponse;
import sg.edu.nus.iss.miniprojectserver.model.AuthenticationRequest;
import sg.edu.nus.iss.miniprojectserver.model.UserModel;
import sg.edu.nus.iss.miniprojectserver.service.EmailSenderService;
import sg.edu.nus.iss.miniprojectserver.service.JwtBlacklistService;
import sg.edu.nus.iss.miniprojectserver.service.UserDetailsSvc;
import sg.edu.nus.iss.miniprojectserver.service.UserService;
import sg.edu.nus.iss.miniprojectserver.service.JwtService;

@RestController
@RequestMapping("/api")
public class RegistrationController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsSvc userDetailsSvc;

    @Autowired
    private EmailSenderService emailSenderService;

    @Autowired
    private JwtBlacklistService blacklistService;

    // @Value("${redirect.login.url}")
    // private String redirectToLoginUrl;

    @PostMapping(path = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> registerUser(@RequestBody UserModel userModel, HttpServletRequest request) throws Exception {
        try {
            User user = userService.registerUser(userModel);

            String token = UUID.randomUUID().toString();
            userService.saveVerificationTokenForUser(token, user);

            emailSenderService.sendVerificationEmail(user, token, applicationUrl(request));         

        } catch (Exception e) {
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"message\":\"Email exists\"}");
        }

        return ResponseEntity.ok("{\"message\":\"Registered Successfully\"}");
    }

    @GetMapping("/verifyRegistration")
    public ResponseEntity<String> verifyRegistration(@RequestParam("token") String token)
            throws IOException {
        String result = userService.validateVerificaionToken(token);

        if (result.equalsIgnoreCase("valid")) {
            
            return ResponseEntity.ok(
                    "{\"message\":\"Email verified Successfully.\"}");

        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"message\":\"An error occured.\"}");
        }
    }

    private String applicationUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }

    
    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest)
            throws Exception {

        UserDetails userDetails = null;
        try {
            userDetails = userDetailsSvc.loadUserByUsername(authenticationRequest.getEmail());
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"message\":\"Email does not exist\"}");
        }

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(),
                            authenticationRequest.getPassword()));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"message\":\"Incorrect password\"}");
        }

        final String jwt = jwtService.generateToken(userDetails.getUsername());
     
        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }

    // traditional logout
    @PostMapping("/logout")
    public ResponseEntity<String> logoutJwt(HttpServletRequest request) {

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            // Assuming you can decode the token to get its expiration time
            long expirationTimeMillis = jwtService.getExpirationTimeMillisFromToken(token);
            blacklistService.addToBlacklist(token, expirationTimeMillis);
        }

        new SecurityContextLogoutHandler().logout(request, null, null);

        // JsonObject json = Json.createObjectBuilder()
        // .add("status", "success")
        // .add("message", "Logged Out")
        // .build();

        // return ResponseEntity.ok(json.toString());
        return ResponseEntity.ok("{\"message\":\"Logged Out\"}");

    }

}
