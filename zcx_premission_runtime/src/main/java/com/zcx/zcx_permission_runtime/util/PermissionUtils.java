package com.zcx.zcx_permission_runtime.util;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Process;
import android.content.pm.PackageManager;

import com.zcx.zcx_permission_runtime.ZcxPermission;
import com.zcx.zcx_permission_runtime.activity.PermissionRequestActivity;
import com.zcx.zcx_permission_runtime.listener.PermissionListener;


/**
 * author:  zhouchaoxiang
 * date:    2019/9/26
 * explain:
 */
public class PermissionUtils {

    /**
     * 检查是否都赋予权限
     *
     * @param grantResults grantResults
     * @return 所有都同意返回true 否则返回false
     */
    public static boolean verifyPermissions(int... grantResults) {
        if (grantResults.length == 0) return true;
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }


    public static boolean checkPermission(String permission) {
        return checkPermission(ZcxPermission.getInstance().getContext(), permission);

    }

    public static boolean checkPermissions(Context context, String... permissions) {
        if (permissions == null || permissions.length == 0) {
            throw new NullPointerException("The permission requested is empty");
        }
        for (String permission : permissions) {
            if (!checkPermission(context, permission)) {
                return false;
            }
        }
        return true;
    }

    public static boolean checkPermission(Context context, String permission) {
        return context.checkPermission(permission, Process.myPid(), Process.myUid()) == PackageManager.PERMISSION_GRANTED;
    }

    public static void requestPermissions(Context context,String[] permissions, int requestCode, PermissionListener listener) {
        if (permissions == null || permissions.length == 0) {
            throw new NullPointerException("The permission requested is empty");
        }
        PermissionRequestActivity.start(context, permissions, requestCode, listener);
    }

}
