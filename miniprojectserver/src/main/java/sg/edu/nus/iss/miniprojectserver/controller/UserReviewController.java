package sg.edu.nus.iss.miniprojectserver.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import sg.edu.nus.iss.miniprojectserver.entity.User;
import sg.edu.nus.iss.miniprojectserver.entity.UserReview;
import sg.edu.nus.iss.miniprojectserver.repository.UserReviewRepo;
import sg.edu.nus.iss.miniprojectserver.service.S3Service;
import sg.edu.nus.iss.miniprojectserver.service.UserReviewService;
import sg.edu.nus.iss.miniprojectserver.service.UserServiceInterface;
import sg.edu.nus.iss.miniprojectserver.service.WebSocketHandlerService;

@RestController
@RequestMapping(path="/api")
public class UserReviewController {
    
    @Autowired
    private S3Service s3Service;

    @Autowired
    private UserReviewRepo reviewRepo;

    @Autowired
    private UserServiceInterface userServiceInterface;

    @Autowired
    private UserReviewService userReviewService;

    @Autowired
    private WebSocketHandlerService webSocketHandlerService;

    @PostMapping(path="/submit-review", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> submitReview(@RequestParam("review") String reviewText, @RequestParam("imageUrl") MultipartFile image, HttpSession session, HttpServletRequest request) throws IOException {

        User currentUser = userServiceInterface.getCurrentUser(session);

        if (currentUser == null) {
            return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
            
        }

        String url = s3Service.uploadFile(image);
        // System.out.println(">>>>>>>>>>>>>>>>> image url is" + url);

        // Create a new Review entity and save it to MySQL
        UserReview review = new UserReview();
        review.setFirstName(currentUser.getFirstName());
        review.setLastName(currentUser.getLastName());
        review.setText(reviewText);
        review.setImageUrl(url);
        reviewRepo.save(review);

        String email = currentUser.getEmail();
        // System.out.println("------------------------------>" + email);

        webSocketHandlerService.sendNotificationToUser(email, "Your review has been submitted.");

        JsonObject obj = Json.createObjectBuilder()
            .add("review", review.toString())
            .add("imageUrl", url)
            .build();

        return new ResponseEntity<String>(obj.toString(), HttpStatus.OK); 
        
    }
    

    @GetMapping(path = "/reviews", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserReview>> getAllReviews() {


        List<UserReview> result = userReviewService.getAllReviews();

        if (result != null) {
            return ResponseEntity.ok().body(result);

        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

