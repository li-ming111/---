package com.xueya.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("audit_log")
public class AuditLog {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 管理员ID
     */
    private Long adminId;
    
    /**
     * 管理员名称
     */
    private String adminName;
    
    /**
     * 操作类型：login-登录, logout-退出, add-添加, edit-编辑, delete-删除, settings-修改设置
     */
    private String operationType;
    
    /**
     * 操作内容
     */
    private String operationContent;
    
    /**
     * IP地址
     */
    private String ipAddress;
    
    /**
     * 用户代理
     */
    private String userAgent;
    
    /**
     * 请求URL
     */
    private String requestUrl;
    
    /**
     * 请求方法
     */
    private String requestMethod;
    
    /**
     * 请求参数
     */
    private String requestParams;
    
    /**
     * 响应状态码
     */
    private Integer responseStatus;
    
    /**
     * 执行时间（毫秒）
     */
    private Long executionTime;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
