package com.zcx.fastpermission;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.zcx.fast_permission_runtime.annotation.NeedPermission;
import com.zcx.fast_permission_runtime.annotation.PermissionBefore;
import com.zcx.fast_permission_runtime.bean.PermissionBeforeBean;
import com.zcx.fast_permission_runtime.bean.PermissionCanceledBean;
import com.zcx.fast_permission_runtime.bean.PermissionDeniedBean;
import com.zcx.fast_permission_runtime.listener.PermissionListener;

import java.util.Arrays;
import java.util.List;

/**
 * A login screen that offers login via email/password.
 */
@NeedPermission(value = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_BACKGROUND_LOCATION},
        requestBefore = Manifest.permission.CAMERA, permissionCanceled = Manifest.permission.CAMERA,
        permissionDenied = Manifest.permission.CAMERA, isAllowExecution = true)
public class CameraActivity extends Activity implements PermissionListener {
    private static final String TAG = "CameraActivity";

    private static final int CAMERA_ID = 0;
    private static final int requestCode = 3;

    private CameraPreview preview;
    private Camera camera;

    private boolean mHasPermission = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_camera);
        Button btBack = findViewById(R.id.back);
        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (camera == null && mHasPermission) {
            initCamera();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        releaseCamera();
    }

    @Override
    public void onPermissionGranted() {
        mHasPermission = true;
        initCamera();
    }

    @Override
    public void onPermissionCanceled(PermissionCanceledBean bean) {
        List<String> cancelList = bean.getCancelList();
        if (cancelList.size() == 1 && Manifest.permission.ACCESS_BACKGROUND_LOCATION.equals(cancelList.get(0))) {
            bean.proceed();
            mHasPermission = true;
            initCamera();
        } else {
            new androidx.appcompat.app.AlertDialog.Builder(bean.getContext())
                    .setTitle("来自配置文件,我们需要相机权限,请同意")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            bean.againRequest();
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            bean.proceed();
                        }
                    })
                    .show();
        }
    }

    @Override
    public void onPermissionDenied(PermissionDeniedBean bean) {

    }

    @PermissionBefore(Manifest.permission.CAMERA)
    public void before(final PermissionBeforeBean beforeBean) {
        List<String> notPermissions = beforeBean.getNotPermissions();
        if (notPermissions.size() == 1 && Manifest.permission.ACCESS_BACKGROUND_LOCATION.equals(notPermissions.get(0))) {
            beforeBean.proceed(false);
            mHasPermission = true;
            initCamera();
        } else {
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
    }

    private void initCamera() {
        camera = getCameraInstance(CAMERA_ID);
        Camera.CameraInfo cameraInfo = null;

        if (camera != null) {
            // Get camera info only if the camera is available
            cameraInfo = new Camera.CameraInfo();
            Camera.getCameraInfo(CAMERA_ID, cameraInfo);
        }

        // Get the rotation of the screen to adjust the preview image accordingly.
        final int displayRotation = getWindowManager().getDefaultDisplay()
                .getRotation();


        FrameLayout preview = findViewById(R.id.camera_preview);
        preview.removeAllViews();

        if (this.preview == null) {
            // Create the Preview view and set it as the content of this Activity.
            this.preview = new CameraPreview(this, camera, cameraInfo, displayRotation);
        } else {
            this.preview.setCamera(camera, cameraInfo, displayRotation);
        }

        preview.addView(this.preview);
    }

    /** A safe way to get an instance of the Camera object. */
    public static Camera getCameraInstance(int cameraId) {
        Camera c = null;
        try {
            c = Camera.open(cameraId); // attempt to get a Camera instance
        } catch (Exception e) {
            // Camera is not available (in use or does not exist)
            Log.d(TAG, "Camera " + cameraId + " is not available: " + e.getMessage());
        }
        return c; // returns null if camera is unavailable
    }

    private void releaseCamera() {
        if (camera != null) {
            camera.release();        // release the camera for other applications
            camera = null;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.w(TAG, "onRequestPermissionsResult: requestCode:"+requestCode+"--permissions:"+ Arrays.toString(permissions) +"--grantResults:"+ Arrays.toString(grantResults));
    }
}

