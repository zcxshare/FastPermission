package com.zcx.zcxpremission;

import android.app.Application;

import com.zcx.zcx_permission_runtime.ZcxPermission;
import com.zcx.zcxpremission.conf.ZcxPermissionConfig;

/**
 * author:  zhouchaoxiang
 * date:    2019/10/7
 * explain: 
 */
public    class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ZcxPermission.getInstance().init(getApplicationContext(),new ZcxPermissionConfig());
    }
}
