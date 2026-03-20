package com.xueya.config;

import com.xueya.entity.OperationLog;
import com.xueya.service.OperationLogService;
import com.xueya.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Aspect
@Component
public class OperationLogAspect {

    @Autowired
    private OperationLogService operationLogService;
    
    @Autowired
    private JwtUtil jwtUtil;

    @Pointcut("execution(* com.xueya.controller.*Controller.*(..))")
    public void logPointcut() {}

    @Before("logPointcut()")
    public void beforeMethod(JoinPoint joinPoint) {
        // 可以在这里记录方法开始执行的时间等信息
    }

    @AfterReturning(pointcut = "logPointcut()", returning = "result")
    public void afterReturningMethod(JoinPoint joinPoint, Object result) {
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            
            // 从请求头中获取JWT token
            String token = request.getHeader("Authorization");
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
                
                try {
                    // 解析token获取用户信息
                    Long userId = jwtUtil.getUserIdFromToken(token);
                    String username = jwtUtil.getUsernameFromToken(token);
                    Long schoolId = jwtUtil.getSchoolIdFromToken(token);
                    
                    // 创建操作日志
                    OperationLog log = new OperationLog();
                    log.setUsername(username);
                    log.setOperation(joinPoint.getSignature().getName());
                    log.setIp(request.getRemoteAddr());
                    log.setUrl(request.getRequestURI());
                    log.setMethod(request.getMethod());
                    log.setParams(getParams(joinPoint));
                    log.setCreateTime(new Date());
                    
                    // 保存操作日志
                    operationLogService.save(log);
                } catch (Exception e) {
                    // JWT解析失败，不记录日志
                }
            }
        } catch (Exception e) {
            // 发生异常，不影响正常业务逻辑
        }
    }

    private String getParams(JoinPoint joinPoint) {
        Map<String, Object> params = new HashMap<>();
        Object[] args = joinPoint.getArgs();
        String[] parameterNames = ((org.aspectj.lang.reflect.MethodSignature) joinPoint.getSignature()).getParameterNames();
        
        for (int i = 0; i < args.length; i++) {
            if (parameterNames[i] != null) {
                params.put(parameterNames[i], args[i]);
            }
        }
        
        return params.toString();
    }
}