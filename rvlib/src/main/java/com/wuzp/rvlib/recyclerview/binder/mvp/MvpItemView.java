package com.wuzp.rvlib.recyclerview.binder.mvp;

import android.content.Context;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import com.wuzp.rvlib.recyclerview.binder.ItemViewHolder;

/**
 * 承载 RV item Mvp 中 V 模块职责的类型
 */
public abstract class MvpItemView<P extends MvpItemPresenter, VH extends ItemViewHolder> {

    private P mItemPresenter;

    private VH mHolder;

    // internal usage
    private Context mContext;

    /**
     * 吸附 {@link MvpItemPresenter}
     *
     * @param itemPresenter {@link MvpItemView} 实例
     */
    final void attachItemPresenter(P itemPresenter) {
        mItemPresenter = itemPresenter;
    }

    final void attachContext(Context context) {
        mContext = context;
    }

    final void attachViewHolder(VH holder) {
        mHolder = holder;
    }

    public final Context getContext() {
        return mContext;
    }

    public final P getItemPresenter() {
        return mItemPresenter;
    }

    public final VH getViewHolder() {
        return mHolder;
    }

    /**
     * Item 对应的视图吸附到 RecyclerView 上时回调
     */
    @CallSuper
    protected void onAttach() {
    }

    /**
     * Item 对应的视图从 RecyclerView 中移除时回调
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

    /**
     * 更新视图，对应 {@link android.support.v7.widget.RecyclerView.Adapter#onBindViewHolder}
     * <p>
     * 此声明周期可能会由 GapWorker 的 prefetch 或数据变化回调，非严格的生命周期，建议只进行 ViewHolder 的 bind 相应操作
     *
     * @param holder {@link ItemViewHolder} 实例
     */
    protected abstract void updateView(@NonNull VH holder);
}
