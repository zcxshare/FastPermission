package com.zcx.zcx_permission_runtime.bean;

import android.content.Context;

import com.zcx.zcx_permission_runtime.annotation.NeedPermission;
import com.zcx.zcx_permission_runtime.aspect.PermissionBaseAspect;

/**
 * author:  zhouchaoxiang
 * date:    2019/9/29
 * explain: 
 */
public    class PermissionBeforeBean extends PermissionBaseBean {

    public PermissionBeforeBean(Context context, PermissionBaseAspect aspect, NeedPermission needPermission) {
        super(context, aspect, needPermission);
    }

    public void proceed(boolean isRequest){
        getAspect().proceed(isRequest);
    }

}
