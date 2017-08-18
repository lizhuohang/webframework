package com.lzh.framework.plugin.security;

import org.apache.shiro.web.env.EnvironmentLoaderListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.FilterRegistration;
import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.HandlesTypes;
import java.util.Set;

/**
 * Smart Security 插件
 * Created by lizhuohang on 17/8/18.
 */
public class SmartSecurityPlugin implements ServletContainerInitializer {

    private static final Logger logger = LoggerFactory.getLogger(SmartSecurityPlugin.class);

    @Override
    public void onStartup(Set<Class<?>> c, ServletContext ctx) throws ServletException {
        logger.debug("****************************************************");
        logger.debug("**           initial security plugin...           **");
        logger.debug("****************************************************");
        // 设置初始化参数
        ctx.setInitParameter("shiroConfigLocations", "classpath:smart-security.ini");
        // 注册 Listener
        ctx.addListener(EnvironmentLoaderListener.class);
        // 注册 Filter
        FilterRegistration.Dynamic smartSecurityFilter = ctx
                .addFilter("SmartSecurityFilter", SmartSecurityFilter.class);
        smartSecurityFilter.addMappingForUrlPatterns(null, false, "/*");
    }
}
