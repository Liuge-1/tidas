package org.example.tidaswebmanagement.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.tidaswebmanagement.constant.BusinessConstants;
import org.example.tidaswebmanagement.pojo.Result;
import org.example.tidaswebmanagement.utils.JwtUtil;
import org.example.tidaswebmanagement.utils.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class TokenInterceptor implements HandlerInterceptor {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {
        String auth = request.getHeader("Authorization");
        if (auth == null || !auth.startsWith(BusinessConstants.TOKEN_PREFIX)) {
            response.setStatus(401);
            writeJson(response, Result.fail(BusinessConstants.MSG_NOT_LOGIN));
            return false;
        }

        String token = auth.substring(7);
        if (!jwtUtil.validateToken(token)) {
            response.setStatus(401);
            writeJson(response, Result.fail(BusinessConstants.MSG_TOKEN_INVALID));
            return false;
        }

        Integer userId = jwtUtil.getUserIdByToken(token);
        String role = jwtUtil.getRoleByToken(token);
        UserContext.setUserId(userId);
        UserContext.setRole(role);

        // 管理接口保护
        if (!BusinessConstants.ROLE_ADMIN.equals(role)) {
            String path = request.getRequestURI();
            String method = request.getMethod();
            if (isAdminRequired(path, method)) {
                response.setStatus(403);
                writeJson(response, Result.fail(BusinessConstants.MSG_NO_PERMISSION));
                return false;
            }
        }
        return true;
    }

    private boolean isAdminRequired(String path, String method) {
        if (path.startsWith("/operation-log")) return true;
        if (path.startsWith("/resignation") && isWrite(method)) return true;
        if (path.startsWith("/report")) return true;
        if (path.startsWith("/depts") && isWrite(method)) return true;
        if (path.startsWith("/notice") && isWrite(method)) return true;
        // 排除 /emps/password — 普通员工也需要修改密码
        if (path.startsWith("/emps/password")) return false;
        // 允许 PUT /emps — 员工修改自己的个人信息（POST/DELETE 仍需要管理员）
        if (path.startsWith("/emps") && isWrite(method) && !method.equals("PUT")) return true;
        return false;
    }

    private boolean isWrite(String method) {
        return "POST".equals(method) || "PUT".equals(method) || "DELETE".equals(method);
    }

    private void writeJson(HttpServletResponse response, Result result) throws Exception {
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(mapper.writeValueAsString(result));
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) {
        UserContext.remove();
    }
}
