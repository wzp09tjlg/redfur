package com.wuzp.libmvp.mvp;

import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.wuzp.corelib.core.Component;

/**
 * @author wuzhenpeng03
 */
public class MvpComponent<V extends IView, P extends IPresenter> extends Component {

    private V mLogicalView;
    private P mPresenter;

    public MvpComponent(@NonNull ViewGroup viewGroup) {
        super(viewGroup);
    }

    @Override
    protected void onAttach() {

        if (mLogicalView == null) {
            mLogicalView = onCreateView();
        }

        if (mPresenter == null) {
            mPresenter = onCreatePresenter();
        }

        // 将View绑定到Presenter
        bind(mLogicalView, mPresenter);

        if (mLogicalView != null) {
            // attach context to view
            mLogicalView.attachContext(mContainer.getContext());

            LayoutInflater inflater = LayoutInflater.from(mContainer.getContext());
            mLogicalView.mView = mLogicalView.inflateView(inflater, mContainer);

            if (mLogicalView.mView == null) {
                Log.w("Component", "mLogicalView.mView is null");
            }
        }
    }

    /**
     * @param view
     * @param presenter
     */
    @CallSuper
    protected void bind(V view, P presenter) {
        if (view != null) {
            view.attachPresenter(presenter);
        }
        if (presenter != null) {
            presenter.attachScopeContext(mScopeContext);
            presenter.attachView(view);
        }
    }

    protected V onCreateView() {
        return null;
    }

    protected P onCreatePresenter() {
        return null;
    }

    public final V getLogicView() {
        return mLogicalView;
    }

    public final P getPresenter() {
        return mPresenter;
    }

    @CallSuper
    @Override
    protected void onCreate() {
        super.onCreate();

        if (mLogicalView != null) {
            mLogicalView.onCreate();
        }

        if (mPresenter != null) {
            mPresenter.onCreate();
        }
    }

    @CallSuper
    @Override
    protected void onStart() {
        super.onStart();

        if (mLogicalView != null) {
            mLogicalView.onStart();
        }

        if (mPresenter != null) {
            mPresenter.onStart();
        }
    }

    @CallSuper
    @Override
    protected void onResume() {
        super.onResume();

        if (mLogicalView != null) {
            mLogicalView.onResume();
        }

        if (mPresenter != null) {
            mPresenter.onResume();
        }
    }

    @CallSuper
    @Override
    protected void onPause() {
        if (mLogicalView != null) {
            mLogicalView.onPause();
        }

        if (mPresenter != null) {
            mPresenter.onPause();
        }

        super.onPause();
    }

    @CallSuper
    @Override
    protected void onStop() {
        if (mLogicalView != null) {
            mLogicalView.onStop();
        }

        if (mPresenter != null) {
            mPresenter.onStop();
        }

        super.onStop();
    }

    @CallSuper
    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mPresenter != null) {
            mPresenter.onDestroy();
        }

        if (mLogicalView != null) {
            mLogicalView.onDestroy();
        }
    }
}

