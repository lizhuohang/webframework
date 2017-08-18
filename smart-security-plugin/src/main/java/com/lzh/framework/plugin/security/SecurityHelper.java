package com.lzh.framework.plugin.security;

import com.lzh.framework.base.utils.CastUtil;
import com.lzh.framework.plugin.security.bean.UserInfo;
import com.lzh.framework.plugin.security.exception.AuthcException;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Security 助手类
 * Created by lizhuohang on 17/8/18.
 */
public final class SecurityHelper {
    private static final Logger logger = LoggerFactory.getLogger(SecurityHelper.class);

    /**
     * 登陆
     *
     * @param userName
     * @param password
     * @throws AuthcException
     */
    public static void login(String userName, String password) throws AuthcException {
        Subject currentUser = SecurityUtils.getSubject();
        if (currentUser != null) {
            UsernamePasswordToken token = new UsernamePasswordToken(userName, password);
            try {
                currentUser.login(token);
            } catch (AuthenticationException e) {
                logger.error("login failure", e);
                throw new AuthcException(e);
            }
        }
    }

    public static void logout() {
        Subject currentUser = SecurityUtils.getSubject();
        if (currentUser != null) {
            currentUser.logout();
        }
    }

    public static UserInfo getUserInfo() {
        Subject currentUser = SecurityUtils.getSubject();
        if (currentUser != null) {
            Object obj = currentUser.getSession().getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
            String username = CastUtil.castString(obj);
            return new UserInfo(username);
        }
        return null;
    }
}
