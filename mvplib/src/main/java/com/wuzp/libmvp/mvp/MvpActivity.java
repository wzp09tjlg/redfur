package com.wuzp.libmvp.mvp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import com.wuzp.corelib.core.SkeletonActivity;

/**
 * @author wuzhenpeng03
 */
public abstract class MvpActivity<V extends IView, P extends IPresenter> extends SkeletonActivity {
    /**
     * layoutId 默认值为 NO_LAYOUT，表示 Activity 自身不指定布局
     * 布局由 View 来创建
     */
    public static final int NO_LAYOUT = -1;

    P mPresenter;
    V mLogicView;

    @Override
    protected void onAfterCreate(@Nullable Bundle bundle) {
        // mLogicView 可以接管实现 Activity 的布局
        // 判断的依据是先判断 layoutId 是否有布局返回，如果有则用 layoutId
        // 如果没有，判断 mLogicView.inflateView 返回的布局
        // 如果都没有返回，则抛异常报错
        mLogicView = onCreateLogicView();
        mPresenter = onCreatePresenter();

        if (layoutId() != NO_LAYOUT) {
            setContentView(layoutId());
        }

        if (mLogicView != null) {
            mLogicView.attachContext(this);
            View view = mLogicView.inflateView(
                getLayoutInflater(),
                (ViewGroup) findViewById(android.R.id.content));
            if (layoutId() == NO_LAYOUT) {
                setContentView(view);
            }
            mLogicView.attachPresenter(mPresenter);
            mLogicView.onCreate();
        }

        if (mPresenter != null) {
            mPresenter.attachScopeContext(getScopeContext());
            mPresenter.attachView(mLogicView);
            mPresenter.onCreate();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mLogicView != null) {
            mLogicView.onStart();
        }
        if (mPresenter != null) {
            mPresenter.onStart();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mLogicView != null) {
            mLogicView.onResume();
        }
        if (mPresenter != null) {
            mPresenter.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mLogicView != null) {
            mLogicView.onPause();
        }
        if (mPresenter != null) {
            mPresenter.onPause();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mLogicView != null) {
            mLogicView.onStop();
        }
        if (mPresenter != null) {
            mPresenter.onStop();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mLogicView != null) {
            mLogicView.onDestroy();
        }
        if (mPresenter != null) {
            mPresenter.onDestroy();
        }
    }

    public int layoutId() {
        return NO_LAYOUT;
    }

    /**
     * @return Presenter
     */
    protected P onCreatePresenter() {
        return null;
    }

    /**
     * @return LogicView
     */
    protected V onCreateLogicView() {
        return null;
    }

    protected P getPresenter() {
        return mPresenter;
    }

    protected V getLogicView() {
        return mLogicView;
    }
}

