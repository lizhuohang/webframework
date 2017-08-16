package com.lzh.framework.base.proxy;

import com.lzh.framework.base.annotation.Transaction;
import com.lzh.framework.base.helper.DataBaseHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * Created by lizhuohang on 17/8/16.
 */
public class TransactionProxy implements Proxy {

    private static final Logger logger = LoggerFactory.getLogger(TransactionProxy.class);

    private static final ThreadLocal<Boolean> FLAGE_HOLDER = new ThreadLocal<Boolean>() {
        @Override
        protected Boolean initialValue() {
            return false;
        }
    };

    /**
     * 代理链
     *
     * @param proxyChain
     * @return
     * @throws Throwable
     */
    @Override
    public Object doProxy(ProxyChain proxyChain) throws Throwable {
        Object result = null;
        boolean flag = FLAGE_HOLDER.get();
        Method method = proxyChain.getTargetMethod();
        // 由于上层实现是针对所有 @Service 的类进行代理，但是我们只对有 @Transaction 注解的方法执行事务过程，所以需要判断是否有注解
        // 类似于 AspectProxy 中的 intercept 方法的作用
        if (!flag && method.isAnnotationPresent(Transaction.class)) {
            FLAGE_HOLDER.set(true);
            try {
                DataBaseHelper.beginTransaction();
                logger.debug("[Transaction step] begin transaction");
                result = proxyChain.doProxyChain();
                DataBaseHelper.commitTransaction();
                logger.debug("[Transaction step] commit transaction");
            } catch (Exception e) {
                DataBaseHelper.rollbackTransaction();
                logger.debug("[Transaction step] rollback transaction , Method is {}.{} , params is {}",
                        proxyChain.getTargetClass().getSimpleName(), method.getName(), proxyChain.getMethodParams());
            } finally {
                FLAGE_HOLDER.remove();
            }
        } else {
            result = proxyChain.doProxyChain();
        }
        return result;
    }
}
