package com.zcx.fast_permission_runtime.bean;

import android.content.Context;

import com.zcx.fast_permission_runtime.annotation.NeedPermission;
import com.zcx.fast_permission_runtime.interfaces.RequestPermissionHandle;

import java.util.List;

/**
 * author:  zhouchaoxiang
 * date:    2019/9/29
 * explain:
 */
public class PermissionBeforeBean extends PermissionBaseBean {
    private List<String> mNotPermissions;
    public PermissionBeforeBean(Context context, int requestCode, RequestPermissionHandle aspect, NeedPermission needPermission, List<String> notPermissions) {
        super(context, requestCode,aspect, needPermission);
        mNotPermissions =notPermissions;
    }

    public void proceed(boolean isRequest) {
        getHandle().proceed(isRequest);
    }

    public List<String> getNotPermissions() {
        return mNotPermissions;
    }
}
