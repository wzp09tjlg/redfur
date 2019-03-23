package com.wuzp.mylibluancher.aidl;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.wuzp.mylibluancher.IMyAidlInter;
import com.wuzp.mylibluancher.R;


/**
 * @author wuzp
 */
public class AidlActivity extends AppCompatActivity {

    private final String TAG = AidlActivity.class.getSimpleName();

    private Button mBtn;
    private TextView mDisplay;
    private Button mCheck;

    public AidlActivity() {
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aidl);
        initView();
    }

    private void initView(){
        mBtn = findViewById(R.id.btn_aidl);
        mDisplay = findViewById(R.id.tv_display);
        mCheck = findViewById(R.id.btn_check);
        initData();
    }

    private void initData(){
        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                init(AidlActivity.this);
            }
        });
        mCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(iMyAidlInter!= null){
                    try {
                        int result = iMyAidlInter.add(100,200);
                        Log.e("wzp","result:" + result);
                    }catch (Exception e){

                    }
                }
            }
        });
    }

    IMyAidlInter iMyAidlInter;

    public void init(Context context){
        //定义intent
        Intent intent = new Intent(context,AidlService.class);
        //绑定服务
        context.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //成功连接
            Log.i(TAG,"PushManager ***************成功连接***************");
            iMyAidlInter = IMyAidlInter.Stub.asInterface(service);

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            //断开连接调用
            Log.i(TAG,"PushManager ***************连接已经断开***************");
        }
    };

//aidl的实现步骤及原理
//1。定义aidl文件，以aidl结尾，然后编译。生成相应的java文件。java文件是一个接口，实现了IInterface接口
// 并且在这个接口中定义需要通信的方法，内部包含一个stub的子类
// stub是个binder实现类
//2.服务端通过服务返回一个binder对象，这个binder对象就是实现了stub类的子类。
//3.客户端通过bindService来获取 service，然后得到binder，转换成第一步生成的那个类型，然后就可以直接调用这个binder对应的方法了。

//原理是：
//1。基于binder的通信。客户端持有

}
