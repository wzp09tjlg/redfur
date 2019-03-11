package com.wuzp.mylibluancher.event;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.wuzp.mylibluancher.R;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * @author wuzhenpeng03
 */
public class ReceiverActivity extends AppCompatActivity {

    @BindView(R.id.tv_receiver)
    TextView mDisplay;

    Unbinder mUnbinder;
    boolean isSend = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receiver);
        mUnbinder = ButterKnife.bind(this);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isSend) {
            gotoSend();
            isSend = true;
        }
    }


    private void gotoSend() {
        startActivity(new Intent(this, SendActivity.class));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageReceived(MessageEvent event) {
        mDisplay.setText(event.name);
    }
}
