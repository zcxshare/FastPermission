package com.zcx.fast_permission_runtime.bean;

import android.content.Context;

import com.zcx.fast_permission_runtime.annotation.NeedPermission;
import com.zcx.fast_permission_runtime.interfaces.RequestPermissionHandle;


/**
 * author:  zhouchaoxiang
 * date:    2019/9/29
 * explain:
 */
public class PermissionBaseBean {
    private Context mContext;
    private int mRequestCode;
    private RequestPermissionHandle mHandle;
    private NeedPermission mNeedPermission;

    public PermissionBaseBean(Context context, int requestCode, RequestPermissionHandle handle, NeedPermission needPermission) {
        mContext = context;
        mRequestCode = requestCode;
        mHandle = handle;
        mNeedPermission = needPermission;
    }

    public Context getContext() {
        return mContext;
    }

    public void setContext(Context context) {
        mContext = context;
    }

    public RequestPermissionHandle getHandle() {
        return mHandle;
    }

    public void setHandle(RequestPermissionHandle handle) {
        mHandle = handle;
    }

    public NeedPermission getNeedPermission() {
        return mNeedPermission;
    }

    public void setNeedPermission(NeedPermission needPermission) {
        mNeedPermission = needPermission;
    }

    public String[] getPermissions() {
        return mNeedPermission.value();
    }

    public int getRequestCode() {
        return mRequestCode;
    }

    public void setRequestCode(int requestCode) {
        mRequestCode = requestCode;
    }
}
