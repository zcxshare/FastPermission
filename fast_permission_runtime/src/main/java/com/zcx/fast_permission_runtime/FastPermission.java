package com.zcx.fast_permission_runtime;

import android.content.Context;

/**
 * author:  zhouchaoxiang
 * date:    2019/9/27
 * explain:
 */
public class FastPermission {
    private Context mContext;
    private Object mConfigObject;

    private FastPermission() {
    }

    public static FastPermission getInstance() {
        return Instance.instance;
    }

    private static class Instance {
        private static FastPermission instance = new FastPermission();
    }

    public void init(Context context, Object configObject) {
        mContext = context;
        mConfigObject = configObject;
    }

    public Context getContext() {
        return mContext;
    }

    public Object getConfigObject() {
        return mConfigObject;
    }

    public static void register(Object obj) {

    }

    public static void onRegister(Object obj) {

    }
}
