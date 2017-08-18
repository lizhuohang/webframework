package com.lzh.framework.plugin.security;

import java.util.Set;

/**
 * Smart Security 接口
 * 可在应用中实现此接口，或者在 smart.properties 文件中提供以下基于 SQL 的配置项
 * smart.plugin.security.jdbc.authc_query：根据用户名获取密码
 * smart.plugin.security.jdbc.roles_query：根据用户名获取角色名集合
 * smart.plugin.security.jdbc.permissions_query：根据角色名获取权限名集合
 * Created by lizhuohang on 17/8/18.
 */
public interface SmartSecurity {
    /**
     * 根据用户名获取密码
     *
     * @param userName
     * @return
     */
    String getPassword(String userName);

    /**
     * 根据用户名获取角色名集合
     *
     * @param userName
     * @return
     */
    Set<String> getRoleNameSet(String userName);

    /**
     * 根据角色名获取权限名集合
     *
     * @param roleName
     * @return
     */
    Set<String> getPermissionNameSet(String roleName);
}
