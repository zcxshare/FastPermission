package com.zcx.zcx_permission_runtime.bean;

import android.content.Context;

import com.zcx.zcx_permission_runtime.annotation.NeedPermission;
import com.zcx.zcx_permission_runtime.aspect.PermissionBaseAspect;

import java.util.List;

/**
 * author:  zhouchaoxiang
 * date:    2019/10/7;
 * explain:
 */
public class PermissionCanceledBean extends PermissionBaseBean {
    private List<String> mCancelList;

    public PermissionCanceledBean(Context context, PermissionBaseAspect aspect, NeedPermission needPermission, List<String> cancelList) {
        super(context, aspect, needPermission);
        mCancelList = cancelList;
    }

    public void againRequest() {
        getAspect().requestPermission();
    }

    public int getRequestCode() {
        return getNeedPermission().requestCode();
    }

    public List<String> getCancelList() {
        return mCancelList;
    }
}
