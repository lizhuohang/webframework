package com.lzh.framework.base.helper;

import com.lzh.framework.base.annotation.Aspect;
import com.lzh.framework.base.proxy.AspectProxy;
import com.lzh.framework.base.proxy.Proxy;
import com.lzh.framework.base.proxy.ProxyManager;
import com.lzh.framework.base.proxy.TransactionProxy;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by lizhuohang on 17/8/16.
 */
public final class AopHelper {

    static {
        try {
            Map<Class<?>, Set<Class<?>>> proxyMap = createProxyMap();
            Map<Class<?>, List<Proxy>> targetMap = createTargetMap(proxyMap);
            for (Class<?> targetClass : targetMap.keySet()) {
                Object proxy = ProxyManager.createProxy(targetClass, targetMap.get(targetClass));
                BeanHelper.setBean(targetClass, proxy);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取带有 Aspect 注解的所有类
     *
     * @param aspect
     * @return
     * @throws Exception
     */
    private static Set<Class<?>> createTargetClassSet(Aspect aspect) throws Exception {
        Set<Class<?>> targetClassSet = new HashSet<>();
        Class<? extends Annotation> annotation = aspect.value();
        if (annotation != null && !annotation.equals(Aspect.class)) {
            targetClassSet.addAll(ClassHelper.getClassSetByAnnotation(annotation));
        }
        return targetClassSet;
    }

    /**
     * @return
     * @throws Exception
     */
    private static Map<Class<?>, Set<Class<?>>> createProxyMap() throws Exception {
        Map<Class<?>, Set<Class<?>>> proxyMap = new HashMap<>();
        addAspectProxy(proxyMap);
        addTransactionProxy(proxyMap);
        return proxyMap;
    }

    /**
     * 建立目标类和其使用代理的关系
     * 一个目标类可能会存在多个代理
     *
     * @param proxyMap
     * @return
     * @throws Exception
     */
    private static Map<Class<?>, List<Proxy>> createTargetMap(Map<Class<?>, Set<Class<?>>> proxyMap) throws Exception {
        Map<Class<?>, List<Proxy>> targetMap = new HashMap<>();
        for (Class<?> proxyClass : proxyMap.keySet()) {
            Set<Class<?>> targetClassSet = proxyMap.get(proxyClass);
            for (Class<?> targetClass : targetClassSet) {
                Proxy proxy = (Proxy) proxyClass.newInstance();
                if (targetMap.containsKey(targetClass)) {
                    targetMap.get(targetClass).add(proxy);
                } else {
                    List<Proxy> proxyList = new ArrayList<>();
                    proxyList.add(proxy);
                    targetMap.put(targetClass, proxyList);
                }
            }
        }
        return targetMap;
    }

    /**
     * 加载普通切面代理类(@Aspect 注解)
     * <p>
     * 获取代理类与目标类之间的映射关系
     * <p>
     * 同时需要扩展 AspectProxy 抽象类 并 带有 Aspect 注解，才能跟代理建立关系
     * 扩展 AspectProx 是因为要指定前后执行的增强（Advise）
     * 带有 Aspect 注解 是为了有目标标注，也就是指明了带有指定标注的类都为目标类
     * <p>
     * 针对所有扩展了 AspectProxy 的类
     *
     * @param proxyMap
     * @throws Exception
     */
    private static void addAspectProxy(Map<Class<?>, Set<Class<?>>> proxyMap) throws Exception {
        Set<Class<?>> proxyClassSet = ClassHelper.getClassSetBySuper(AspectProxy.class);
        for (Class<?> proxyClass : proxyClassSet) {
            if (proxyClass.isAnnotationPresent(Aspect.class)) {
                Aspect aspect = proxyClass.getAnnotation(Aspect.class);
                Set<Class<?>> targetClassSet = createTargetClassSet(aspect);
                proxyMap.put(proxyClass, targetClassSet);
            }
        }
    }

    /**
     * 获取事务相关的注解类
     * 只针对一个 @Transaction 注解
     *
     * @param proxyMap
     * @throws Exception
     */
    private static void addTransactionProxy(Map<Class<?>, Set<Class<?>>> proxyMap) throws Exception {
        Set<Class<?>> serviceClassSet = ClassHelper.getServiceClassSet();
        proxyMap.put(TransactionProxy.class, serviceClassSet);
    }

}
