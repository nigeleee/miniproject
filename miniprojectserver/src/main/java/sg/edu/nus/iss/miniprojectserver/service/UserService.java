package sg.edu.nus.iss.miniprojectserver.service;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpSession;
import sg.edu.nus.iss.miniprojectserver.entity.User;
import sg.edu.nus.iss.miniprojectserver.entity.VerificationToken;
import sg.edu.nus.iss.miniprojectserver.model.UserModel;
import sg.edu.nus.iss.miniprojectserver.repository.UserRepo;
import sg.edu.nus.iss.miniprojectserver.repository.VerificationTokenRepo;

@Service
public class UserService implements UserServiceInterface {
    
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private VerificationTokenRepo verificationTokenRepo;

    @Override
    public User registerUser(UserModel userModel) {
        
        if (userRepo.findByEmail(userModel.getEmail()) != null) {
            throw new RuntimeException("Email already registered");
        }

        User user = new User();
        user.setEmail(userModel.getEmail());
        user.setFirstName(userModel.getFirstName());
        user.setLastName(userModel.getLastName());
        user.setPhone(userModel.getPhone());
        user.setAddress(userModel.getAddress());
        user.setPassword(passwordEncoder.encode(userModel.getPassword()));
    
        userRepo.save(user);

        return user;
    }

    @Override
    public void saveVerificationTokenForUser(String token, User user) {
        VerificationToken verificationToken = new VerificationToken(user, token);

        verificationTokenRepo.save(verificationToken);
    }

    @Override
    public String validateVerificaionToken(String token) {
        VerificationToken verificationToken = verificationTokenRepo.findByToken(token);

        if (verificationToken == null) {
            return "invalid token";
        }

        User user = verificationToken.getUser();
        Calendar cal = Calendar.getInstance();

        if ((verificationToken.getExpirationTime().getTime() - cal.getTime().getTime()) <= 0) {
            verificationTokenRepo.delete(verificationToken);
            return "expired";
        }
        user.setEnabled(true);
        userRepo.save(user);
        return "valid";
    }

    @Override
    public User getCurrentUser(HttpSession session) {

        User user = (User) session.getAttribute("OAUTH2_USER");
    
        if (user != null) {
            return user;
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken)) {

            // System.out.println(">>>>>>>>>>>>>>>>>>>>> Authentication is successful.");
            
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetails) {
            
                UserDetails userDetails = (UserDetails) principal;
                String usernameFromPrincipal = userDetails.getUsername();
                

                User userFromRepo = userRepo.findByEmail(usernameFromPrincipal);
                // System.out.println(">>>>>>>>>>>>>>>>>>>>>>>> User fetched from repository: " + userFromRepo);

                return userFromRepo;
            } else {
                System.out.println("Principal is not an instance of UserDetails.");

            }
        }
        return null;
    }

    @Override
    public User getUserByEmail(String email) {
        if(email != null ) {
            return userRepo.findByEmail(email); 
        } else {
            throw new EntityNotFoundException("Email not found");
        }
    }
        

    @Override
    public boolean validateUserPassword(User user, String password) {
        String storedHash = user.getPassword(); 
        
        return passwordEncoder.matches(password, storedHash);

    }

    public User findOrCreateUser(String email, String name) {
        return null;
    }

}
