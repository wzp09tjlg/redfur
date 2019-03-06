package com.wuzp.commonlib.Utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;

/**
 * 打开系统页面的工具类
 *
 * @author wuzhenpeng03
 */
public class OpenSysActivityUtils {

    private OpenSysActivityUtils() {
    }

    /**
     * 跳转到系统的包含飞行模式的界面
     *
     * @param context
     */
    public static void openAirplaneSettingActivity(Context context) {
        Intent intent = new Intent(Settings.ACTION_AIRPLANE_MODE_SETTINGS);
        context.startActivity(intent);
    }


    /**
     * 跳转到系统的更多连接或其它连接界面
     *
     * @param context
     */
    public static void openWirelessSettingActivity(Context context) {
        Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
        context.startActivity(intent);
    }


    /**
     * 跳转到系统的APN设置界面
     *
     * @param context
     */
    public static void openApnSettingActivity(Context context) {
        Intent intent = new Intent(Settings.ACTION_APN_SETTINGS);
        context.startActivity(intent);
    }


    /**
     * 根据包名跳转到该app的应用信息界面
     *
     * @param context
     * @param packageName
     */
    public static void openSomeAppInfoActivity(Context context, @NonNull String packageName) {
        Uri packageURI = Uri.parse("package:" + packageName);
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
        context.startActivity(intent);
    }


    /**
     * 跳转到系统的应用管理界面(默认应用界面)
     *
     * @param context
     */
    public static void openAppManagerDefualtSettingActivity(Context context) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_SETTINGS);
        context.startActivity(intent);
    }

    /**
     * 跳转到系统的应用管理界面(全部界面)
     *
     * @param context
     */
    public static void openAppManagerSettingActivity(Context context) {
        Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_APPLICATIONS_SETTINGS);
        context.startActivity(intent);
    }


    /**
     * 跳转到系统的蓝牙管理界面
     *
     * @param context
     */
    public static void openBuletoothSettingActivity(Context context) {
        Intent intent = new Intent(Settings.ACTION_BLUETOOTH_SETTINGS);
        context.startActivity(intent);
    }

    /**
     * 跳转到系统的语言和时间管理界面
     *
     * @param context
     */
    public static void openDateTimeSettingActivity(Context context) {
        Intent intent = new Intent(Settings.ACTION_DATA_ROAMING_SETTINGS);
        context.startActivity(intent);
    }

    /**
     * 跳转到系统的显示和亮度界面
     *
     * @param context
     */
    public static void openDisplaySettingActivity(Context context) {
        Intent intent = new Intent(Settings.ACTION_DISPLAY_SETTINGS);
        context.startActivity(intent);
    }

    /**
     * 跳转到系统的设置界面
     */
    public static void openSettingActivity(Context context) {
        Intent intent = new Intent(Settings.ACTION_SETTINGS);
        context.startActivity(intent);
    }

    /**
     * 跳转到系统的WLAN界面
     */
    public static void openWlanSettingActivity(Context context) {
        Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
        context.startActivity(intent);
    }
}
