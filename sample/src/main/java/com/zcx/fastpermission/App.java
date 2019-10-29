package com.zcx.fastpermission;

import android.app.Application;

import com.zcx.fast_permission_runtime.FastPermission;
import com.zcx.fastpermission.conf.FastPermissionConfig;

/**
 * author:  zhouchaoxiang
 * date:    2019/10/7
 * explain:
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FastPermission.getInstance().init(getApplicationContext(), new FastPermissionConfig());
    }
}
