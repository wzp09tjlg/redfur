package com.wuzp.corelib.core.internal;

import android.os.Bundle;
import android.support.annotation.RestrictTo;
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
    public String alias() {
        return activity.alias();
    }

    @Override
    public Bundle getBundle() {
        return activity.getIntent().getExtras();
    }
}
