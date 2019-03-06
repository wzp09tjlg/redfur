package com.wuzp.corelib.core.internal;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import com.wuzp.corelib.core.INavigator;
import com.wuzp.corelib.core.ScopeContext;
import com.wuzp.corelib.core.ScopeContextBase;

/**
 * Component 的生命周期是  的子集. 所以在 Component 内不能直接使用 Page 的 LifeCycle.
 * 在这里对 LifeCycle 做个替换, 不影响业务使用.
 *
 */
// inner class
@RestrictTo(RestrictTo.Scope.LIBRARY)
public final class ScopeContextComponentImpl extends ScopeContextBase {

    private ScopeContext mOwner;
    private String alias;

    public ScopeContextComponentImpl(@NonNull ScopeContext context, String alias) {
        this.mOwner = context;
        this.alias = alias;
    }

    @Override
    protected INavigator newNavigator() {
        return new INavigator() {
            @Override
            public void push() {
                mOwner.getNavigator().push();
            }

            @Override
            public void pushForResult() {
                mOwner.getNavigator().pushForResult();
            }

            @Override
            public void popToRoot() {
                mOwner.getNavigator().popToRoot();
            }

            @Override
            public void finish() {
                mOwner.getNavigator().finish();
            }

            @Override
            public void finish(Bundle data) {
                mOwner.getNavigator().finish(data);
            }

            @Override
            public void showDialog(@NonNull Dialog dialog, @NonNull String tag) {
                mOwner.getNavigator().showDialog(dialog, tag);
            }
        };
    }

    @Override
    public Object getObject(String key) {
        Object result = super.getObject(key);
        if (result == null) {
            return mOwner.getObject(key);
        }
        return result;
    }

    @Nullable
    @Override
    public ScopeContext getParent() {
        return null;
    }

    @NonNull
    @Override
    public String alias() {
        return alias + "@" + this;
    }

    @Override
    public Bundle getBundle() {
        return mOwner.getBundle();
    }

}
