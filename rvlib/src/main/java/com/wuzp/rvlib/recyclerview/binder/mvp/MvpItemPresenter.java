package com.wuzp.rvlib.recyclerview.binder.mvp;

import android.content.Context;
import android.support.annotation.CallSuper;

/**
 * 承载 RV item Mvp 中 P 模块职责的类型
 */
public abstract class MvpItemPresenter<V extends MvpItemView, T> {

    private V mItemView;
    private T mItem;

    /**
     * 吸附 {@link MvpItemView}
     *
     * @param itemView {@link MvpItemView} 实例
     */
    final void attachItemView(V itemView) {
        mItemView = itemView;
    }

    /**
     * 吸附 RV item 对应的数据实例
     *
     * @param item RV item 对应的数据实例
     */
    final void attachItem(T item) {
        mItem = item;
    }

    /**
     * @return {@link Context} 实例
     */
    public final Context getContext() {
        if (mItemView == null) {
            throw new IllegalStateException("View not callAttach to this view of " + getClass().getName());
        }
        return mItemView.getContext();
    }

    public final V getItemView() {
        return mItemView;
    }

    public final T getItem() {
        return mItem;
    }

    /**
     * Item 对应的视图被吸附到 RecyclerView 时触发
     */
    @CallSuper
    protected void onAttach() {
    }

    /**
     * Item 对应的视图从 RecyclerView 上移除时触发
     */
    @CallSuper
    protected void onDetach() {
    }

    /**
     * Item 对应的 {@link MvpItemUnit} 重新触发 bind 或被销毁时触发
     */
    @CallSuper
    protected void onDestroy() {
    }
}

