package com.wuzp.corelib.core.internal;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;
import com.wuzp.corelib.core.INavigator;
import com.wuzp.corelib.core.ScopeContextBase;
import com.wuzp.corelib.core.SkeletonActivity;

/**
 * @author wuzhenpeng03
 */
@RestrictTo(RestrictTo.Scope.LIBRARY)
public class ScopeContextActivityImpl extends ScopeContextBase {

    SkeletonActivity activity;

    public ScopeContextActivityImpl(SkeletonActivity baseActivity) {
        activity = baseActivity;
    }

    @Override
    protected INavigator newNavigator() {
        return new INavigator() {

            @Override
            public void push() {
            }

            @Override
            public void pushForResult() {
                push();
            }

            @Override
            public void popToRoot() {
            }

            @Override
            public void finish() {
                activity.finish();
            }

            @Override
            public void finish(Bundle data) {
                finish();
            }

            @Override
            public void showDialog(@NonNull Dialog dialog, @NonNull String tag) {
            }
        };
    }

    @Override
    public String alias() {
        return activity.alias();
    }

    @Override
    public Bundle getBundle() {
        return activity.getIntent().getExtras();
    }
}
