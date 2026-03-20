package com.xueya.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xueya.entity.Role;

import java.util.List;

public interface RoleService extends IService<Role> {
    List<Role> getRolesBySchoolId(Long schoolId);
    void assignPermissions(Long roleId, List<Long> permissionIds);
    List<Long> getPermissionIdsByRoleId(Long roleId);
}