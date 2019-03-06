package com.wuzp.commonlib.Utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.graphics.Rect;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.WindowManager;

import java.io.File;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.List;

/**
 * 系统相关的工具类
 *
 * @author wuzhenpeng03
 */
public class SystemUtils {

    static Activity context;
    private static final String LOC_GPS = "gps";

    private SystemUtils() {
    }

    public static String getIp(Context context) {
        String ip = null;
        ConnectivityManager conMan = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo.State mobile = conMan.getNetworkInfo(0).getState();
        NetworkInfo.State wifi = conMan.getNetworkInfo(1).getState();
        if (mobile == NetworkInfo.State.CONNECTED) {
            ip = getLocalIpAddress();
        }

        if (wifi == NetworkInfo.State.CONNECTED) {
            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            int ipAddress = wifiInfo.getIpAddress();
            ip = intToIp(ipAddress);
        }

        return ip;
    }

    private static String intToIp(int i) {
        return (i & 255) + "." + (i >> 8 & 255) + "." + (i >> 16 & 255) + "." + (i >> 24 & 255);
    }

    private static String getLocalIpAddress() {
        try {
            Enumeration en = NetworkInterface.getNetworkInterfaces();

            while (en.hasMoreElements()) {
                NetworkInterface intf = (NetworkInterface) en.nextElement();
                Enumeration enumIpAddr = intf.getInetAddresses();

                while (enumIpAddr.hasMoreElements()) {
                    InetAddress inetAddress = (InetAddress) enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException var4) {
            var4.printStackTrace();
        }

        return null;
    }

    public static boolean hasGPSDevice(Context context) {
        LocationManager mgr = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (mgr == null) {
            return false;
        } else {
            List<String> providers = mgr.getAllProviders();
            return providers != null && providers.contains("gps");
        }
    }

    public static String getLocalMacAddress(Context context) {
        String mac = "000000";
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if (wifi != null) {
            WifiInfo info = wifi.getConnectionInfo();
            if (info != null) {
                mac = info.getMacAddress();
            }
        }

        return mac;
    }

    public static String getSDCardPath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;
    }

    public static boolean isWifiEnabled(Context context) {
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        return wifiManager.isWifiEnabled();
    }

    public static String getWifiName(Context context) {
        if (context == null) {
            return "";
        } else {
            WifiManager wifiManager = (WifiManager) context.getApplicationContext()
                .getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            return wifiInfo == null ? "" : wifiInfo.getSSID();
        }
    }

    public static String getWifiMac(Context context) {
        if (context == null) {
            return "";
        } else {
            WifiManager wifiManager = (WifiManager) context.getApplicationContext()
                .getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            return wifiInfo == null ? "" : wifiInfo.getMacAddress();
        }
    }

    public static String getOperationName(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String operationName = telephonyManager.getSimOperator();
        if (operationName == null) {
            return "unknow";
        } else {
            if (!operationName.startsWith("46000") && !operationName.startsWith("46002")) {
                if (operationName.startsWith("46001")) {
                    operationName = "中国联通";
                } else if (operationName.startsWith("46003")) {
                    operationName = "中国电信";
                }
            } else {
                operationName = "中国移动";
            }

            return operationName;
        }
    }

    public static boolean isGpsEnabled(Context context) {
        String gps = Settings.System.getString(context.getContentResolver(), "location_providers_allowed");
        return !TextUtils.isEmpty(gps) && gps.contains("gps");
    }

    public static boolean isNetWorkAvailable(Context context) {
        if (context == null) {
            return false;
        } else {
            ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
    }

    public static boolean isWifi(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
            .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetInfo != null && activeNetInfo.getType() == 1;
    }

    public static int getWindowHeight(Activity activity) {
        WindowManager windowManager = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
        return windowManager.getDefaultDisplay().getHeight();
    }

    public static int getTitleBarHeight(Activity activity) {
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        return frame.top;
    }

    public static boolean isCalling(Context context) {
        ComponentName cn = getTopComponentName(context);
        return null != cn && cn.getPackageName().equalsIgnoreCase("com.android.phone") && cn.getClassName()
            .contains("InCallScreen");
    }

    public static String getBriefPhoneNumber(String phoneNumber) {
        return getBriefPhoneNumber(phoneNumber, 4);
    }

    public static String getBriefPhoneNumber(String phoneNumber, int length) {
        return phoneNumber == null ? ""
            : (phoneNumber.length() >= length ? phoneNumber.substring(phoneNumber.length() - length) : phoneNumber);
    }

    public static String getFormatPhoneNumber(String phoneNum) {
        return TextUtils.isEmpty(phoneNum) ? "" : phoneNum.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
    }

    private static ComponentName getTopComponentName(Context context) {
        ComponentName cn = null;
        ActivityManager activityManager = (ActivityManager) ((ActivityManager) context
            .getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningTaskInfo> runningTaskInfos = activityManager.getRunningTasks(1);
        if (runningTaskInfos != null && runningTaskInfos.size() >= 1) {
            cn = ((ActivityManager.RunningTaskInfo) runningTaskInfos.get(0)).topActivity;
        }

        return cn;
    }

    public static String getPhoneInformation(Context context) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n手机机型 = " + Build.MODEL);
        sb.append("\nSDK = " + Build.VERSION.SDK);
        sb.append("\n手机版本号 = " + Build.VERSION.RELEASE);
        if (ActivityCompat.checkSelfPermission(context, "android.permission.READ_PHONE_STATE") != 0) {
            sb.append("\nDeviceId(IMEI) = ");
            sb.append("\nDeviceSoftwareVersion = ");
            sb.append("\nLine1Number = ");
            sb.append("\nNetworkCountryIso = ");
            sb.append("\nNetworkOperator = ");
            sb.append("\nNetworkOperatorName = ");
            sb.append("\nNetworkType = ");
            sb.append("\nPhoneType = ");
            sb.append("\nSubscriberId(IMSI) = ");
        } else {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            sb.append("\nDeviceId(IMEI) = " + tm.getDeviceId());
            sb.append("\nDeviceSoftwareVersion = " + tm.getDeviceSoftwareVersion());
            sb.append("\nLine1Number = " + tm.getLine1Number());
            sb.append("\nNetworkCountryIso = " + tm.getNetworkCountryIso());
            sb.append("\nNetworkOperator = " + tm.getNetworkOperator());
            sb.append("\nNetworkOperatorName = " + tm.getNetworkOperatorName());
            sb.append("\nNetworkType = " + tm.getNetworkType());
            sb.append("\nPhoneType = " + tm.getPhoneType());
            sb.append("\nSubscriberId(IMSI) = " + tm.getSubscriberId());
        }

        return sb.toString();
    }

    public static String getNetworkType(Context context) {
        String name = "UNKNOWN";
        if (context == null) {
            return name;
        } else {
            ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                if (networkInfo.getType() == 1) {
                    name = Context.WIFI_SERVICE;
                } else if (networkInfo.getType() == 0) {
                    int networkType = networkInfo.getSubtype();
                    switch (networkType) {
                        case 1:
                        case 2:
                        case 4:
                        case 7:
                        case 11:
                            name = "2G";
                            break;
                        case 3:
                        case 5:
                        case 6:
                        case 8:
                        case 9:
                        case 10:
                        case 12:
                        case 14:
                        case 15:
                            name = "3G";
                            break;
                        case 13:
                            name = "4G";
                    }
                }
            }

            return name;
        }
    }
}
