package com.zcx.fast_permission_runtime.bean;

import android.content.Context;

import com.zcx.fast_permission_runtime.annotation.NeedPermission;
import com.zcx.fast_permission_runtime.aspect.PermissionBaseAspect;
import com.zcx.fast_permission_runtime.interfaces.RequestPermissionHandle;
import com.zcx.fast_permission_runtime.util.SettingUtils;

import java.util.List;

/**
 * author:  zhouchaoxiang
 * date:    2019/10/8
 * explain:
 */
public class PermissionDeniedBean extends PermissionBaseBean {

    private List<String> mCancelList;
    private List<String> mDeniedList;

    public PermissionDeniedBean(Context context, int requestCode, RequestPermissionHandle aspect, NeedPermission needPermission, List<String> cancelList, List<String> deniedList) {
        super(context, requestCode,aspect, needPermission);
        mCancelList = cancelList;
        mDeniedList = deniedList;
    }

    public void proceed() {
        getHandle().proceed(false);
    }

    public void againRequest() {
        getHandle().proceed(true);
    }

    public void toSettingActivity() {
        SettingUtils.go2Setting(getContext());
    }

    public int getRequestCode() {
        return getNeedPermission().requestCode();
    }

    public List<String> getCancelList() {
        return mCancelList;
    }

    public List<String> getDeniedList() {
        return mDeniedList;
    }
}
