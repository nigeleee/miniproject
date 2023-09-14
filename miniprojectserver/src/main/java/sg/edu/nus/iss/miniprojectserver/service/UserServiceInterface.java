package sg.edu.nus.iss.miniprojectserver.service;

import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpSession;
import sg.edu.nus.iss.miniprojectserver.entity.User;
import sg.edu.nus.iss.miniprojectserver.model.UserModel;

@Service
public interface UserServiceInterface {

    User registerUser(UserModel userModel);

    void saveVerificationTokenForUser(String token, User user);

    String validateVerificaionToken(String token);

    User getCurrentUser(HttpSession session);

    User getUserByEmail(String email);

    boolean validateUserPassword(User user, String password);

    
}
