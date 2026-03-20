package com.xueya.controller;

import com.xueya.entity.Role;
import com.xueya.service.RoleService;
import com.xueya.service.PermissionService;
import com.xueya.entity.Permission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @Autowired
    private PermissionService permissionService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/school/{schoolId}")
    public List<Role> getRolesBySchoolId(@PathVariable Long schoolId) {
        return roleService.getRolesBySchoolId(schoolId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public Role createRole(@RequestBody Role role) {
        roleService.save(role);
        return role;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public Role updateRole(@PathVariable Long id, @RequestBody Role role) {
        role.setId(id);
        roleService.updateById(role);
        return role;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void deleteRole(@PathVariable Long id) {
        roleService.removeById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/permissions")
    public List<Permission> getAllPermissions() {
        return permissionService.getAllPermissions();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{roleId}/permissions")
    public void assignPermissions(@PathVariable Long roleId, @RequestBody List<Long> permissionIds) {
        roleService.assignPermissions(roleId, permissionIds);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{roleId}/permissions")
    public List<Long> getPermissionIdsByRoleId(@PathVariable Long roleId) {
        return roleService.getPermissionIdsByRoleId(roleId);
    }
}