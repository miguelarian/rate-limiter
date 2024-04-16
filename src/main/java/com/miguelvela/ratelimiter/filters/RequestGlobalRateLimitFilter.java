package com.miguelvela.ratelimiter.filters;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;

@Component
@Order(2)
public class RequestGlobalRateLimitFilter implements Filter {

    private final static String GLOBAL_REQUEST_COUNTER_KEY = "ratelimiter:global:counter";
    private final static int GLOBAL_REQUEST_COUNTER_TTL = 30; // seconds
    private final static int GLOBAL_REQUEST_COUNTER_MAX_REQUESTS = 10;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final Logger LOG = LoggerFactory.getLogger("RequestRateLimitFilter");

    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain) throws IOException, ServletException {

        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        Integer requestsCount = (Integer) redisTemplate.opsForValue().get(GLOBAL_REQUEST_COUNTER_KEY);
        if(requestsCount != null && requestsCount >= GLOBAL_REQUEST_COUNTER_MAX_REQUESTS) {
            httpServletResponse.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            httpServletResponse.getWriter().write("Too many requests");
            return;
        }

        redisTemplate.opsForValue().increment(GLOBAL_REQUEST_COUNTER_KEY);
        redisTemplate.expire(GLOBAL_REQUEST_COUNTER_KEY, Duration.ofSeconds(GLOBAL_REQUEST_COUNTER_TTL));

        LOG.info(GLOBAL_REQUEST_COUNTER_KEY + ": TTL extended " + GLOBAL_REQUEST_COUNTER_TTL + " seconds");

        chain.doFilter(request, response);
    }
}
