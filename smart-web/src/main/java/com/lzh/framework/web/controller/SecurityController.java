package com.lzh.framework.web.controller;

import com.lzh.framework.base.annotation.Action;
import com.lzh.framework.base.annotation.Controller;
import com.lzh.framework.base.bean.Param;
import com.lzh.framework.base.bean.View;
import com.lzh.framework.plugin.security.SecurityHelper;
import com.lzh.framework.plugin.security.annotation.User;
import com.lzh.framework.plugin.security.bean.UserInfo;
import com.lzh.framework.plugin.security.exception.AuthcException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by lizhuohang on 17/8/18.
 */
@User
@Controller
public class SecurityController {

    private static final Logger logger = LoggerFactory.getLogger(SecurityController.class);

    @Action("get:/")
    public View index() {
        UserInfo userInfo = SecurityHelper.getUserInfo();

        View view = new View("index.jsp");
        view.addModel("userInfo", userInfo.getUsername());
        return view;
    }

    @Action("get:/login")
    public View login() {
        return new View("login.jsp");
    }

    @Action("post:/login")
    public View loginSubmit(Param param) {
        String username = param.getString("username");
        String password = param.getString("password");
        try {
            SecurityHelper.login(username, password);
        } catch (AuthcException e) {
            logger.error("login failure", e);
            return new View("/login");
        }
        return new View("/customer");
    }

    @Action("get:/logout")
    public View logout() {
        SecurityHelper.logout();
        return new View("/");
    }
}
