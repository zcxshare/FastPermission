package com.zcx.fast_permission_runtime.aspect;

import android.content.Context;
import android.text.TextUtils;

import com.zcx.fast_permission_runtime.annotation.NeedPermission;
import com.zcx.fast_permission_runtime.annotation.PermissionBefore;
import com.zcx.fast_permission_runtime.annotation.PermissionCanceled;
import com.zcx.fast_permission_runtime.annotation.PermissionDenied;
import com.zcx.fast_permission_runtime.bean.PermissionBaseBean;
import com.zcx.fast_permission_runtime.bean.PermissionBeforeBean;
import com.zcx.fast_permission_runtime.bean.PermissionCanceledBean;
import com.zcx.fast_permission_runtime.bean.PermissionDeniedBean;
import com.zcx.fast_permission_runtime.exception.FastPermissionException;
import com.zcx.fast_permission_runtime.util.PermissionUtils;

import org.aspectj.lang.ProceedingJoinPoint;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

/**
 * author:  zhouchaoxiang
 * date:    2019/9/26
 * explain:
 */

public abstract class PermissionBaseAspect {

    protected ProceedingJoinPoint mJoinPoint;
    protected Context mContext;
    protected Object mObject;
    protected Method[] mMethods;
    protected NeedPermission mNeedPermission;


    public void requestPermission() {
        requestPermission(mContext, mJoinPoint, mNeedPermission, mObject, mMethods);
    }

    protected abstract void requestPermission(final Context context, final ProceedingJoinPoint joinPoint, final NeedPermission needPermission, final Object object, final Method[] methods);

    protected boolean executeBefore(Context context, Object object, Method[] methods, NeedPermission needPermission, String beforeKey) {
        for (Method method : methods) {
            PermissionBefore annotation = method.getAnnotation(PermissionBefore.class);
            if (annotation != null) {
                String value = annotation.value();
                List<String> notPermissions = PermissionUtils.getNotPermissions(context, needPermission.value());
                PermissionBeforeBean beforeBean = new PermissionBeforeBean(null, null, null, notPermissions);
                if (TextUtils.isEmpty(beforeKey) && TextUtils.isEmpty(value)) {
                    if (executeBeforeMethod(beforeBean, context, object, method)) return true;
                } else if (!TextUtils.isEmpty(beforeKey) && beforeKey.equals(value)) {
                    if (executeBeforeMethod(beforeBean, context, object, method)) return true;
                }
            }
        }
        return false;
    }

    protected boolean executeCanceled(Context context, Object object, Method[] methods, PermissionCanceledBean bean, String canceledKey) {
        for (Method method : methods) {
            PermissionCanceled annotation = method.getAnnotation(PermissionCanceled.class);
            if (annotation != null) {
                String value = annotation.value();
                if (TextUtils.isEmpty(canceledKey) && TextUtils.isEmpty(value)) {
                    if (executeCanceledMethod(bean, context, object, method)) return true;
                } else if (!TextUtils.isEmpty(canceledKey) && canceledKey.equals(value)) {
                    if (executeCanceledMethod(bean, context, object, method)) return true;
                }
            }
        }
        return false;
    }

    protected boolean executeDenied(Context context, Object object, Method[] methods, PermissionDeniedBean bean, String deniedKey) {
        for (Method method : methods) {
            PermissionDenied annotation = method.getAnnotation(PermissionDenied.class);
            if (annotation != null) {
                String value = annotation.value();
                if (TextUtils.isEmpty(deniedKey) && TextUtils.isEmpty(value)) {
                    if (executeDeniedMethod(bean, context, object, method)) return true;
                } else if (!TextUtils.isEmpty(deniedKey) && deniedKey.equals(value)) {
                    if (executeDeniedMethod(bean, context, object, method)) return true;
                }
            }
        }
        return false;
    }

    protected boolean executeBeforeMethod(PermissionBeforeBean bean, Context context, Object object, Method method) {
        bindInfo(bean, context);
        return executeMethod(bean, object, method,
                "The method parameters decorated by the PermissionBefore annotation can only be the PermissionBeforeBean");
    }

    protected boolean executeCanceledMethod(PermissionCanceledBean bean, Context context, Object object, Method method) {
        bindInfo(bean, context);
        return executeMethod(bean, object, method,
                "The method parameters decorated by the PermissionCanceled annotation can only be the PermissionCanceledBean");
    }

    protected boolean executeDeniedMethod(PermissionDeniedBean bean, Context context, Object object, Method method) {
        bindInfo(bean, context);
        return executeMethod(bean, object, method,
                "The method parameters decorated by the PermissionDenied annotation can only be the PermissionDeniedBean");
    }

    protected void bindInfo(PermissionBaseBean bean, Context context) {
        bean.setContext(context);
        bean.setAspect(this);
        bean.setNeedPermission(mNeedPermission);
    }

    protected <B extends PermissionBaseBean> boolean executeMethod(B bean, Object object, Method method, String exceptionPrompt) {
        Class<?>[] paramTypes = method.getParameterTypes();
        if (paramTypes.length != 1 || paramTypes[0] != bean.getClass()) {
            throw new FastPermissionException(exceptionPrompt);
        }
        try {
            method.invoke(object, bean);
            return true;
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return false;
    }

    protected void proceed(ProceedingJoinPoint joinPoint) {
        try {
            joinPoint.proceed();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    public abstract void proceed(boolean isRequest);

}
