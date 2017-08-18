package com.lzh.framework.plugin.security.exception;

import java.security.PrivilegedActionException;

/**
 * 认证异常（当非法访问时抛出）
 * Created by lizhuohang on 17/8/18.
 */
public class AuthcException extends Exception {
    public AuthcException() {
        super();
    }

    public AuthcException(String message) {
        super(message);
    }

    public AuthcException(String message, Throwable cause) {
        super(message, cause);
    }

    public AuthcException(Throwable cause) {
        super(cause);
    }
}
