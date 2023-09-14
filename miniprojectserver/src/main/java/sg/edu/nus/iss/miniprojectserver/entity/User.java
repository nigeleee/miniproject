package sg.edu.nus.iss.miniprojectserver.entity;

import jakarta.persistence.*;
import lombok.Data;


@Entity
@Data
@Table(name="user")
public class User {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long userId;
    private String firstName;
    private String lastName;
    private String phone;
    private String address;
    private String password;

    @Column(unique = true)
    private String email;
    private boolean enabled = false;
    
        
}

