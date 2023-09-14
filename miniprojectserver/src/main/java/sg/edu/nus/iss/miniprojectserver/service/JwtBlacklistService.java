package sg.edu.nus.iss.miniprojectserver.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sg.edu.nus.iss.miniprojectserver.repository.JwtBlacklistRepo;

@Service
public class JwtBlacklistService {
    @Autowired
    private JwtBlacklistRepo repo;

    public void addToBlacklist(String token, long expirationTimeMillis) {
        repo.addToBlacklist(token, expirationTimeMillis);
    }

    public boolean isBlacklisted(String token) {
        return repo.isBlacklisted(token);
    }
}
