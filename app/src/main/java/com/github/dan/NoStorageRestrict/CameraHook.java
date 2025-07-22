package com.github.dan.NoStorageRestrict;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;
import de.robv.android.xposed.XposedHelpers;

public class CameraHook implements IXposedHookLoadPackage {
    @Override
    public void handleLoadPackage(final LoadPackageParam lpparam) throws Throwable {
        // Cek package target biar gak hook semua app
        if (!lpparam.packageName.equals("com.rtsoft.growtopia")) return;

        XposedBridge.log("Growtopia loaded, attempting to hook...");

        try {
            ClassLoader cl = lpparam.classLoader;

            XposedHelpers.findAndHookMethod(
           "com.rtsoft.growtopia.SharedActivity",
    cl,
    "makeToastUI",
    String.class,
    new XC_MethodHook() {
        @Override
        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
            String msg = (String) param.args[0];
            if (msg != null && msg.contains("No video content is available")) {
                XposedBridge.log("Blocked toast: " + msg);
                param.setResult(null); // Blokir agar tidak muncul
            }
        }
    }
);

        } catch (Exception e) {
            XposedBridge.log("Hooking failed: " + e);
        }
    }
}
