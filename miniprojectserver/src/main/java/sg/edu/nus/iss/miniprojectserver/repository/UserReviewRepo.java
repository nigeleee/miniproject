package sg.edu.nus.iss.miniprojectserver.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import sg.edu.nus.iss.miniprojectserver.entity.UserReview;

@Repository
public interface UserReviewRepo extends JpaRepository<UserReview, Long> {
    
}
