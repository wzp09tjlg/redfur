package com.wuzp.corelib.core;

import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;
import android.view.ViewGroup;
import com.wuzp.corelib.core.internal.ScopeContextComponentImpl;

/**
 * 独立的业务单元
 */
public abstract class Component implements ILive {

    protected ViewGroup mContainer;

    private volatile boolean mInited = false;

    protected ScopeContextBase mScopeContext;

    public Component(@NonNull ViewGroup viewGroup) {
        mContainer = viewGroup;
    }

    public final ViewGroup getContainer() {
        return mContainer;
    }

    /**
     * 初始化组件的各个部分(View 和 Presenter)，在使用组件时必须先调用此方法
     */
    void init() {
        if (!mInited) {
            onAttach();
            mInited = true;
        } else {
            throw new IllegalStateException("Component only allows initial once");
        }
    }

    void detachView() {
        mContainer.removeAllViews();
        mInited = false;
        onDetach();
    }


    // internal usage
    @RestrictTo(RestrictTo.Scope.LIBRARY)
    public final void attachScopeContext(ScopeContext context) {
        mScopeContext = onCreateScopeContext(context);
    }

    @NonNull
    protected ScopeContextBase onCreateScopeContext(ScopeContext pageScopeContext) {
        return new ScopeContextComponentImpl(
            pageScopeContext,
            pageScopeContext != null ? pageScopeContext.alias() : "");
    }

    protected void onAttach() {
    }


    @CallSuper
    protected void onCreate() {
        if (mScopeContext != null) {
            mScopeContext.onCreate(this);
        }
    }

    @CallSuper
    protected void onStart() {
        if (mScopeContext != null) {
            mScopeContext.onStart(this);
        }
    }

    @CallSuper
    protected void onResume() {
        if (mScopeContext != null) {
            mScopeContext.onResume(this);
        }
    }

    @CallSuper
    protected void onPause() {
        if (mScopeContext != null) {
            mScopeContext.onPause(this);
        }
    }

    @CallSuper
    protected void onStop() {
        if (mScopeContext != null) {
            mScopeContext.onStop(this);
        }
    }

    @CallSuper
    protected void onDestroy() {
        if (mScopeContext != null) {
            mScopeContext.onDestroy(this);
        }

    }

    protected void onDetach() {
        if (mScopeContext != null) {
            mScopeContext.detachAll();
        }
    }

    @Override
    public boolean isActive() {
        if (mScopeContext != null) {
            return mScopeContext.getLiveHandler().isActive();
        } else {
            return false;
        }
    }

    @Override
    public boolean isDestroyed() {
        if (mScopeContext != null) {
            return mScopeContext.getLiveHandler().isDestroyed();
        } else {
            return true;
        }
    }

    public final ScopeContext getScopeContext() {
        return mScopeContext;
    }
}
