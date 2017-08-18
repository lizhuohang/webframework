package com.lzh.framework.plugin.security.realm;

import com.lzh.framework.base.utils.CollectionUtil;
import com.lzh.framework.plugin.security.SecurityConstant;
import com.lzh.framework.plugin.security.SmartSecurity;
import com.lzh.framework.plugin.security.password.Md5CredentialsMatcher;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;

import java.util.HashSet;
import java.util.Set;

/**
 * 基于 Smart 的自定义 Realm（需要实现 SmartSecurity 接口）
 * Created by lizhuohang on 17/8/18.
 */
public class SmartCustomRealm extends AuthorizingRealm {
    private final SmartSecurity smartSecurity;

    public SmartCustomRealm(SmartSecurity smartSecurity) {
        this.smartSecurity = smartSecurity;
        super.setName(SecurityConstant.REALMS_CUSTOM);
        super.setCredentialsMatcher(new Md5CredentialsMatcher());
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        if (principalCollection == null) {
            throw new AuthorizationException("parameter principals is null");
        }
        // 获取已认证用户的用户名
        String username = (String) super.getAvailablePrincipal(principalCollection);

        // 通过 SmartSecurity 接口并根据用户名获取角色名集合
        Set<String> roleNameSet = smartSecurity.getRoleNameSet(username);

        // 通过 SmartSecurity 接口并根据角色名获取与其对应的权限名集合
        Set<String> permissionNameSet = new HashSet<>();
        if (CollectionUtil.isNotEmpty(roleNameSet)) {
            for (String roleName : roleNameSet) {
                Set<String> currentPermissionNameSet = smartSecurity.getPermissionNameSet(roleName);
                permissionNameSet.addAll(currentPermissionNameSet);
            }
        }

        // 将角色名集合与权限名集合放入 AuthorizationInfo 对象中，便于后续的授权操作
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        authorizationInfo.setRoles(roleNameSet);
        authorizationInfo.setStringPermissions(permissionNameSet);
        return authorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken)
            throws AuthenticationException {
        if (authenticationToken == null) {
            throw new AuthenticationException("parameter token is null");
        }

        // 通过 AuthenticationToken 对象获取从表单中提交过来的用户名
        String username = ((UsernamePasswordToken) authenticationToken).getUsername();

        // 通过 SmartSecurity 接口提供的方法，传入用户名获取数据库中加密后的密码
        String password = smartSecurity.getPassword(username);

        // 将用户名和数据库中加密后的密码放入 AuthenticationInfo 对象中，便于后续的认证操作
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo();
        authenticationInfo.setPrincipals(new SimplePrincipalCollection(username, super.getName()));
        authenticationInfo.setCredentials(password);
        return authenticationInfo;
    }
}
