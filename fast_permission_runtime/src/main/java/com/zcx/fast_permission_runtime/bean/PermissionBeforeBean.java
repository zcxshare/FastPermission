package com.zcx.fast_permission_runtime.bean;

import android.content.Context;

import com.zcx.fast_permission_runtime.annotation.NeedPermission;
import com.zcx.fast_permission_runtime.aspect.PermissionBaseAspect;

import java.util.List;

/**
 * author:  zhouchaoxiang
 * date:    2019/9/29
 * explain:
 */
public class PermissionBeforeBean extends PermissionBaseBean {
    private List<String> mNotPermissions;
    public PermissionBeforeBean(Context context, PermissionBaseAspect aspect, NeedPermission needPermission,List<String> notPermissions) {
        super(context, aspect, needPermission);
        mNotPermissions =notPermissions;
    }

    public void proceed(boolean isRequest) {
        getAspect().proceed(isRequest);
    }

    public List<String> getNotPermissions() {
        return mNotPermissions;
    }
}
