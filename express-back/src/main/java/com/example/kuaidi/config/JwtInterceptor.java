package com.example.kuaidi.config;

import com.example.kuaidi.utils.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.HashMap;
import java.util.Map;

@Component
public class JwtInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // OPTIONS 请求放行
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        if (token == null || token.isEmpty()) {
            writeError(response, 401, "未登录，请先登录");
            return false;
        }

        try {
            if (jwtUtil.isTokenExpired(token)) {
                writeError(response, 401, "登录已过期，请重新登录");
                return false;
            }
            // 将用户信息存入 request
            Integer staffId = jwtUtil.getStaffId(token);
            String username = jwtUtil.getUsername(token);
            Integer role = jwtUtil.getRole(token);
            request.setAttribute("staffId", staffId);
            request.setAttribute("username", username);
            request.setAttribute("role", role);

            // 普通用户 (role=1) 只能访问自助取件、用户中心、通知相关接口
            if (role != null && role == 1) {
                String uri = request.getRequestURI();
                boolean allowed = uri.equals("/api/packages/outbound/self")
                        || uri.equals("/api/packages/query")
                        || uri.equals("/api/packages/confirm")
                        || uri.equals("/api/packages/my")
                        || uri.startsWith("/api/packages/outbound/self/")
                        || uri.startsWith("/api/user/")
                        || uri.equals("/api/notifications");
                if (!allowed) {
                    writeError(response, 403, "权限不足");
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            writeError(response, 401, "无效的登录凭证");
            return false;
        }
    }

    private void writeError(HttpServletResponse response, int code, String message) throws Exception {
        response.setStatus(code);
        response.setContentType("application/json;charset=UTF-8");
        Map<String, Object> result = new HashMap<>();
        result.put("code", code);
        result.put("message", message);
        result.put("data", null);
        response.getWriter().write(new ObjectMapper().writeValueAsString(result));
    }
}
