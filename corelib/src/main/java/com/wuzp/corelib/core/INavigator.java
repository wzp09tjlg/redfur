package com.wuzp.corelib.core;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;

/**
 * @author wuzhenpeng03
 */
public interface INavigator {
    void push(/*Page page*/);

    void pushForResult(/*Page page*/);

    void popToRoot();

    void finish();

    void finish(Bundle data);

    void showDialog(@NonNull Dialog dialog, @NonNull String tag);
}
