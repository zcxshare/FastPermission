package com.zcx.fastpermission;

import android.Manifest;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.zcx.fast_permission_runtime.annotation.NeedPermission;
import com.zcx.fast_permission_runtime.annotation.PermissionBefore;
import com.zcx.fast_permission_runtime.annotation.PermissionCanceled;
import com.zcx.fast_permission_runtime.annotation.PermissionDenied;
import com.zcx.fast_permission_runtime.bean.PermissionBeforeBean;
import com.zcx.fast_permission_runtime.bean.PermissionCanceledBean;
import com.zcx.fast_permission_runtime.bean.PermissionDeniedBean;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button mBtContacts;
    Button mBtStartActivity;
    Button mBtPresenter;
    NormalPresenter normalPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBtContacts = findViewById(R.id.button_contacts);
        mBtStartActivity = findViewById(R.id.button_start_activity);
        mBtPresenter = findViewById(R.id.button_presenter);
        mBtContacts.setOnClickListener(this);
        mBtStartActivity.setOnClickListener(this);
        mBtPresenter.setOnClickListener(this);
        normalPresenter = new NormalPresenter();
    }


    @Override
    public void onClick(View v) {
        if (v == mBtContacts) {
            onClickContacts();
        } else if (v == mBtStartActivity) {
            Intent intent = new Intent(this, CameraActivity.class);
            startActivity(intent);
        }else if (v == mBtPresenter){
            normalPresenter.onClick(this);
        }

    }

    @NeedPermission(value = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
            requestBefore = Manifest.permission.CAMERA, permissionCanceled = Manifest.permission.CAMERA,
            permissionDenied = Manifest.permission.CAMERA, isAllowExecution = true)
    private void onClickContacts() {
        FragmentManager fragmentManager = getFragmentManager();
        Fragment cameraFragment = fragmentManager.findFragmentByTag("camera");
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (cameraFragment == null) {
            fragmentTransaction.replace(R.id.sample_content_fragment, CameraPreviewFragment.newInstance(), "camera")
                    .addToBackStack("camera")
                    .commitAllowingStateLoss();
        } else {
            fragmentTransaction.show(cameraFragment);
        }
    }

    @PermissionBefore
    public void before(final PermissionBeforeBean beforeBean) {
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("我们需要相机权限来正常拍照")
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

    @PermissionCanceled
    public void cancel(final PermissionCanceledBean canceledBean) {
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("我们需要权限,是否同意一下")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        canceledBean.againRequest();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .show();
    }

    @PermissionDenied
    public void denied(final PermissionDeniedBean deniedBean) {
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("我们需要权限,是否设置")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deniedBean.toSettingActivity();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .show();
    }

}
