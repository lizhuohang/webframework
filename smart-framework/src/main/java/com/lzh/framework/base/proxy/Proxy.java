package com.lzh.framework.base.proxy;

/**
 * 代理接口
 * Created by lizhuohang on 17/8/15.
 */
public interface Proxy {
    /**
     * 代理链
     *
     * @param proxyChain
     * @return
     * @throws Throwable
     */
    Object doProxy(ProxyChain proxyChain) throws Throwable;
}
