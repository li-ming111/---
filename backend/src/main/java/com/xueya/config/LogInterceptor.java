package com.xueya.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xueya.entity.OperationLog;
import com.xueya.service.OperationLogService;
import com.xueya.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;

@Component
public class LogInterceptor implements HandlerInterceptor {
    @Autowired
    private OperationLogService operationLogService;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 记录请求开始时间
        request.setAttribute("startTime", System.currentTimeMillis());
        return true;
    }
    
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 暂时注释掉操作日志记录，让服务能够正常启动
        /*
        // 记录操作日志
        OperationLog log = new OperationLog();
        
        // 获取用户信息
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            try {
                String username = jwtUtil.getUsernameFromToken(token);
                log.setUsername(username);
            } catch (Exception e) {
                log.setUsername("anonymous");
            }
        } else {
            log.setUsername("anonymous");
        }
        
        // 设置其他日志信息
        log.setOperation(getOperationName(request));
        log.setIp(getClientIp(request));
        log.setUrl(request.getRequestURI());
        log.setMethod(request.getMethod());
        log.setParams(getRequestParams(request));
        log.setStatus(response.getStatus());
        log.setCreateTime(new Date());
        
        if (ex != null) {
            log.setErrorMsg(ex.getMessage());
        }
        
        // 保存日志
        operationLogService.save(log);
        */
    }
    
    // 获取操作名称
    private String getOperationName(HttpServletRequest request) {
        String uri = request.getRequestURI();
        if (uri.contains("/api/auth/login")) {
            return "用户登录";
        } else if (uri.contains("/api/school/add")) {
            return "添加学校";
        } else if (uri.contains("/api/school/update")) {
            return "更新学校";
        } else if (uri.contains("/api/school/delete")) {
            return "删除学校";
        } else if (uri.contains("/api/admin/create-admin")) {
            return "创建管理员";
        } else if (uri.contains("/api/admin/delete-user")) {
            return "删除用户";
        } else if (uri.contains("/api/resources/upload")) {
            return "上传资源";
        } else if (uri.contains("/api/system/backup")) {
            return "创建备份";
        } else {
            return "其他操作";
        }
    }
    
    // 获取客户端IP
    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
    
    // 获取请求参数
    private String getRequestParams(HttpServletRequest request) {
        StringBuilder params = new StringBuilder();
        
        // 获取URL参数
        request.getParameterMap().forEach((key, values) -> {
            for (String value : values) {
                params.append(key).append("=") .append(value).append("&");
            }
        });
        
        // 获取请求体参数
        if (request.getMethod().equals("POST") || request.getMethod().equals("PUT")) {
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()));
                String line;
                StringBuilder body = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    body.append(line);
                }
                if (body.length() > 0) {
                    params.append("body=") .append(body);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        return params.toString();
    }
}