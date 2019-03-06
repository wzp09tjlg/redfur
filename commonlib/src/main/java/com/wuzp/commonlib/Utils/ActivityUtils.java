package com.wuzp.commonlib.Utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.text.TextUtils;

import java.util.Iterator;
import java.util.List;

/**
 * Activity相关工具类
 *
 * @author wuzhenpeng03
 */
public final class ActivityUtils {

    private ActivityUtils() {
    }

    public static void startSystemSettingActivity(Context context) {
        Intent intent = new Intent("android.settings.SETTINGS");
        if (isIntentSafe(context, intent)) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }

    }

    public static void startWifiSettingActivity(Context context) {
        Intent intent = new Intent("android.settings.WIFI_SETTINGS");
        if (!isIntentSafe(context, intent)) {
            intent = new Intent("android.settings.WIRELESS_SETTINGS");
            if (!isIntentSafe(context, intent)) {
                intent = new Intent("android.settings.SETTINGS");
            }
        }

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void startGpsSettingActivity(Context context) {
        Intent intent = new Intent("android.settings.LOCATION_SOURCE_SETTINGS");
        if (isIntentSafe(context, intent)) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }

    }

    public static void startDialActivity(Context context, String phoneNumber) {
        Intent intent = new Intent("android.intent.action.DIAL");
        if (!isIntentSafe(context, intent)) {
            intent = new Intent("android.intent.action.VIEW");
        }

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.parse(phoneNumber);
        if (TextUtils.isEmpty(uri.getScheme())) {
            intent.setData(Uri.parse("tel:" + phoneNumber));
        } else {
            intent.setData(uri);
        }

        context.startActivity(intent);
    }

    private static boolean isIntentSafe(Context context, Intent intent) {
        List<ResolveInfo> activities = context.getPackageManager().queryIntentActivities(intent, 65536);
        if (activities != null) {
            Iterator var3 = activities.iterator();

            while (var3.hasNext()) {
                ResolveInfo resolveInfo = (ResolveInfo) var3.next();
                if (resolveInfo.activityInfo.exported) {
                    return true;
                }
            }
        }

        return false;
    }
}
