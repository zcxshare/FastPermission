package com.zcx.zcx_permission_runtime.annotation;

import android.Manifest;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * author:  zhouchaoxiang
 * date:    2019/9/25
 * explain:
 */
@Documented
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface NeedPermission {

    /**
     * PermissionUtils to apply for, for example {@link Manifest.permission#WRITE_EXTERNAL_STORAGE}
     */
    String[] value();


    int requestCode() default 0;

    /**
     * Whether to continue execution when permission is canceled or denied
     */
    boolean isAllowExecution() default false;

    /**
     * explain before request dialog
     */
    String requestBefore() default "";

    /**
     * When permission is denied
     */
    String permissionCanceled() default "";

    /**
     * When permission is denied and checked no longer reminded
     */
    String permissionDenied() default "";



}
