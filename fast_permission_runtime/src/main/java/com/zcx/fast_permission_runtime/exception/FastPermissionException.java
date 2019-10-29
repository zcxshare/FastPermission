package com.zcx.fast_permission_runtime.exception;

/**
 * author:  zhouchaoxiang
 * date:    2019/9/29
 * explain:
 */
public class FastPermissionException extends RuntimeException {
    public FastPermissionException() {
    }

    public FastPermissionException(String message) {
        super(message);
    }

    public FastPermissionException(String message, Throwable cause) {
        super(message, cause);
    }

    public FastPermissionException(Throwable cause) {
        super(cause);
    }

    public FastPermissionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
