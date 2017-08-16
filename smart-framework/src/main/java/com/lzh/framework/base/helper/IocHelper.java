package com.lzh.framework.base.helper;

import com.lzh.framework.base.annotation.Inject;
import com.lzh.framework.base.utils.CollectionUtil;
import com.lzh.framework.base.utils.ReflectionUtil;
import org.apache.commons.lang.ArrayUtils;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Set;

/**
 * Created by lizhuohang on 17/8/14.
 */
public final class IocHelper {
    static {
        // 获取所有 Bean 类与 Bean 实例之间的映射关系（简称 Bean Map）
        Map<Class<?>, Object> beanMap = BeanHelper.getBeanMap();
        if (CollectionUtil.isNotEmpty(beanMap)) {
            // 遍历 Bean Map
            for (Map.Entry<Class<?>, Object> beanEntry : beanMap.entrySet()) {
                // 从 BeanMap 中获取 Bean 类与 Bean 实例
                Class<?> beanClass = beanEntry.getKey();
                Object beanInstance = beanEntry.getValue();
                // 获取 Bean 类定义的所有成员变量
                Field[] beanFields = beanClass.getDeclaredFields();
                if (ArrayUtils.isNotEmpty(beanFields)) {
                    // 遍历BeanField
                    for (Field beanField : beanFields) {
                        // 判断当前BeanField是否带有Inject注解
                        if (beanField.isAnnotationPresent(Inject.class)) {
                            Class<?> beanFieldClass = beanField.getType();
                            // 通过接口类获取其子类的Set
                            Set<Class<?>> implClasses = ClassHelper.getClassSetBySuper(beanFieldClass);
                            Object beanFieldInstance = null;
                            if (CollectionUtil.isNotEmpty(implClasses)) {
                                // 子类不为空，从子类中挑选实现类进行实例注入
                                if (implClasses.size() == 1) {
                                    // 只有一个实现类
                                    beanFieldInstance = beanMap.get(implClasses.iterator().next());
                                } else {
                                    // 有多个实现类
                                }
                                if (beanFieldInstance != null) {
                                    // 通过反射初始化 BeanField 的值
                                    ReflectionUtil.setField(beanInstance, beanField, beanFieldInstance);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
