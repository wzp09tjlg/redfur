package com.wuzp.mylibluancher.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.wuzp.mylibluancher.R;
import com.wuzp.mylibluancher.base.BaseActivity;
import com.wuzp.mylibluancher.event.ReceiverActivity;
import com.wuzp.mylibluancher.main.menu.MenuComponent;
import com.wuzp.mylibluancher.main.title.TitleComponent;

import java.lang.ref.WeakReference;

/**
 * 真正业务层的activity
 * <p>
 * 可以承载众多业务模块的activity
 *
 * @author wuzhenpeng03
 */
public class BusinessActivity extends BaseActivity {

    @BindView(R.id.fl_title_bar_contain)
    FrameLayout mTitleComponent;
    @BindView(R.id.fl_menu_contain)
    FrameLayout mMenuComponent;
    @BindView(R.id.fl_business_contain)
    FrameLayout mBusinessComponent;

    Context context = this;

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int what = msg.what;
            int offset = what % 2;
            try {
                Thread.sleep(2000);
            } catch (Exception e) {
            }
        }
    };

    @Override
    protected void onAfterCreate(@Nullable Bundle savedInstanceState) {
        super.onAfterCreate(savedInstanceState);
        LayoutInflater inflater = LayoutInflater.from(this);
        View rootView = inflater.inflate(R.layout.activity_business, null, false);
        ButterKnife.bind(this, rootView);
        setContentView(rootView);
    }

    @Override
    protected void setupComponents() {
        super.setupComponents();
        TitleComponent titleComponent = new TitleComponent(mTitleComponent);
        MenuComponent menuComponent = new MenuComponent(mMenuComponent);

        addComponent(titleComponent);
        addComponent(menuComponent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //testLeak();
        //testBlock();
        gotoEvent();
    }

    private void gotoEvent() {
        startActivity(new Intent(this, ReceiverActivity.class));
    }

    private void testBlock() {
        int size = 100;
        for (int i = 1; i < size; i++) {
            mHandler.sendEmptyMessage(i);
            //  new Thread(new MyRunnbale(i)).start();
        }
    }

    static class MyRunnbale implements Runnable {

        int len;

        public MyRunnbale(int len) {
            this.len = len;
        }

        @Override
        public void run() {
            try {
                Thread.sleep(2000 * len);
            } catch (Exception e) {
            }
        }
    }

    private void testLeak() {
        mHandler.sendEmptyMessageDelayed(0101, 5000);
        mHandler.postDelayed(new InnerClass(), 10000);
    }

    private void doFinish() {
        finish();
    }

    class InnerClass implements Runnable {

        // 因为这个类持有MainActivity的引用，在MainActivity销毁的时候 还是持有，并且这个消息在消息队列中等待执行，所以导致内存泄露
        //Context mContext = (Context)MainActivity.this;
        //如果改成弱引用，那么就不存在内存泄露了
        WeakReference mContext = new WeakReference<Context>(context);

        @Override
        public void run() {

        }
    }

    public class TestThreadLocalSourceCheck{

        Looper looper = null;
        ThreadLocal<Looper> threadLocal = new ThreadLocal<>();

    }

}
