package com.wuzp.mylibluancher.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.Nullable;

import com.wuzp.commonlib.Utils.LogUtil;


/**
 * common service
 *
 * @author wuzp
 */
public class CommonService extends Service implements ICommonServiceInter {

    public static final String COMMON_SERVICE_ACTION = "com.wuzp.mylibluancher.service.CommonService";

    private final String TAG = CommonService.class.getSimpleName();

    private MyBinder mMyBinder = new MyBinder();

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtil.d(TAG,"onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtil.d(TAG,"onStartCommand");
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                stopServiceSelf();
            }
        },5000);
        return super.onStartCommand(intent, flags, startId);
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        LogUtil.d(TAG,"onBind");
        return mMyBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        LogUtil.d(TAG,"onUnbind");
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtil.d(TAG,"onDestroy");
    }

    private Handler mHandler  = new Handler(Looper.getMainLooper());

    private void stopServiceSelf(){
        stopSelf();
    }


   public class MyBinder extends Binder{
        CommonService getService(){
            return CommonService.this;
        }
    }

    @Override
    public int add(int num1, int num2) {
        return num1 + num2;
    }
}
