package com.zcx.zcx_permission_runtime.setting.support;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import com.zcx.zcx_permission_runtime.setting.ISetting;

/**
 * Created by mq on 2018/3/6 上午11:44
 * mqcoder90@gmail.com
 */

public class HuaWei implements ISetting{
    private Context mContext;

    public HuaWei(Context context) {
        mContext = context;
    }

    @Override
    public Intent getSetting() {
        Intent intent = new Intent(mContext.getPackageName());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ComponentName comp = new ComponentName("com.huawei.systemmanager", "com.huawei.permissionmanager.ui.MainActivity");
        intent.setComponent(comp);
        return intent;
    }
}
