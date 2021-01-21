package com.zcx.fast_permission_runtime.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Process;
import android.content.pm.PackageManager;
import android.provider.Settings;

import com.zcx.fast_permission_runtime.FastPermission;
import com.zcx.fast_permission_runtime.activity.PermissionRequestActivity;
import com.zcx.fast_permission_runtime.bean.PermissionBeforeBean;
import com.zcx.fast_permission_runtime.exception.FastPermissionException;
import com.zcx.fast_permission_runtime.interfaces.RequestPermissionHandle;
import com.zcx.fast_permission_runtime.listener.PermissionListener;

import java.util.ArrayList;
import java.util.List;


/**
 * author:  zhouchaoxiang
 * date:    2019/9/26
 * explain:
 */
public class PermissionUtils {
    public static final int REQUEST_TYPE_NORMAL = PermissionRequestActivity.REQUEST_TYPE_NORMAL;
    public static final int REQUEST_TYPE_INSTALL = PermissionRequestActivity.REQUEST_TYPE_INSTALL;

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
        return checkPermission(FastPermission.getInstance().getContext(), permission);

    }

    /**
     * @return 所有都同意返回true 否则返回false
     */
    public static boolean checkPermissions(Context context, String... permissions) {
        if (permissions == null || permissions.length == 0) {
            throw new FastPermissionException("The permission requested is empty");
        }
        for (String permission : permissions) {
            if (!checkPermission(context, permission)) {
                return false;
            }
        }
        return true;
    }

    public static boolean checkPermission(Context context, String permission) {
        if (permission.equals(Manifest.permission.REQUEST_INSTALL_PACKAGES)) {
            return checkInstallPermission(context);
        }
        return context.checkPermission(permission, Process.myPid(), Process.myUid()) == PackageManager.PERMISSION_GRANTED;
    }

    public static void requestPermissions(Context context, String[] permissions, int requestCode, PermissionListener listener) {
        requestPermissions(context, permissions, REQUEST_TYPE_NORMAL, requestCode, listener);
    }

    public static void requestPermissions(final Context context, final String[] permissions, final int requestType, final int requestCode, final PermissionListener listener) {
        if (permissions == null || permissions.length == 0) {
            throw new FastPermissionException("The permission requested is empty");
        }
        List<String> notPermissions = PermissionUtils.getNotPermissions(context, permissions);
        PermissionBeforeBean beforeBean = new PermissionBeforeBean(context, requestCode, new RequestPermissionHandle() {
            @Override
            public void proceed(boolean isRequest) {
                if (isRequest) {
                    PermissionRequestActivity.start(context, permissions, requestType, requestCode, listener);
                }
            }
        }, null, notPermissions);
        listener.onPermissionBefore(beforeBean);
    }

    public static List<String> getNotPermissions(Context context, String... permissions) {
        List<String> notPermissions = new ArrayList<>();
        if (permissions == null || permissions.length == 0) {
            throw new FastPermissionException("The permission requested is empty");
        }
        for (String permission : permissions) {
            if (!checkPermission(context, permission)) {
                notPermissions.add(permission);
            }
        }
        return notPermissions;
    }

    public static boolean checkInstallPermission(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return context.getPackageManager().canRequestPackageInstalls();
        }
        return true;
    }

    public static void toInstallPackageSetting(Context context, int requestCode, PermissionListener listener) {
        requestPermissions(context, new String[]{Manifest.permission.REQUEST_INSTALL_PACKAGES}, REQUEST_TYPE_INSTALL, requestCode, listener);
    }

    public static void toInstallPackageSetting(Activity activity, int requestCode) {
        Uri packageURI = Uri.parse("package:" + activity.getPackageName());
        Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, packageURI);
        activity.startActivityForResult(intent, requestCode);
    }

}
