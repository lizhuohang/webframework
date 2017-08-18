package com.lzh.framework.web.security;


import com.lzh.framework.plugin.security.SmartSecurity;
import org.apache.commons.lang.StringUtils;

import java.util.Set;

/**
 * Created by lizhuohang on 17/8/18.
 */
public class AppSecurity implements SmartSecurity {
    /**
     * 根据用户名获取密码
     *
     * @param userName
     * @return
     */
    @Override
    public String getPassword(String userName) {
        String password = StringUtils.EMPTY;
        if (userName.equals("lzh")) {
            // 123456
            password = "e10adc3949ba59abbe56e057f20f883e";
        } else if (userName.endsWith("lmr")) {
            // abcdef
            password = "e80b5017098950fc58aad83c8c14978e";
        }
        return password;
    }

    /**
     * 根据用户名获取角色名集合
     *
     * @param userName
     * @return
     */
    @Override
    public Set<String> getRoleNameSet(String userName) {
        return null;
    }

    /**
     * 根据角色名获取权限名集合
     *
     * @param roleName
     * @return
     */
    @Override
    public Set<String> getPermissionNameSet(String roleName) {
        return null;
    }
}
