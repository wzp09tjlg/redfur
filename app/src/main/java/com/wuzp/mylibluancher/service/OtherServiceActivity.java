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
 * 检查如果是是bind的方式来绑定service的法 多次绑定，在第几次解绑的时候 才是真正销毁service
 *
 * @author wuzp
 */
public class OtherServiceActivity extends AppCompatActivity {

    @BindView(R.id.btn_other_service)
    Button btnOtherService;
    @BindView(R.id.btn_check_service_ability)
    Button btnCheckServiceAbility;
    @BindView(R.id.btn_unbind)
    Button btnUnbind;

    Unbinder unbinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_other);
        unbinder = ButterKnife.bind(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (unbinder != null) {
            unbinder.unbind();
        }

        //如果调用两次 会出现错误,报connection没有注册
//        if(mConnect != null){
//            unbindService(mConnect);
//        }
    }

    @OnClick({R.id.btn_other_service, R.id.btn_check_service_ability, R.id.btn_unbind})
    public void onClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_other_service:
                bindCommonService();
                break;
            case R.id.btn_check_service_ability:
                checkServiceAbility();
                break;
            case R.id.btn_unbind:
                unbindCommonService();
                break;
        }
    }

    private void bindCommonService() {
        Intent intent = new Intent(this, CommonService.class);
        //bindService(intent, mConnect, Service.BIND_AUTO_CREATE);
        startService(intent);
    }

    private void checkServiceAbility() {
        if (commonService != null) {
            int resultOther = commonService.add(1000, 2000);
            LogUtil.d("resultOther:" + resultOther);
        }
    }

    private void unbindCommonService() {
        //unbind的方法一个connection只能调用一次。如果调用多次会报错。报service的connection 没有注册。
        if (mConnect != null) {
            unbindService(mConnect);
        }
    }

    private CommonService commonService = null;

    private ServiceConnection mConnect = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            commonService = ((CommonService.MyBinder) service).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
}
