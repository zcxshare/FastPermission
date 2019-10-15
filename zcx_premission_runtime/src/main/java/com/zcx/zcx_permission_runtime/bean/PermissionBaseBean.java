package com.zcx.zcx_permission_runtime.bean;

import android.content.Context;

import com.zcx.zcx_permission_runtime.annotation.NeedPermission;
import com.zcx.zcx_permission_runtime.aspect.PermissionBaseAspect;


/**
 * author:  zhouchaoxiang
 * date:    2019/9/29
 * explain: 
 */
public    class PermissionBaseBean {
    private Context mContext;
    private PermissionBaseAspect mAspect;
    private NeedPermission mNeedPermission;

    public PermissionBaseBean(Context context, PermissionBaseAspect aspect, NeedPermission needPermission) {
        mContext = context;
        mAspect = aspect;
        mNeedPermission = needPermission;
    }

    public Context getContext() {
        return mContext;
    }

    public void setContext(Context context) {
        mContext = context;
    }

    public PermissionBaseAspect getAspect() {
        return mAspect;
    }

    public void setAspect(PermissionBaseAspect aspect) {
        mAspect = aspect;
    }

    public NeedPermission getNeedPermission() {
        return mNeedPermission;
    }

    public void setNeedPermission(NeedPermission needPermission) {
        mNeedPermission = needPermission;
    }

    public String[] getPermissions(){
        return mNeedPermission.value();
    }
}
