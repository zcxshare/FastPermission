package com.zcx.fast_permission_runtime.bean;

import android.content.Context;

import com.zcx.fast_permission_runtime.annotation.NeedPermission;
import com.zcx.fast_permission_runtime.interfaces.RequestPermissionHandle;

import java.util.List;

/**
 * author:  zhouchaoxiang
 * date:    2019/10/7;
 * explain:
 */
public class PermissionCanceledBean extends PermissionBaseBean {
    private List<String> mCancelList;

    public PermissionCanceledBean(Context context, int requestCode, RequestPermissionHandle aspect, NeedPermission needPermission, List<String> cancelList) {
        super(context, requestCode,aspect, needPermission);
        mCancelList = cancelList;
    }

    public void proceed() {
        getHandle().proceed(false);
    }

    public void againRequest() {
        getHandle().proceed(true);
    }

    public int getRequestCode() {
        return getNeedPermission().requestCode();
    }

    public List<String> getCancelList() {
        return mCancelList;
    }
}
