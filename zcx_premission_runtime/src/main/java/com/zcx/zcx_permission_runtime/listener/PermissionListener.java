package com.zcx.zcx_permission_runtime.listener;

import com.zcx.zcx_permission_runtime.bean.PermissionCanceledBean;
import com.zcx.zcx_permission_runtime.bean.PermissionDeniedBean;

/**
 * author:  zhouchaoxiang
 * date:    2019/9/26
 * explain: 
 */
public  interface PermissionListener  {
    void onPermissionGranted();

    void onPermissionCanceled(PermissionCanceledBean bean);

    void onPermissionDenied(PermissionDeniedBean bean);
}
