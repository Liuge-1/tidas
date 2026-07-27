package org.example.tidaswebmanagement.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.example.tidaswebmanagement.anno.OperationLog;
import org.example.tidaswebmanagement.mapper.LoginMapper;
import org.example.tidaswebmanagement.service.OperationLogService;
import org.example.tidaswebmanagement.utils.UserContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;

/**
 * 操作日志切面 —— 自动采集增删改操作信息并持久化。
 * <p>
 * 只拦截 @OperationLog 注解的方法。环绕通知：
 * 业务正常 → 标记"成功"；抛异常 → 标记"失败"并记录错误信息。
 */
@Aspect
@Component
public class OperationLogAspect {

    private static final Logger log = LoggerFactory.getLogger(OperationLogAspect.class);

    @Autowired
    private OperationLogService operationLogService;
    @Autowired
    private LoginMapper loginMapper;
    @Autowired
    private ObjectMapper objectMapper; // Spring 自动配置的 ObjectMapper（已注册 JavaTimeModule）

    @Around("@annotation(opLog)")
    public Object around(ProceedingJoinPoint joinPoint, OperationLog opLog) throws Throwable {
        org.example.tidaswebmanagement.pojo.OperationLog entity =
                new org.example.tidaswebmanagement.pojo.OperationLog();

        // 1. 操作描述（从注解 value 读取）
        entity.setOperation(opLog.value());

        // 2. 操作人
        Integer userId = UserContext.getUserId();
        entity.setUserId(userId);
        entity.setUsername(resolveUsername(userId));

        // 3. 请求信息
        ServletRequestAttributes attrs =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs != null) {
            HttpServletRequest req = attrs.getRequest();
            entity.setUrl(req.getRequestURI());
            entity.setMethod(req.getMethod());
            entity.setIp(getClientIp(req));
        }

        // 4. 请求参数（JSON序列化，截断防止溢出）
        try {
            String json = objectMapper.writeValueAsString(joinPoint.getArgs());
            entity.setParams(json.length() > 5000 ? json.substring(0, 5000) + "..." : json);
        } catch (Exception e) {
            entity.setParams("序列化失败");
        }

        // 5. 时间
        entity.setCreateTime(LocalDateTime.now());

        // 6. 执行业务，区分成功/失败
        Object result;
        try {
            result = joinPoint.proceed();
            entity.setResult("成功");
        } catch (Throwable e) {
            entity.setResult("失败");
            String msg = e.getMessage();
            entity.setErrorMsg(msg != null && msg.length() > 2000 ? msg.substring(0, 2000) : msg);
            saveLogQuietly(entity);
            throw e; // 继续抛出，让全局异常处理器处理
        }

        saveLogQuietly(entity);
        return result;
    }

    /** 解析用户名：id==0 为内置管理员，否则查 emp 表 */
    private String resolveUsername(Integer userId) {
        if (userId == null) return "未知";
        if (userId == 0) return "管理员";
        try {
            String name = loginMapper.getUsernameById(userId);
            return name != null ? name : "用户" + userId;
        } catch (Exception e) {
            return "用户" + userId;
        }
    }

    /** 获取客户端真实IP */
    private String getClientIp(HttpServletRequest req) {
        String ip = req.getHeader("X-Forwarded-For");
        if (ip == null || ip.isBlank() || "unknown".equalsIgnoreCase(ip))
            ip = req.getHeader("X-Real-IP");
        if (ip == null || ip.isBlank() || "unknown".equalsIgnoreCase(ip))
            ip = req.getHeader("Proxy-Client-IP");
        if (ip == null || ip.isBlank() || "unknown".equalsIgnoreCase(ip))
            ip = req.getRemoteAddr();
        if (ip != null && ip.contains(","))
            ip = ip.split(",")[0].trim();
        return ip;
    }

    /** 防日志保存失败影响业务 */
    private void saveLogQuietly(org.example.tidaswebmanagement.pojo.OperationLog entity) {
        try {
            operationLogService.save(entity);
        } catch (Exception e) {
            log.error("操作日志入库失败", e);
        }
    }
}
