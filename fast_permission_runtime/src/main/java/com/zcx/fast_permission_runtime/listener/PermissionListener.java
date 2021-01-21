package com.zcx.fast_permission_runtime.listener;

import com.zcx.fast_permission_runtime.bean.PermissionBeforeBean;
import com.zcx.fast_permission_runtime.bean.PermissionCanceledBean;
import com.zcx.fast_permission_runtime.bean.PermissionDeniedBean;

/**
 * author:  zhouchaoxiang
 * date:    2019/9/26
 * explain:
 */
public interface PermissionListener {
    void onPermissionBefore(PermissionBeforeBean bean);

    void onPermissionGranted();

    void onPermissionCanceled(PermissionCanceledBean bean);

    void onPermissionDenied(PermissionDeniedBean bean);
}
