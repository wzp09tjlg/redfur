package com.wuzp.mylibluancher.app;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.StrictMode;
import android.util.Log;
import com.github.moduth.blockcanary.BlockCanary;
import com.github.moduth.blockcanary.BlockCanaryContext;
import com.squareup.leakcanary.LeakCanary;
import com.wuzp.mylibluancher.BuildConfig;

/**
 * 应用的application类
 *
 * @author wuzhenpeng03
 */
public class LApplication extends Application {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();

        mContext = this;

        enabledStrictMode();
        if (checkLeakCanary(this)) {
            return;
        }
        initBlockCanary();
    }

    //设置leakCanary检查
    private boolean checkLeakCanary(Application application) {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return true;
        }
        LeakCanary.install(this);
        return false;
    }

    //设置blockCanary
    private void initBlockCanary() {
        BlockCanary.install(this, new AppContext()).start();
    }

    //开启严格模式
    private static void enabledStrictMode() {
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder() //
            .detectAll() //
            .penaltyLog() //
            .penaltyDeath() //
            .build());
    }

    public static Context getContext() {
        return mContext;
    }

    //参数设置
    public class AppContext extends BlockCanaryContext {

        private static final String TAG = "AppContext";

        @Override
        public String provideQualifier() {
            String qualifier = "";
            try {
                PackageInfo info = LApplication.getContext().getPackageManager()
                    .getPackageInfo(LApplication.getContext().getPackageName(), 0);
                qualifier += info.versionCode + "_" + info.versionName + "_YYB";
            } catch (PackageManager.NameNotFoundException e) {
                Log.e(TAG, "provideQualifier exception", e);
            }
            return qualifier;
        }

        @Override
        public int provideBlockThreshold() {
            return 500;
        }

        @Override
        public boolean displayNotification() {
            return BuildConfig.DEBUG;
        }

        @Override
        public boolean stopWhenDebugging() {
            return false;
        }
    }
}


