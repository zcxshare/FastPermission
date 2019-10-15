package com.zcx.zcx_permission_runtime.exception;
/**
 * author:  zhouchaoxiang
 * date:    2019/9/29
 * explain: 
 */
public    class ZcxPermissionException extends RuntimeException   {
    public ZcxPermissionException() {
    }

    public ZcxPermissionException(String message) {
        super(message);
    }

    public ZcxPermissionException(String message, Throwable cause) {
        super(message, cause);
    }

    public ZcxPermissionException(Throwable cause) {
        super(cause);
    }

    public ZcxPermissionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
