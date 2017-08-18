package com.lzh.framework.plugin.security.aspect;

import com.lzh.framework.base.annotation.Aspect;
import com.lzh.framework.base.annotation.Controller;
import com.lzh.framework.base.proxy.AspectProxy;
import com.lzh.framework.plugin.security.annotation.User;
import com.lzh.framework.plugin.security.exception.AuthzException;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * Created by lizhuohang on 17/8/18.
 */
@Aspect(Controller.class)
public class AuthzAnnotationAspect extends AspectProxy {

    /**
     * 定义一个基于授权功能的注解类数组
     */
    private static final Class[] ANNOTATION_CLASS_ARRAY = {
            User.class
    };

    @Override
    public void before(Class<?> cls, Method method, Object[] params) throws Throwable {
        Annotation annotation = getAnnotation(cls, method);
        if (annotation != null) {
            // 判断授权注解的类型
            Class<?> annotationType = annotation.annotationType();
            if (annotationType.equals(User.class)) {
                handleUser();
            }
        }
    }

    private Annotation getAnnotation(Class<?> cls, Method method) {
        // 遍历所有的授权注解
        for (Class<? extends Annotation> annotationClass : ANNOTATION_CLASS_ARRAY) {
            // 首先判断目标方法上是否带有授权注解
            if (method.isAnnotationPresent(annotationClass)) {
                return method.getAnnotation(annotationClass);
            }
            // 然后判断目标类上是否带有授权注解
            if (cls.isAnnotationPresent(annotationClass)) {
                return cls.getAnnotation(annotationClass);
            }
        }
        // 若目标方法与目标类上均未带有授权注解，则返回空对象
        return null;
    }

    private void handleUser() {
        Subject currentUser = SecurityUtils.getSubject();
        PrincipalCollection principalCollection = currentUser.getPrincipals();
        if (principalCollection == null || principalCollection.isEmpty()) {
            throw new AuthzException("当前用户尚未登录");
        }
    }
}
