package sg.edu.nus.iss.miniprojectserver.repository;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class JwtBlacklistRepo {

    @Autowired
    private StringRedisTemplate redisTemplate;

    public void addToBlacklist(String token, long expirationTimeMillis) {
        redisTemplate.opsForValue().set(token, "blacklisted", expirationTimeMillis, TimeUnit.MILLISECONDS);
    }

    public boolean isBlacklisted(String token) {
        return redisTemplate.hasKey(token);
    }
}
