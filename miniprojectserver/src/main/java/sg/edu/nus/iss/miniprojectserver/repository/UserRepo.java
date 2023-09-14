package sg.edu.nus.iss.miniprojectserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import sg.edu.nus.iss.miniprojectserver.entity.User;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {

    User findByEmail(String email);
    
}
