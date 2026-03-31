package com.xueya.aspect;

import com.xueya.service.AuditLogService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
public class AuditLogAspect {

    @Autowired
    private AuditLogService auditLogService;

    // 定义切点：所有Controller层的方法
    @Pointcut("execution(* com.xueya.controller.*.*(..))")
    public void controllerPointcut() {}

    // 在方法成功执行后记录日志
    @AfterReturning(pointcut = "controllerPointcut()", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        // 暂时注释掉审计日志记录，避免访问不存在的表
        /*
        try {
            // 获取方法信息
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Method method = signature.getMethod();
            String methodName = method.getName();
            String className = joinPoint.getTarget().getClass().getSimpleName();
            
            // 根据方法名判断操作类型
            String operationType = getOperationType(methodName);
            String operationContent = className + "." + methodName;
            
            // 记录审计日志
            auditLogService.logOperation(operationType, operationContent);
            
        } catch (Exception e) {
            // 记录日志失败不影响主业务
            e.printStackTrace();
        }
        */
    }

    private String getOperationType(String methodName) {
        if (methodName.contains("login")) {
            return "login";
        } else if (methodName.contains("logout")) {
            return "logout";
        } else if (methodName.contains("add") || methodName.contains("create") || methodName.contains("save")) {
            return "add";
        } else if (methodName.contains("edit") || methodName.contains("update") || methodName.contains("modify")) {
            return "edit";
        } else if (methodName.contains("delete") || methodName.contains("remove")) {
            return "delete";
        } else if (methodName.contains("settings") || methodName.contains("config")) {
            return "settings";
        } else {
            return "other";
        }
    }
}
