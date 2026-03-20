package com.xueya.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xueya.entity.Permission;

import java.util.List;

public interface PermissionService extends IService<Permission> {
    List<Permission> getAllPermissions();
    List<Permission> getPermissionsByRoleId(Long roleId);
}