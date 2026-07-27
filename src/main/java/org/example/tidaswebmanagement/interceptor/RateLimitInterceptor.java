package org.example.tidaswebmanagement.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.tidaswebmanagement.anno.RateLimit;
import org.example.tidaswebmanagement.pojo.Result;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 接口限流拦截器 — 基于 IP，使用滑动窗口记录每分钟请求次数
 */
@Component
public class RateLimitInterceptor implements HandlerInterceptor {

    private static final ObjectMapper mapper = new ObjectMapper();

    /** key: IP地址, value: 请求时间戳列表（毫秒） */
    private final Map<String, List<Long>> requestTimestamps = new ConcurrentHashMap<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {
        // 只对 Controller 方法生效
        if (!(handler instanceof HandlerMethod handlerMethod)) {
            return true;
        }

        RateLimit rateLimit = handlerMethod.getMethodAnnotation(RateLimit.class);
        if (rateLimit == null) {
            return true; // 无注解，不限流
        }

        int maxRequests = rateLimit.value();
        String ip = getClientIp(request);
        long now = System.currentTimeMillis();
        long windowStart = now - 60_000; // 1分钟前

        List<Long> timestamps = requestTimestamps.computeIfAbsent(ip, k -> new ArrayList<>());

        synchronized (timestamps) {
            // 移除1分钟之前的时间戳
            timestamps.removeIf(t -> t < windowStart);

            if (timestamps.size() >= maxRequests) {
                response.setContentType("application/json;charset=UTF-8");
                response.setStatus(429); // Too Many Requests
                response.getWriter().write(mapper.writeValueAsString(
                        Result.fail("请求过于频繁，请稍后再试")));
                return false;
            }

            timestamps.add(now);
            return true;
        }
    }

    /**
     * 获取客户端真实IP
     */
    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        // 如果通过多级代理，取第一个IP
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }
}
