package com.zcx.fast_permission_runtime.setting.support;

import android.content.Context;
import android.content.Intent;

import com.zcx.fast_permission_runtime.setting.ISetting;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by mq on 2018/3/6 上午11:43
 * mqcoder90@gmail.com
 */

public class XiaoMi implements ISetting{
    private Context mContext;
    public XiaoMi(Context context) {
        mContext = context;
    }

    @Override
    public Intent getSetting() {
        String rom = getMiuiVersion();
        Intent intent=new Intent();
        if ("V8".equals(rom) || "V9".equals(rom)) {
            intent.setAction("miui.intent.action.APP_PERM_EDITOR");
            intent.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.PermissionsEditorActivity");
            intent.putExtra("extra_pkgname", mContext.getPackageName());
        }else {
            intent.setAction("miui.intent.action.APP_PERM_EDITOR");
            intent.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.AppPermissionsEditorActivity");
            intent.putExtra("extra_pkgname", mContext.getPackageName());
        }
        return intent;
    }
    private static String getMiuiVersion() {
        String propName = "ro.miui.ui.version.name";
        String line;
        BufferedReader input = null;
        try {
            Process p = Runtime.getRuntime().exec("getprop " + propName);
            input = new BufferedReader(
                    new InputStreamReader(p.getInputStream()), 1024);
            line = input.readLine();
            input.close();
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        } finally {
            try {
                input.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return line;
    }
}
