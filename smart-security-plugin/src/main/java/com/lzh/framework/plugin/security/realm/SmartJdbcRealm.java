package com.lzh.framework.plugin.security.realm;

import com.lzh.framework.base.helper.DataBaseHelper;
import com.lzh.framework.plugin.security.SecurityConfig;
import com.lzh.framework.plugin.security.password.Md5CredentialsMatcher;
import org.apache.shiro.realm.jdbc.JdbcRealm;

/**
 * 基于 Smart 的 JDBC Realm（需要提供相关 smart.plugin.security.jdbc.* 配置项）
 * Created by lizhuohang on 17/8/18.
 */
public class SmartJdbcRealm extends JdbcRealm{
    public SmartJdbcRealm(){
        super.setDataSource(DataBaseHelper.getDataSource());
        super.setAuthenticationQuery(SecurityConfig.getJdbcAuthcQuery());
        super.setUserRolesQuery(SecurityConfig.getJdbcRolesQuery());
        super.setPermissionsQuery(SecurityConfig.getJdbcPermissionsQuery());
        super.setPermissionsLookupEnabled(true);
        super.setCredentialsMatcher(new Md5CredentialsMatcher());
    }
}
