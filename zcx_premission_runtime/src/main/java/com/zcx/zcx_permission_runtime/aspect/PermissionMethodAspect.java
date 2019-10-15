package com.zcx.zcx_permission_runtime.aspect;

import android.app.Fragment;
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
public    class PermissionMethodAspect extends PermissionBaseAspect {
    @Pointcut("execution(@com.zcx.zcx_permission_runtime.annotation.NeedPermission * * (..)) " +
            "&& @annotation(com.zcx.zcx_permission_runtime.annotation.NeedPermission)")
    public void requestPermissionPointcut() {
    }

    @Around("requestPermissionPointcut()")
    public void requestPermissionAround(final ProceedingJoinPoint joinPoint) {
        mJoinPoint = joinPoint;
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        mNeedPermission = method.getAnnotation(NeedPermission.class);
        if (mNeedPermission == null) {
            throw new NullPointerException("There is no NeedPermission annotation");
        }
        mObject = joinPoint.getThis();
        if (mObject == null) {
            throw new NullPointerException("object is null");
        }

        if (mObject instanceof Context) {
            mContext = (Context) mObject;
        } else if (mObject instanceof Fragment) {
            mContext = ((Fragment) mObject).getActivity();
        } else {
            mContext = ZcxPermission.getInstance().getContext();
        }
        String[] permissions = mNeedPermission.value();
        if (PermissionUtils.checkPermissions(mContext, permissions)) {
            proceed(joinPoint);
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
        } else if (mNeedPermission.isAllowExecution()) {
            proceed(mJoinPoint);
        }
    }

    protected void requestPermission(final Context context, final ProceedingJoinPoint joinPoint, final NeedPermission needPermission, final Object object, final Method[] methods) {
        PermissionUtils.requestPermissions(context, needPermission.value(), needPermission.requestCode(), new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                proceed(joinPoint);
            }

            @Override
            public void onPermissionCanceled(PermissionCanceledBean bean) {
                if (needPermission.isAllowExecution()) {
                    proceed(joinPoint);
                    return;
                }
                String canceledKey = needPermission.permissionCanceled();
                boolean isExecuteCanceled;
                isExecuteCanceled = executeCanceled(context, object, methods, bean, canceledKey);
                if (!isExecuteCanceled) {
                    Object configObject = ZcxPermission.getInstance().getConfigObject();
                    if (configObject == null) return;
                    Class<?> aClass1 = configObject.getClass();
                    Method[] methods1 = aClass1.getMethods();
                    executeCanceled(context, configObject, methods1, bean, canceledKey);
                }
            }

            @Override
            public void onPermissionDenied(PermissionDeniedBean bean) {
                if (needPermission.isAllowExecution()) {
                    proceed(joinPoint);
                    return;
                }
                String deniedKey = needPermission.permissionDenied();
                boolean isExecuteDenied;
                isExecuteDenied = executeDenied(context, object, methods, bean, deniedKey);
                if (!isExecuteDenied) {
                    Object configObject = ZcxPermission.getInstance().getConfigObject();
                    if (configObject == null) return;
                    Class<?> aClass1 = configObject.getClass();
                    Method[] methods1 = aClass1.getMethods();
                    executeDenied(context, configObject, methods1, bean, deniedKey);
                }
            }
        });
    }
}
