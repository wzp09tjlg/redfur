package com.wuzp.libmvp.mvp;

import android.content.Context;
import com.wuzp.corelib.core.Cancelable;
import com.wuzp.corelib.core.ILive;
import com.wuzp.corelib.core.ScopeContext;

/**
 * @author wuzhenpeng03
 */
public abstract class IPresenter<V extends IView> implements ILive {

    private V mLogicView;
    private ScopeContext mScopeContext;

    // internal usage
    final void attachView(V view) {
        this.mLogicView = view;
    }

    // internal usage
    final void attachScopeContext(ScopeContext context) {
        this.mScopeContext = context;
    }

    public final V getLogicView() {
        return mLogicView;
    }

    public final ScopeContext getScopeContext() {
        return mScopeContext;
    }

    public final Context getContext(){
        if (mLogicView == null) {
            throw new IllegalStateException("View not attach to this view of " + this.getClass().getName());
        }
        return mLogicView.getContext();
    }

    protected final void autoRelease(Cancelable cancelable) {
        getScopeContext().getLiveHandler().bind(cancelable);
    }

    protected void onCreate() {
        // attend to empty
    }


    protected void onStart() {
        // attend to empty
    }

    protected void onResume() {
        // attend to empty
    }

    protected void onPause() {
        // attend to empty
    }

    protected void onStop() {
        // attend to empty
    }

    protected void onDestroy() {
        // attend to empty
    }

    @Override
    public boolean isActive() {
        if (mScopeContext != null)
            return mScopeContext.getLiveHandler().isActive();
        else
            return false;
    }

    @Override
    public boolean isDestroyed() {
        if (mScopeContext != null)
            return mScopeContext.getLiveHandler().isDestroyed();
        else
            return true;
    }
}