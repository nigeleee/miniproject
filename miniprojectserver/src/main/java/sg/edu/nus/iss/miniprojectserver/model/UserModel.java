package sg.edu.nus.iss.miniprojectserver.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class UserModel {
    
    private String firstName;
    private String lastName;
    private String password;
    private String email;
    private String phone;
    private String address;
    private boolean matchingPassword;
        
}
