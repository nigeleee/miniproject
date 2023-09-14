package sg.edu.nus.iss.miniprojectserver.service;

import java.io.File;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;


@Service
public class S3Service {

    private final AmazonS3 s3;
    private final String bucketName = "vttp-miniproject";

    public S3Service(AmazonS3 s3) {
        this.s3 = s3;
    }

    public String uploadFile(MultipartFile file) {
        try {
            File tempFile = File.createTempFile(file.getOriginalFilename(), "");
            file.transferTo(tempFile);
            s3.putObject(new PutObjectRequest(bucketName, file.getOriginalFilename(), tempFile));
            tempFile.delete();
    
            String objectUrl = String.format("https://%s.s3.amazonaws.com/%s", bucketName, file.getOriginalFilename());

            return objectUrl;
            
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload file", e);
        }
    }
}

