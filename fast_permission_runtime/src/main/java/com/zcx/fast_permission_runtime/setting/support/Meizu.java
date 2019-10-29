package com.zcx.fast_permission_runtime.setting.support;
import android.content.Context;
import android.content.Intent;

import com.zcx.fast_permission_runtime.setting.ISetting;

/**
 * author:  zhouchaoxiang
 * date:    2019/10/8
 * explain: 
 */
public    class Meizu  implements ISetting  {
    private Context mContext;

    public Meizu(Context context) {
        mContext = context;
    }

    @Override
    public Intent getSetting() {
        Intent intent = new Intent("com.meizu.safe.security.SHOW_APPSEC");
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.putExtra("packageName", mContext.getPackageName());
        return intent;
    }
}
