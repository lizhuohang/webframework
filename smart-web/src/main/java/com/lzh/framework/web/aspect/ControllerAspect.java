package com.lzh.framework.web.aspect;

import com.lzh.framework.base.annotation.Aspect;
import com.lzh.framework.base.annotation.Controller;
import com.lzh.framework.base.proxy.AspectProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * Created by lizhuohang on 17/8/16.
 */
@Aspect(Controller.class)
public class ControllerAspect extends AspectProxy {

    private static final Logger logger = LoggerFactory.getLogger(ControllerAspect.class);

    private long begin;

    @Override
    public void before(Class<?> cls, Method method, Object[] params) throws Throwable {
        logger.debug("------------controller proxy begin------------");
        logger.debug(String.format("class: %s", cls.getName()));
        logger.debug(String.format("method: %s", method.getName()));
        begin = System.currentTimeMillis();
    }

    @Override
    public void after(Class<?> cls, Method method, Object[] params, Object result) throws Throwable {

        logger.debug(String.format("time: %dms",System.currentTimeMillis() - begin));
        logger.debug("------------ controller proxy end ------------");
    }
}
