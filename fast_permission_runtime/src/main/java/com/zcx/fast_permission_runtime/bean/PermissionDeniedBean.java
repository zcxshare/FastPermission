package com.zcx.fast_permission_runtime.bean;

import android.content.Context;

import com.zcx.fast_permission_runtime.annotation.NeedPermission;
import com.zcx.fast_permission_runtime.aspect.PermissionBaseAspect;
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

    public PermissionDeniedBean(Context context, PermissionBaseAspect aspect, NeedPermission needPermission, List<String> cancelList, List<String> deniedList) {
        super(context, aspect, needPermission);
        mCancelList = cancelList;
        mDeniedList = deniedList;
    }

    public void againRequest() {
        getAspect().requestPermission();
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
