package com.xueya.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("operation_log")
public class OperationLog {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String username;
    private String operation;
    private String ip;
    private String url;
    private String method;
    private String params;
    private Integer status;
    private String errorMsg;
    private Date createTime;
}