package com.lzh.framework.plugin.security.password;

import com.lzh.framework.base.utils.CodecUtil;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;

/**
 * Created by lizhuohang on 17/8/18.
 */
public class Md5CredentialsMatcher implements CredentialsMatcher {
    @Override
    public boolean doCredentialsMatch(AuthenticationToken authenticationToken, AuthenticationInfo authenticationInfo) {
        // 获取从表单提交过来的密码明文，尚未通过 MD5 加密
        String submitted = String.valueOf(((UsernamePasswordToken) authenticationToken).getPassword());
        // 获取数据库中存储的密码，已经通过 MD5 加密过的
        String encrypted = String.valueOf(authenticationInfo.getCredentials());
        return CodecUtil.md5(submitted).equals(encrypted);
    }
}
