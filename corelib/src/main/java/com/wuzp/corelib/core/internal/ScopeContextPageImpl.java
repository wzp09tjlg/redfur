package com.wuzp.corelib.core.internal;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;
import com.wuzp.corelib.core.INavigator;
import com.wuzp.corelib.core.ScopeContextBase;

@RestrictTo(RestrictTo.Scope.LIBRARY)
public class ScopeContextPageImpl extends ScopeContextBase {

    public ScopeContextPageImpl() {

    }

    @NonNull
    @Override
    public String alias() {
        return "";
    }

    @Override
    public Bundle getBundle() {
        return null;
    }
}
