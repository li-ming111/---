package com.xueya.utils;

import java.util.HashMap;
import java.util.Map;

public class ResponseUtil {
    // 成功响应
    public static Map<String, Object> success() {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "操作成功");
        return response;
    }

    // 带消息的成功响应
    public static Map<String, Object> success(String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", message);
        return response;
    }

    // 带数据的成功响应
    public static Map<String, Object> success(String message, Object data) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", message);
        response.put("data", data);
        return response;
    }

    // 失败响应
    public static Map<String, Object> error(String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("message", message);
        return response;
    }

    // 带错误码的失败响应
    public static Map<String, Object> error(int code, String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("code", code);
        response.put("message", message);
        return response;
    }

    // 带数据的失败响应
    public static Map<String, Object> error(String message, Object data) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("message", message);
        response.put("data", data);
        return response;
    }
}