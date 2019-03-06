package com.wuzp.commonlib.Utils;

import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.text.TextUtils;

/**
 * 常用的工具类
 *
 * @author wuzhenpeng03
 */
public class CommonUtils {

    private CommonUtils() {
    }

    public static boolean equals(String str1, String str2) {
        return TextUtils.isEmpty(str1) && TextUtils.isEmpty(str2) || equals((Object) str1, (Object) str2);
    }

    public static boolean equals(Object obj1, Object obj2) {
        return obj1 == null && obj2 == null || obj1 != null && obj1.equals(obj2);
    }

    /**
     * 打电话
     */
    public static void doCall(Context context, String phoneNum) {
        if (!TextUtils.isEmpty(phoneNum)) {
            Uri uri;
            if (phoneNum.startsWith("tel:")) {
                uri = Uri.parse(phoneNum);
            } else {
                uri = Uri.parse("tel:" + phoneNum);
            }
            Intent intent = new Intent(Intent.ACTION_DIAL, uri);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (intent.resolveActivity(context.getPackageManager()) != null) {
                context.startActivity(intent);
            }
        }
    }

    /**
     * 发送短信
     */
    public static void sendMessage(Context context, String phoneNum, String content) {
        if (!TextUtils.isEmpty(phoneNum)) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("smsto:" + phoneNum));
            intent.putExtra("sms_body", content);
            if (intent.resolveActivity(context.getPackageManager()) != null) {
                context.startActivity(intent);//设置发送的内容
            }
        }
    }

    /**
     * 播放系统提示音
     *
     * @param context 上下文环境,用于提示音获取
     */
    public static void playNotificationRingtone(Context context) {
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone rt = RingtoneManager.getRingtone(context, uri);
        rt.play();
    }
}
