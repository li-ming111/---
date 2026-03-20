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

                // 映射角色到角色名称
                String roleName = "USER";
                if ("0".equals(role) || "super_admin".equals(role)) {
                    roleName = "SUPER_ADMIN";
                } else if ("school_admin".equals(role)) {
                    roleName = "SCHOOL_ADMIN";
                } else if ("1".equals(role) || "STUDENT".equals(role)) {
                    roleName = "STUDENT";
                }
                
                UserDetails userDetails = User.withUsername(username)
                        .password("password") // 密码在认证时已经验证，这里不需要
                        .authorities("ROLE_" + roleName)
                        .build();

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (Exception e) {
                // JWT 验证失败，继续处理请求，后续会被 SecurityConfig 中的配置拦截
            }
        }

        chain.doFilter(request, response);
    }
}