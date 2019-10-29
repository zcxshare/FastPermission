package com.zcx.fastpermission;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;

import com.zcx.fast_permission_runtime.annotation.NeedPermission;


/**
 * author:  zhouchaoxiang
 * date:    2019/10/17
 * explain: 
 */
public    class NormalPresenter {
    @NeedPermission(Manifest.permission.CAMERA)
    public void onClick(Activity activity){
        FragmentManager fragmentManager = activity.getFragmentManager();
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
}
