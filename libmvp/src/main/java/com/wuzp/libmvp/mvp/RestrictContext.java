package com.wuzp.libmvp.mvp;

import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;

/**
 * @author wuzhenpeng03
 */
public interface RestrictContext {
    AssetManager getAssets();

    Resources getResources();

    Resources.Theme getTheme();

    CharSequence getText(@StringRes int resId);

    @NonNull
    String getString(@StringRes int resId);

    @NonNull
    String getString(@StringRes int resId, Object... formatArgs);

    @ColorInt
    int getColor(@ColorRes int id);

    @Nullable
    Drawable getDrawable(@DrawableRes int id);
}
