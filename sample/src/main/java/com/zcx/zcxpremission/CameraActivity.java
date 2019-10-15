package com.zcx.zcxpremission;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.zcx.zcx_permission_runtime.annotation.NeedPermission;
import com.zcx.zcx_permission_runtime.annotation.PermissionBefore;
import com.zcx.zcx_permission_runtime.bean.PermissionBeforeBean;
import com.zcx.zcx_permission_runtime.bean.PermissionCanceledBean;
import com.zcx.zcx_permission_runtime.bean.PermissionDeniedBean;
import com.zcx.zcx_permission_runtime.listener.PermissionListener;

/**
 * A login screen that offers login via email/password.
 */
@NeedPermission(Manifest.permission.CAMERA)
public class CameraActivity extends Activity implements PermissionListener {
    private static final String TAG = "CameraActivity";

    private static final int CAMERA_ID = 0;

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
        mHasPermission =true;
        initCamera();
    }

    @Override
    public void onPermissionCanceled(PermissionCanceledBean bean) {

    }

    @Override
    public void onPermissionDenied(PermissionDeniedBean bean) {

    }

    @PermissionBefore
    public void before(final PermissionBeforeBean beforeBean){
        new android.support.v7.app.AlertDialog.Builder(this)
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
}

