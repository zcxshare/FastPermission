package com.zcx.fast_permission_runtime.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.zcx.fast_permission_runtime.R;
import com.zcx.fast_permission_runtime.bean.PermissionCanceledBean;
import com.zcx.fast_permission_runtime.bean.PermissionDeniedBean;
import com.zcx.fast_permission_runtime.exception.FastPermissionException;
import com.zcx.fast_permission_runtime.listener.PermissionListener;
import com.zcx.fast_permission_runtime.util.PermissionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PermissionRequestActivity extends Activity {

    private static final String INTENT_REQUEST_CODE = "intent_request_code";
    private static final String INTENT_PERMISSIONS = "intent_permissions";

    private static final String REQUEST_TYPE = "request_type";

    public static final int REQUEST_TYPE_NORMAL = 1;
    public static final int REQUEST_TYPE_INSTALL = 2;

    private static PermissionListener sPermissionListener;
    private static Context sContext;

    private String[] mPermissions;
    private int mRequestCode;
    private int mRequestType;

    public static void start(Context context, String[] permissions, int requestType, int requestCode, PermissionListener listener) {
        sPermissionListener = listener;
        sContext = context;
        Intent intent = new Intent(context, PermissionRequestActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(INTENT_PERMISSIONS, permissions);
        intent.putExtra(INTENT_REQUEST_CODE, requestCode);
        intent.putExtra(REQUEST_TYPE, requestType);
        context.startActivity(intent);
        if (context instanceof Activity) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
                ((Activity) context).overridePendingTransition(0, 0);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission_request);

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= 19) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //虚拟键盘也透明
            //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }

        mPermissions = getIntent().getStringArrayExtra(INTENT_PERMISSIONS);
        mRequestCode = getIntent().getIntExtra(INTENT_REQUEST_CODE, 0);
        mRequestType = getIntent().getIntExtra(REQUEST_TYPE, REQUEST_TYPE_NORMAL);

        requestPermission();
    }

    private void requestPermission() {
        if (mPermissions == null || mPermissions.length == 0) {
            throw new FastPermissionException("The permission requested is empty");
        }
        if (mRequestType == REQUEST_TYPE_NORMAL) {
            requestNormalPermission();
        } else if (mRequestType == REQUEST_TYPE_INSTALL) {
            PermissionUtils.toInstallPackageSetting(this, mRequestCode);
        }
    }

    private void requestNormalPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (PermissionUtils.checkPermissions(this, mPermissions)) {
                if (sPermissionListener != null) {
                    sPermissionListener.onPermissionGranted();
                }
                finish();
                return;
            }
            requestPermissions(mPermissions, mRequestCode);
        } else {
            PackageManager packageManager = getPackageManager();
            String packageName = getPackageName();
            List<String> deniedList = new ArrayList<>();
            for (String mPermission : mPermissions) {
                int code = packageManager.checkPermission(mPermission, packageName);
                if (code != PackageManager.PERMISSION_GRANTED) {
                    deniedList.add(mPermission);
                }
            }
            if (sPermissionListener != null) {
                if (deniedList.size() == 0) {
                    sPermissionListener.onPermissionGranted();
                } else {
                    PermissionDeniedBean deniedBean = new PermissionDeniedBean(sContext, mRequestCode, null, null, null, deniedList);
                    sPermissionListener.onPermissionDenied(deniedBean);
                }
            }
            finish();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (PermissionUtils.verifyPermissions(grantResults)) {
            if (sPermissionListener != null) {
                sPermissionListener.onPermissionGranted();
            }
        } else {
            List<String> cancelList = new ArrayList<>();
            List<String> deniedList = new ArrayList<>();
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M || !shouldShowRequestPermissionRationale(permissions[i])) {
                        deniedList.add(permissions[i]);
                    } else {
                        cancelList.add(permissions[i]);
                    }
                }
            }
            if (sPermissionListener != null) {
                if (deniedList.size() > 0) {
                    PermissionDeniedBean deniedBean = new PermissionDeniedBean(sContext, mRequestCode, null, null, cancelList, deniedList);
                    sPermissionListener.onPermissionDenied(deniedBean);
                } else {
                    PermissionCanceledBean canceledBean = new PermissionCanceledBean(sContext, mRequestCode, null, null, cancelList);
                    sPermissionListener.onPermissionCanceled(canceledBean);
                }
            }
        }
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == mRequestCode) {
            if (sPermissionListener != null) {
                if (resultCode == RESULT_OK) {
                    sPermissionListener.onPermissionGranted();
                } else {
                    PermissionDeniedBean deniedBean = new PermissionDeniedBean(sContext, mRequestCode, null, null, null, Arrays.asList(mPermissions));
                    sPermissionListener.onPermissionDenied(deniedBean);
                }
            }
        }
        finish();
    }

    @Override
    public void finish() {
        super.finish();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
            overridePendingTransition(0, 0);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sPermissionListener = null;
        sContext = null;
    }

}
