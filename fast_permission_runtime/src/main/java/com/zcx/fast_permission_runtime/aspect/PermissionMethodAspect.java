package com.zcx.fast_permission_runtime.aspect;

import android.content.Context;
import android.text.TextUtils;

import com.zcx.fast_permission_runtime.FastPermission;
import com.zcx.fast_permission_runtime.annotation.NeedPermission;
import com.zcx.fast_permission_runtime.bean.PermissionCanceledBean;
import com.zcx.fast_permission_runtime.bean.PermissionDeniedBean;
import com.zcx.fast_permission_runtime.exception.FastPermissionException;
import com.zcx.fast_permission_runtime.listener.PermissionListener;
import com.zcx.fast_permission_runtime.util.PermissionUtils;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * author:  zhouchaoxiang
 * date:    2019/10/15
 * explain:
 */
@Aspect
public class PermissionMethodAspect extends PermissionBaseAspect {

    @Around("execution(@com.zcx.fast_permission_runtime.annotation.NeedPermission * * (..)) " +
            "&& @annotation(com.zcx.fast_permission_runtime.annotation.NeedPermission)")
    public void requestPermissionAround(final ProceedingJoinPoint joinPoint) {
        mJoinPoint = joinPoint;
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        mNeedPermission = method.getAnnotation(NeedPermission.class);
        if (mNeedPermission == null) {
            throw new FastPermissionException("There is no NeedPermission annotation");
        }
        mObject = joinPoint.getThis();
        if (mObject == null) {
            throw new FastPermissionException("object is null");
        }

        if (mObject instanceof Context) {
            mContext = (Context) mObject;
        } else {
            try {
                Method getContext = mObject.getClass().getMethod("getContext");
                Object context = getContext.invoke(mObject);
                if (context instanceof Context) {
                    mContext = (Context) context;
                }else {
                    mContext = FastPermission.getInstance().getContext();
                }
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                mContext = FastPermission.getInstance().getContext();
            }
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
            Object configObject = FastPermission.getInstance().getConfigObject();
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
                String canceledKey = needPermission.permissionCanceled();
                boolean isExecuteCanceled;
                isExecuteCanceled = executeCanceled(context, object, methods, bean, canceledKey);
                if (!isExecuteCanceled) {
                    Object configObject = FastPermission.getInstance().getConfigObject();
                    if (configObject == null) return;
                    Class<?> aClass1 = configObject.getClass();
                    Method[] methods1 = aClass1.getMethods();
                    isExecuteCanceled = executeCanceled(context, configObject, methods1, bean, canceledKey);
                }
                if (needPermission.isAllowExecution() && !isExecuteCanceled) {
                    proceed(joinPoint);
                }
            }

            @Override
            public void onPermissionDenied(PermissionDeniedBean bean) {
                String deniedKey = needPermission.permissionDenied();
                boolean isExecuteDenied;
                isExecuteDenied = executeDenied(context, object, methods, bean, deniedKey);
                if (!isExecuteDenied) {
                    Object configObject = FastPermission.getInstance().getConfigObject();
                    if (configObject == null) return;
                    Class<?> aClass1 = configObject.getClass();
                    Method[] methods1 = aClass1.getMethods();
                    isExecuteDenied = executeDenied(context, configObject, methods1, bean, deniedKey);
                }
                if (needPermission.isAllowExecution() && !isExecuteDenied){
                    proceed(joinPoint);
                }
            }
        });
    }
}
