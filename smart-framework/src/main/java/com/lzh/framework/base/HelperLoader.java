package com.lzh.framework.base;

import com.lzh.framework.base.helper.AopHelper;
import com.lzh.framework.base.helper.BeanHelper;
import com.lzh.framework.base.helper.ClassHelper;
import com.lzh.framework.base.helper.ControllerHelper;
import com.lzh.framework.base.helper.IocHelper;
import com.lzh.framework.base.utils.ClassUtil;

/**
 * 加载相应的Helper类
 * Created by lzh on 2017/8/14.
 */
public final class HelperLoader {
    public static void init() {
        Class<?>[] classList = {
                // 扫描所有的类，为下面的初始化做准备
                ClassHelper.class,
                // 获取所有的 Bean 类，包括Controller，Service 等等
                BeanHelper.class,
                // 加载 AOP 框架，扫描 @AspectProxy 注解，先构建代理到目标类的数据结构，再构建目标类到代理列表的数据结构
                // 然后使用 CGLib 生成目标类的动态代理类的实例，放入 BEAN_MAP 中
                // 此时会覆盖先前 BeanHelper 往 BEAN_MAP 中放入的部分 Controller 的相关信息
                AopHelper.class,
                // 初始化 IOC 框架，扫描 @Inject 注解，然后利用反射，向目标类的字段注入已经存在于 BEAN_MAP 中的实例
                IocHelper.class,
                // ControllerHelper 只是扫描所有 Controller 中的@Action，并生成 Request 和 Method 的对应关系
                // 并不会对 BeanHelper 中的 BEAN_MAP 有任何操作
                // 但是此时 BEAN_MAP 中的 Controller 实体已经是经过动态代理过的，所以上面的 Method 也是动态代理后的方法
                ControllerHelper.class
        };

        for (Class<?> cls : classList) {
            ClassUtil.loadClass(cls.getName(), true);
        }
    }
}
