package com.wuzp.commonlib.Utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;

import java.io.File;
import java.util.Iterator;
import java.util.List;

/**
 * 应用相关的工具类
 *
 * @author wuzhenpeng03
 */
public class ApkUtils {

    private ApkUtils() {
    }

    public static PackageInfo getInstalledApkPackageInfo(Context context, String packageName) {
        PackageManager pm = context.getPackageManager();
        List<PackageInfo> apps = pm.getInstalledPackages(PackageManager.GET_ACTIVITIES);
        Iterator it = apps.iterator();

        PackageInfo packageinfo;
        String thisName;
        do {
            if (!it.hasNext()) {
                return null;
            }

            packageinfo = (PackageInfo) it.next();
            thisName = packageinfo.packageName;
        } while (!thisName.equals(packageName));

        return packageinfo;
    }

    public static List<PackageInfo> getInstallApks(Context context) {
        PackageManager pm = context.getPackageManager();
        List<PackageInfo> apps = pm.getInstalledPackages(PackageManager.GET_ACTIVITIES);
        return apps;
    }

    public static boolean isInstalled(Context context, String packageName) {
        PackageManager pm = context.getPackageManager();
        boolean installed = false;

        try {
            pm.getPackageInfo(packageName, 0);
            installed = true;
        } catch (Exception var5) {
            var5.printStackTrace();
        }

        return installed;
    }

    public static String getSourceApkPath(Context context, String packageName) {
        if (TextUtils.isEmpty(packageName)) {
            return null;
        } else {
            try {
                ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(packageName, 0);
                return appInfo.sourceDir;
            } catch (PackageManager.NameNotFoundException var3) {
                var3.printStackTrace();
                return null;
            }
        }
    }

    public static void installApk(String filepath, Context context) {
        File apkFile = new File(filepath);
        if (apkFile.exists()) {
            Intent intent = new Intent("android.intent.action.VIEW");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                Uri contentUri = FileProvider
                    .getUriForFile(context, context.getPackageName() + ".fileProvider", apkFile);
                intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
            } else {
                intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }

            context.startActivity(intent);
        }
    }

    public static String getPackageVersionName(Context context) {
        try {
            PackageManager pm = context.getPackageManager();
            return pm.getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (Exception var2) {
            var2.printStackTrace();
            return "";
        }
    }
}
