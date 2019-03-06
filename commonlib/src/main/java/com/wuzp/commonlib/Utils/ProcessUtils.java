package com.wuzp.commonlib.Utils;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.os.Build;
import android.os.Process;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;

/**
 * Process相关的工具类
 *
 * @author wuzhenpeng03
 */
public abstract class ProcessUtils {

    public static boolean isBackground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses != null && appProcesses.size() != 0) {
            Iterator var3 = appProcesses.iterator();

            ActivityManager.RunningAppProcessInfo appProcess;
            do {
                if (!var3.hasNext()) {
                    return false;
                }

                appProcess = (ActivityManager.RunningAppProcessInfo) var3.next();
            } while (!appProcess.processName.equals(context.getPackageName()));

            return appProcess.importance != 100;
        } else {
            return false;
        }
    }

    public static boolean isRunningInForeground(Context context) {
        return Build.VERSION.SDK_INT >= 21 ? ProcessUtils.LollipopRunningProcessCompat.isRunningInForeground(context)
            : ProcessUtils.RunningProcessCompat.isRunningInForeground(context);
    }

    public static boolean isMainProcess(Context context) {
        try {
            ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            List<RunningAppProcessInfo> raps = am.getRunningAppProcesses();
            if (null != raps && raps.size() > 0) {
                int pid = Process.myPid();
                String packageName = context.getPackageName();
                Iterator var5 = raps.iterator();

                while (var5.hasNext()) {
                    RunningAppProcessInfo info = (RunningAppProcessInfo) var5.next();
                    if (packageName.equals(info.processName)) {
                        return pid == info.pid;
                    }
                }
            }
        } catch (Exception var7) {
            ;
        }

        return false;
    }

    private ProcessUtils() {
    }

    private static final class LollipopRunningProcessCompat extends ProcessUtils.RunningProcessCompat {

        private LollipopRunningProcessCompat() {
            super();
        }

        public static boolean isRunningInForeground(Context context) {
            try {
                Field processStateField = RunningAppProcessInfo.class.getDeclaredField("processState");
                ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
                List<RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
                if (null == processInfos || processInfos.isEmpty()) {
                    return false;
                }

                String packageName = context.getPackageName();
                Iterator var5 = processInfos.iterator();

                while (var5.hasNext()) {
                    RunningAppProcessInfo rapi = (RunningAppProcessInfo) var5.next();
                    if (rapi.importance == 100 && rapi.importanceReasonCode == 0) {
                        try {
                            Integer processState = Integer.valueOf(processStateField.getInt(rapi));
                            if (processState != null && processState.intValue() == 2 && rapi.pkgList != null
                                && rapi.pkgList.length > 0) {
                                return rapi.pkgList[0].equals(packageName);
                            }
                        } catch (Exception var8) {
                            ;
                        }
                    }
                }
            } catch (Exception var9) {
                ;
            }

            return false;
        }
    }

    private static class RunningProcessCompat {

        private RunningProcessCompat() {
        }

        public static boolean isRunningInForeground(Context context) {
            try {
                ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
                List<RunningTaskInfo> tasks = am.getRunningTasks(1);
                return null != tasks && !tasks.isEmpty() ? ((RunningTaskInfo) tasks.get(0)).topActivity.getPackageName()
                    .equals(context.getPackageName()) : false;
            } catch (Exception var3) {
                return false;
            }
        }
    }
}
