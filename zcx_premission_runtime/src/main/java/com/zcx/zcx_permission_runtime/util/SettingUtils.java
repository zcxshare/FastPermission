package com.zcx.zcx_permission_runtime.util;

import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.zcx.zcx_permission_runtime.setting.ISetting;
import com.zcx.zcx_permission_runtime.setting.support.Default;
import com.zcx.zcx_permission_runtime.setting.support.HuaWei;
import com.zcx.zcx_permission_runtime.setting.support.OPPO;
import com.zcx.zcx_permission_runtime.setting.support.ViVo;
import com.zcx.zcx_permission_runtime.setting.support.XiaoMi;

/**
 * author:  zhouchaoxiang
 * date:    2019/10/8
 * explain:
 */
public class SettingUtils {

    private static final String MANUFACTURER_HUAWEI = "Huawei";//华为
    private static final String MANUFACTURER_MEIZU = "Meizu";//魅族
    private static final String MANUFACTURER_XIAOMI = "Xiaomi";//小米
    private static final String MANUFACTURER_SONY = "Sony";//索尼
    private static final String MANUFACTURER_OPPO = "OPPO";
    private static final String MANUFACTURER_LG = "LG";
    private static final String MANUFACTURER_VIVO = "vivo";
    private static final String MANUFACTURER_SAMSUNG = "samsung";//三星
    private static final String MANUFACTURER_LETV = "Letv";//乐视
    private static final String MANUFACTURER_ZTE = "ZTE";//中兴
    private static final String MANUFACTURER_YULONG = "YuLong";//酷派
    private static final String MANUFACTURER_LENOVO = "LENOVO";//联想

    /**
     * 跳设置界面
     *
     * @param context context
     */
    public static void go2Setting(Context context) {
        ISetting iSetting;

        switch (Build.MANUFACTURER) {
            case MANUFACTURER_HUAWEI:
                iSetting = new HuaWei(context);
                break;
            case MANUFACTURER_VIVO:
                iSetting = new ViVo(context);
                break;
            case MANUFACTURER_OPPO:
                iSetting = new OPPO(context);
                break;
            case MANUFACTURER_XIAOMI:
                iSetting = new XiaoMi(context);
                break;
            case MANUFACTURER_MEIZU:
                iSetting = new XiaoMi(context);
                break;
            default:
                iSetting = new Default(context);
                break;
        }
        Intent intent = iSetting.getSetting();
        if (intent == null) return;
        try {
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            Default aDefault = new Default(context);
            context.startActivity(aDefault.getSetting());
        }
    }


}
