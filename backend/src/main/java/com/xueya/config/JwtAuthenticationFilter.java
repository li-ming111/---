package com.xueya.config;

import com.xueya.utils.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    
    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");

        // 对于认证相关的路径，直接放行，不进行JWT验证
        String requestUri = request.getRequestURI();
        if (requestUri.startsWith("/api/auth/")) {
            chain.doFilter(request, response);
            return;
        }

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            try {
                Long userId = jwtUtil.getUserIdFromToken(token);
                String username = jwtUtil.getUsernameFromToken(token);
                String role = jwtUtil.getRoleFromToken(token);
                Long schoolId = jwtUtil.getSchoolIdFromToken(token);

                // 映射角色到角色名称
                String roleName = "USER";
                if ("0".equals(role) || "super_admin".equals(role)) {
                    roleName = "SUPER_ADMIN";
                } else if ("school_admin".equals(role)) {
                    roleName = "SCHOOL_ADMIN";
                } else if ("1".equals(role) || "STUDENT".equals(role)) {
                    roleName = "STUDENT";
                }
                
                // 为超级管理员添加ADMIN角色权限
                List<String> authorities = new ArrayList<>();
                authorities.add("ROLE_" + roleName);
                if ("SUPER_ADMIN".equals(roleName)) {
                    authorities.add("ROLE_ADMIN");
                }
                
                UserDetails userDetails = User.withUsername(username)
                        .password("password") // 密码在认证时已经验证，这里不需要
                        .authorities(authorities.toArray(new String[0]))
                        .build();

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                
                // 创建自定义的 details 对象，包含学校 ID
                Map<String, Object> details = new HashMap<>();
                details.put("schoolId", schoolId);
                details.put("userId", userId);
                details.put("role", roleName);
                authentication.setDetails(details);

                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (Exception e) {
                // JWT 验证失败，继续处理请求，后续会被 SecurityConfig 中的配置拦截
            }
        }

        chain.doFilter(request, response);
    }
}