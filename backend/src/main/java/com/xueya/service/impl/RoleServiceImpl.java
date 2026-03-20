package com.xueya.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xueya.entity.Role;
import com.xueya.entity.RolePermission;
import com.xueya.mapper.RoleMapper;
import com.xueya.mapper.RolePermissionMapper;
import com.xueya.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Autowired
    private RolePermissionMapper rolePermissionMapper;

    @Override
    public List<Role> getRolesBySchoolId(Long schoolId) {
        return baseMapper.selectList(null).stream()
                .filter(role -> role.getSchoolId().equals(schoolId))
                .collect(Collectors.toList());
    }

    @Override
    public void assignPermissions(Long roleId, List<Long> permissionIds) {
        // 删除旧的权限关联
        rolePermissionMapper.selectList(null).stream()
                .filter(rp -> rp.getRoleId().equals(roleId))
                .forEach(rp -> rolePermissionMapper.deleteById(rp.getId()));

        // 添加新的权限关联
        for (Long permissionId : permissionIds) {
            RolePermission rolePermission = new RolePermission();
            rolePermission.setRoleId(roleId);
            rolePermission.setPermissionId(permissionId);
            rolePermissionMapper.insert(rolePermission);
        }
    }

    @Override
    public List<Long> getPermissionIdsByRoleId(Long roleId) {
        return rolePermissionMapper.selectList(null).stream()
                .filter(rp -> rp.getRoleId().equals(roleId))
                .map(RolePermission::getPermissionId)
                .collect(Collectors.toList());
    }
}