package com.lzh.framework.base;

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
                ClassHelper.class,
                BeanHelper.class,
                IocHelper.class,
                ControllerHelper.class
        };

        for (Class<?> cls : classList) {
            ClassUtil.loadClass(cls.getName(), false);
        }
    }
}
