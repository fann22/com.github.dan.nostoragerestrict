package com.github.dan.NoStorageRestrict;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class CameraHook implements IXposedHookLoadPackage {

    @Override
    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        // Hook hanya ke aplikasi tertentu jika ingin, misalnya:
        if (!lpparam.packageName.equals("android")) return;

        Class<?> cameraManagerClass = XposedHelpers.findClass(
                "android.hardware.camera2.CameraManager",
                lpparam.classLoader
        );

        XposedHelpers.findAndHookMethod(
                cameraManagerClass,
                "getCameraIdList",
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        // Logika sebelum original method dipanggil
                        XposedBridge.log("getCameraIdList() akan dipanggil");
                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        // Logika setelah original method dipanggil
                        XposedBridge.log("getCameraIdList() dipanggil, hasil asli: " + java.util.Arrays.toString((String[]) param.getResult()));

                        // Contoh: ubah hasilnya
                        // param.setResult(new String[]{"0", "1", "2", "3"});
                    }
                }
        );
    }
}