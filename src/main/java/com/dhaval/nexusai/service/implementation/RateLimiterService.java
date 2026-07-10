package com.dhaval.nexusai.service.implementation;

import com.dhaval.nexusai.config.RateLimitProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RateLimiterService {

    private final RedisTemplate <String , Object> redisTemplate;
    private final RateLimitProperties rateLimitProperties;

    public boolean allowRequest (String key) {

        int LIMIT = rateLimitProperties.getMaxRequests();
        int WINDOW_SECONDS = rateLimitProperties.getWindowSeconds();
        String redisKey = "rate_limit:" + key;

        long currentTime = System.currentTimeMillis();

        long windowStart = currentTime - WINDOW_SECONDS * 1000L;

        // Remove expired requests
        redisTemplate.opsForZSet()
                .removeRangeByScore(redisKey, 0, windowStart);

        // Count requests in current window
        Long requestCount =
                redisTemplate.opsForZSet().zCard(redisKey);

        if (requestCount != null && requestCount >= LIMIT) {
            return false;
        }

        // Save current request
        redisTemplate.opsForZSet()
                .add(redisKey,
                        String.valueOf(currentTime),
                        currentTime);

        // Auto cleanup
        redisTemplate.expire(
                redisKey,
                Duration.ofSeconds(WINDOW_SECONDS));

        return true;
    }

}
