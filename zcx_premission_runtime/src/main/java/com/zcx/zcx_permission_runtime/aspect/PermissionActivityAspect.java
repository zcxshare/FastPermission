package com.zcx.zcx_permission_runtime.aspect;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import com.zcx.zcx_permission_runtime.ZcxPermission;
import com.zcx.zcx_permission_runtime.annotation.NeedPermission;
import com.zcx.zcx_permission_runtime.bean.PermissionCanceledBean;
import com.zcx.zcx_permission_runtime.bean.PermissionDeniedBean;
import com.zcx.zcx_permission_runtime.listener.PermissionListener;
import com.zcx.zcx_permission_runtime.util.PermissionUtils;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

/**
 * author:  zhouchaoxiang
 * date:    2019/10/15
 * explain:
 */
@Aspect
public class PermissionActivityAspect extends PermissionBaseAspect {
    @Pointcut("execution(* android.app.Activity.onCreate(android.os.Bundle)) && @target(com.zcx.zcx_permission_runtime.annotation.NeedPermission)")
    public void pointcutOnActivityCreate() {
    }

    @Around("pointcutOnActivityCreate()")
    public void adviceOnActivityCreate(final ProceedingJoinPoint joinPoint) throws Throwable {
        mJoinPoint = joinPoint;
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        mNeedPermission = (NeedPermission) signature.getDeclaringType().getAnnotation(NeedPermission.class);
        mObject = joinPoint.getTarget();
        proceed(joinPoint);

        if (mObject instanceof Context) {
            mContext = (Context) mObject;
        } else {
            mContext = ZcxPermission.getInstance().getContext();
        }

        String[] permissions = mNeedPermission.value();
        if (PermissionUtils.checkPermissions(mContext, permissions)) {
            if (mObject instanceof PermissionListener) {
                ((PermissionListener) mObject).onPermissionGranted();
            }
            return;
        }

        Class<?> aClass = mObject.getClass();
        mMethods = aClass.getMethods();
        String beforeKey = mNeedPermission.requestBefore();
        boolean isExecuteBefore;
        isExecuteBefore = executeBefore(mContext, mObject, mMethods, mNeedPermission, beforeKey);
        if (!isExecuteBefore && !TextUtils.isEmpty(beforeKey)) {
            Object configObject = ZcxPermission.getInstance().getConfigObject();
            if (configObject != null) {
                Class<?> aClass1 = configObject.getClass();
                Method[] methods1 = aClass1.getMethods();
                isExecuteBefore = executeBefore(mContext, configObject, methods1, mNeedPermission, beforeKey);
            }
        }
        if (!isExecuteBefore) {
            requestPermission(mContext, joinPoint, mNeedPermission, mObject, mMethods);
        }
    }


    public void proceed(boolean isRequest) {
        if (isRequest) {
            requestPermission();
        } else if (!mNeedPermission.isAllowExecution() && mObject instanceof Activity) {
            ((Activity) mObject).finish();
        }
    }

    @Override
    protected void requestPermission(final Context context, final ProceedingJoinPoint joinPoint, final NeedPermission needPermission, final Object object, final Method[] methods) {
        PermissionUtils.requestPermissions(context, needPermission.value(), needPermission.requestCode(), new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                if (object instanceof PermissionListener) {
                    ((PermissionListener) object).onPermissionGranted();
                }
            }

            @Override
            public void onPermissionCanceled(PermissionCanceledBean bean) {
                if (object instanceof PermissionListener) {
                    ((PermissionListener) object).onPermissionCanceled(bean);
                }
                if (needPermission.isAllowExecution()) {
                    return;
                }
                String canceledKey = needPermission.permissionCanceled();
                boolean isExecuteCanceled;
                isExecuteCanceled = executeCanceled(context, object, methods, bean, canceledKey);
                if (!isExecuteCanceled) {
                    Object configObject = ZcxPermission.getInstance().getConfigObject();
                    if (configObject != null) {
                        Class<?> aClass1 = configObject.getClass();
                        Method[] methods1 = aClass1.getMethods();
                        isExecuteCanceled = executeCanceled(context, configObject, methods1, bean, canceledKey);
                    }
                }
                if (!isExecuteCanceled) {
                    finishActivity(object);
                }
            }

            @Override
            public void onPermissionDenied(PermissionDeniedBean bean) {
                if (object instanceof PermissionListener) {
                    ((PermissionListener) object).onPermissionDenied(bean);
                }
                if (needPermission.isAllowExecution()) {
                    return;
                }
                String deniedKey = needPermission.permissionDenied();
                boolean isExecuteDenied;
                isExecuteDenied = executeDenied(context, object, methods, bean, deniedKey);
                if (!isExecuteDenied) {
                    Object configObject = ZcxPermission.getInstance().getConfigObject();
                    if (configObject != null) {
                        Class<?> aClass1 = configObject.getClass();
                        Method[] methods1 = aClass1.getMethods();
                        isExecuteDenied = executeDenied(context, configObject, methods1, bean, deniedKey);
                    }
                }
                if (!isExecuteDenied) {
                    finishActivity(object);
                }
            }
        });
    }

    private void finishActivity(Object object) {
        if (object instanceof Activity) {
            ((Activity) object).finish();
        }
    }
}
