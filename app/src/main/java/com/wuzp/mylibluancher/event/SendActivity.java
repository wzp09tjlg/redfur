package com.wuzp.mylibluancher.event;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import com.wuzp.mylibluancher.R;
import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wuzhenpeng03
 */
public class SendActivity extends AppCompatActivity {

    @BindView(R.id.btn_send_msg)
    Button mSend;

    Unbinder mUnbinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);
        mUnbinder = ButterKnife.bind(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
    }

    @OnClick(R.id.btn_send_msg)
    public void sendMessage() {
        EventBus.getDefault().post(new MessageEvent("good new is here."));
    }

    //hashMap的源码分析
    private void testHashMap() {
        Map<String, String> map = new HashMap<>();
        map.put("key", "value");
        String value = map.get("key");
    }
}
