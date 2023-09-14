package sg.edu.nus.iss.miniprojectserver.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sg.edu.nus.iss.miniprojectserver.entity.UserReview;
import sg.edu.nus.iss.miniprojectserver.repository.UserReviewRepo;

@Service
public class UserReviewService {
    
    @Autowired
    private UserReviewRepo userReviewRepo;

    public List<UserReview> getAllReviews() {
        return userReviewRepo.findAll();
    }
}
