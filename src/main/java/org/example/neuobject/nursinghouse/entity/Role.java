package org.example.neuobject.nursinghouse.entity;


import java.util.Arrays;
import java.util.List;

/**
 * <p>Defines all possible role identity in the system</p>
 *
 * @author baige
 * @version 1.0
 * @date 2026/5/20 9:08
 */

public enum Role {
    //系统管理员——拥有所有权限
    ADMIN("系统管理员", Arrays.asList(Permission.values())),

    //健康管家——只拥有部分权限
    HEALTH_WORKER("健康管家", Arrays.asList(
            Permission.CUSTOMER_QUERY,
            Permission.NURSING_RECORD_QUERY,
            Permission.HEALTH_DAILY_CARE,
            Permission.HEALTH_APPLY_OUT,
            Permission.HEALTH_APPLY_QUIT
    ));

    private String role;
    private List<Permission> permissions;

    Role(String role, List<Permission> permissions) {
        this.role = role;
        this.permissions = permissions;
    }

    public String getRole() {
        return role;
    }

    //获取该角色拥有的所有权限
    public List<Permission> getPermissions() {
        return permissions;
    }

    //判断该角色是否有某个权限
    public boolean hasPermission(Permission permission) {
        return permissions.contains(permission);
    }
}
