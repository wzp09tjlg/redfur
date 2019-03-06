package com.wuzp.corelib.core.internal;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;
import com.wuzp.corelib.core.INavigator;
import com.wuzp.corelib.core.ScopeContextBase;

@RestrictTo(RestrictTo.Scope.LIBRARY)
public class ScopeContextPageImpl extends ScopeContextBase {

    //AbstractPage mPage;

    public ScopeContextPageImpl(/*@NonNull AbstractPage page*/) {
        //this.mPage = page;
    }

    @Override
    protected INavigator newNavigator() {
        return new INavigator() {

            @Override
            public void push() {
                //mPage.push(page);
            }

            @Override
            public void pushForResult() {
                //mPage.pushForResult(page);
            }

            @Override
            public void popToRoot() {
                //mPage.popToRoot();
            }

            @Override
            public void finish() {
                //mPage.finish();
            }

            @Override
            public void finish(Bundle data) {
                //mPage.finish(data);
            }

            @Override
            public void showDialog(@NonNull Dialog dialog, @NonNull String tag) {
                //dialog.show(mPage.getInstrument(), tag);
            }
        };
    }

    @NonNull
    @Override
    public String alias() {
        return "";//mPage.alias() + "@" + this;
    }

    @Override
    public Bundle getBundle() {
        return null;//mPage.getArgs();
    }
}
