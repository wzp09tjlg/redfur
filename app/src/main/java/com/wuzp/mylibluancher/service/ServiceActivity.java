package com.wuzp.mylibluancher.service;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.wuzp.commonlib.Utils.LogUtil;
import com.wuzp.mylibluancher.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


/**
 * 测验service的Activity
 * 启动service的两种模式。普通和bind两种方式
 *
 * @author wuzp
 */
public class ServiceActivity extends AppCompatActivity {


    @BindView(R.id.btn_common_service)
    Button mBtnCommonService;
    @BindView(R.id.btn_bind_service)
    Button mBtnBindService;

    Unbinder unbinder = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);
        unbinder = ButterKnife.bind(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(unbinder != null){
            unbinder.unbind();
        }
        if(mConnect != null){
            unbindService(mConnect);
        }
    }

    @OnClick({R.id.btn_common_service,R.id.btn_bind_service})
    public void onClicked(View view){
        switch (view.getId()){
            case R.id.btn_common_service:
                startCommonService();
//                bindCommonService();
                break;
            case R.id.btn_bind_service:
                bindCommonService();
                break;
        }
    }

    //这个起得跟activity得一样，会让人误解。所以在日常开发过程中应该尽量不要起这么模糊得名字。那是在作死
    private void startCommonService(){
        //显式启动service
        Intent intent = new Intent(this,CommonService.class);
        //普通的启动方式
        //startService(intent);
        //bind的绑定方式
        bindService(intent,mConnect, Service.BIND_AUTO_CREATE);
        // 隐式启动service
    }

    private void bindCommonService(){
        int result = commonServiceInter.add(100,200);
        LogUtil.d("result:"+ result);
    }

    ICommonServiceInter commonServiceInter = null;

    private ServiceConnection mConnect = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //非静态内部类也是可以拿来做类型转换？？？
            commonServiceInter = ((CommonService.MyBinder)(service)).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
}
