package com.wuzp.mylibluancher.service;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Debug;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.wuzp.commonlib.Utils.LogUtil;
import com.wuzp.mylibluancher.R;
import com.wuzp.mylibluancher.app.LApplication;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


/**
 * 测验service的Activity
 * 启动service的两种模式。普通和bind两种方式
 * <p>
 * 多个客户端可以绑定到一个Service，但Service只在第一个客户端BindService时才会调用onBind()。后续BindService的客户端获得的IBinder都是从Service第一次调用onBind()中返回的IBinder。
 * <p>
 * 如果Service是bindService启动的（不是startService），那么当最后一个客户端unBindService()，Service将会destroy。
 *
 * @author wuzp
 */
public class ServiceActivity extends AppCompatActivity {

    private static final String TAG = "ServiceActivity";

    @BindView(R.id.btn_common_service)
    Button mBtnCommonService;
    @BindView(R.id.btn_bind_service)
    Button mBtnBindService;
    @BindView(R.id.btn_other_service)
    Button btnOtherService;

    Unbinder unbinder = null;

    static {

        File tempFile = new File(Environment.getExternalStorageState() + "/" + TAG + ".trace");
        if (tempFile == null) {
            try {
                tempFile.createNewFile();
            } catch (Exception e) {
            }
        }
    }

    public ServiceActivity() {
        super();
        //进行性能的代码调优，在方法级别上查看某个方法耗费的时间 以及调用的频繁度
        //指定一个目录，开始写文件
//        File tempFile = new File(Environment.getExternalStorageState()+"/"+TAG+".trace");
//        if(tempFile == null){
//            try {
//                tempFile.createNewFile();
//            }catch (Exception e){}
//        }
        Debug.startMethodTracing(TAG);

        new Thread(new Runnable() {
            @Override
            public void run() {
                Handler handler = new Handler();
            }
        }).start();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);
        unbinder = ButterKnife.bind(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (unbinder != null) {
            unbinder.unbind();
        }
        if (mConnect != null) {
            // unbindService(mConnect);
        }
        Debug.stopMethodTracing();
    }

    @OnClick({R.id.btn_common_service, R.id.btn_bind_service, R.id.btn_other_service})
    public void onClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_common_service:
                startCommonService();
//                bindCommonService();
                break;
            case R.id.btn_bind_service:
                bindCommonService();
                break;
            case R.id.btn_other_service:
                openOtherService();
                break;
        }
    }

    //这个起得跟activity得一样，会让人误解。所以在日常开发过程中应该尽量不要起这么模糊得名字。那是在作死
    private void startCommonService() {
        //显式启动service
        Intent intent = new Intent(this, CommonService.class);
        //普通的启动方式
        //startService(intent);
        //bind的绑定方式
        //bindService(intent, mConnect, Service.BIND_AUTO_CREATE);
        startService(intent);
        // 隐式启动service
    }

    private void bindCommonService() {
        int result = commonServiceInter.add(100, 200);
        LogUtil.d("result:" + result);
        int result1 = mCommonService.add(200, 300);
        LogUtil.d("result1:" + result1);
    }

    private void openOtherService() {
        Intent intent = new Intent(this, OtherServiceActivity.class);
        startActivity(intent);
    }

    CommonService mCommonService = null;

    ICommonServiceInter commonServiceInter = null;

    private ServiceConnection mConnect = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //非静态内部类也是可以拿来做类型转换,获得的是service的"对象"。虽然调用的方式是可以调用任意在service中声明的方法，
            // 但是 其实调用的是service 的一个代理对象。中间是通过binder机制进行通信的。
            commonServiceInter = ((CommonService.MyBinder) (service)).getService();
            mCommonService = ((CommonService.MyBinder) (service)).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }

    };


    ///////////////// test threadLocal
    ThreadLocal<Boolean> mBooleanThreadLocal = new ThreadLocal<>();

    private void testThreadLoca() {
        mBooleanThreadLocal.set(true);
        Log.d(TAG, "[Thread#main]mBooleanThreadLocal=" + mBooleanThreadLocal.get());

        new

                Thread("Thread#1") {
                    @Override
                    public void run() {
                        mBooleanThreadLocal.set(false);
                        Log.d(TAG, "[Thread#1]mBooleanThreadLocal=" + mBooleanThreadLocal.get());
                    }

                    ;
                }.

                start();

        new

                Thread("Thread#2") {
                    @Override
                    public void run() {
                        Log.d(TAG, "[Thread#2]mBooleanThreadLocal=" + mBooleanThreadLocal.get());
                    }


                };


    }
}
