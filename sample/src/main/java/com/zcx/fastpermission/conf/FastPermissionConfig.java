package com.zcx.fastpermission.conf;

import android.Manifest;
import android.content.DialogInterface;

import com.zcx.fast_permission_runtime.annotation.PermissionBefore;
import com.zcx.fast_permission_runtime.annotation.PermissionCanceled;
import com.zcx.fast_permission_runtime.annotation.PermissionDenied;
import com.zcx.fast_permission_runtime.bean.PermissionBeforeBean;
import com.zcx.fast_permission_runtime.bean.PermissionCanceledBean;
import com.zcx.fast_permission_runtime.bean.PermissionDeniedBean;

/**
 * author:  zhouchaoxiang
 * date:    2019/10/8
 * explain:
 */
public class FastPermissionConfig {

    @PermissionBefore(Manifest.permission.CAMERA)
    public void before(final PermissionBeforeBean beforeBean) {
        new android.support.v7.app.AlertDialog.Builder(beforeBean.getContext())
                .setTitle("来自配置文件,我们需要相机权限来正常拍照")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        beforeBean.proceed(true);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        beforeBean.proceed(false);
                    }
                })
                .show();
    }

    @PermissionCanceled(Manifest.permission.CAMERA)
    public void cancel(final PermissionCanceledBean canceledBean) {
        new android.support.v7.app.AlertDialog.Builder(canceledBean.getContext())
                .setTitle("来自配置文件,我们需要相机权限,请同意")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        canceledBean.againRequest();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        canceledBean.proceed();
                    }
                })
                .show();
    }

    @PermissionDenied(Manifest.permission.CAMERA)
    public void denied(final PermissionDeniedBean deniedBean) {
        new android.support.v7.app.AlertDialog.Builder(deniedBean.getContext())
                .setTitle("来自配置文件,我们需要权限,是否设置")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deniedBean.toSettingActivity();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deniedBean.proceed();
                    }
                })
                .show();
    }

}
