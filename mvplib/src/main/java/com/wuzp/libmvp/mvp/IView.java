package com.wuzp.libmvp.mvp;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.wuzp.corelib.core.ScopeContext;

/**
 * @author wuzhenpeng03
 */
public abstract class IView<P extends IPresenter> implements RestrictContext {

    private P mPresenter;

    // internal usage
    View mView;
    // internal usage
    private Context mContext;

    // internal usage
    final void attachPresenter(P presenter) {
        this.mPresenter = presenter;
    }

    final void attachContext(Context context) {
        this.mContext = context;
    }

    public final Context getContext() {
        return mContext;
    }

    protected final ScopeContext getScopeContext() {
        if (mPresenter == null) {
            throw new IllegalStateException("Presenter not attach to this view of " + this.getClass().getName());
        }
        return mPresenter.getScopeContext();
    }

    public final P getPresenter() {
        return mPresenter;
    }

    protected @NonNull
    View inflateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        return null;
    }

    protected final View getView() {
        return this.mView;
    }

    @MainThread
    protected void onCreate() {
    }

    @MainThread
    protected void onStart() {
    }

    @MainThread
    protected void onResume() {
    }

    @MainThread
    protected void onPause() {
    }

    @MainThread
    protected void onStop() {
    }

    @MainThread
    protected void onDestroy() {
    }

    public Resources getResources() {
        return mContext.getResources();
    }

    public AssetManager getAssets() {
        return mContext.getAssets();
    }


    @Override
    public Resources.Theme getTheme() {
        return mContext.getTheme();
    }

    @Override
    public final CharSequence getText(@StringRes int resId) {
        return getResources().getText(resId);
    }

    @Override
    @NonNull
    public final String getString(@StringRes int resId) {
        return getResources().getString(resId);
    }

    @Override
    @NonNull
    public final String getString(@StringRes int resId, Object... formatArgs) {
        return getResources().getString(resId, formatArgs);
    }

    @Override
    @ColorInt
    public final int getColor(@ColorRes int id) {
        return getResources().getColor(id);
    }

    @Override
    @Nullable
    public final Drawable getDrawable(@DrawableRes int id) {
        return getResources().getDrawable(id);
    }
}
