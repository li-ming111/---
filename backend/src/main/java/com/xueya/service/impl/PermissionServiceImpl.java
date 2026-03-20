package com.xueya.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xueya.entity.Permission;
import com.xueya.entity.RolePermission;
import com.xueya.mapper.PermissionMapper;
import com.xueya.mapper.RolePermissionMapper;
import com.xueya.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements PermissionService {

    @Autowired
    private RolePermissionMapper rolePermissionMapper;

    @Override
    public List<Permission> getAllPermissions() {
        return baseMapper.selectList(null);
    }

    @Override
    public List<Permission> getPermissionsByRoleId(Long roleId) {
        List<Long> permissionIds = rolePermissionMapper.selectList(null).stream()
                .filter(rp -> rp.getRoleId().equals(roleId))
                .map(RolePermission::getPermissionId)
                .collect(Collectors.toList());

        return permissionIds.stream()
                .map(baseMapper::selectById)
                .collect(Collectors.toList());
    }
}