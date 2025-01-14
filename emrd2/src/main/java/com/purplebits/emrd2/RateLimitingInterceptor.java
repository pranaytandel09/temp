package com.purplebits.emrd2;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;

@Component
public class RateLimitingInterceptor implements HandlerInterceptor {

    private final int maxRequestsPerMinute;
    private final Map<String, Bucket> bucketMap = new ConcurrentHashMap<>();

    public RateLimitingInterceptor(@Value("${app.maxRequestsPerMinute}") int maxRequestsPerMinute) {
        this.maxRequestsPerMinute = maxRequestsPerMinute;
    }
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        String ipAddress = request.getRemoteAddr();
        Bucket bucket = bucketMap.computeIfAbsent(ipAddress, key -> createNewBucket());

        if (bucket.tryConsume(1)) {
            return true;
        } else {
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.getWriter().write("Rate limit exceeded");
            return false; 
        }
    }

    private Bucket createNewBucket() {
        Bandwidth limit = Bandwidth.classic(maxRequestsPerMinute, Refill.greedy(maxRequestsPerMinute, Duration.ofMinutes(1)));
        return Bucket.builder().addLimit(limit).build();
    }
}
