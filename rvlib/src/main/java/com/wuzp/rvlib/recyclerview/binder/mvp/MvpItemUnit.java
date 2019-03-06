package com.wuzp.rvlib.recyclerview.binder.mvp;

import android.content.Context;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import com.wuzp.rvlib.recyclerview.binder.ItemViewHolder;

/**
 * RV item mvp 能力容器单元
 */
public abstract class MvpItemUnit<V extends MvpItemView<P, VH>, P extends MvpItemPresenter<V, T>,
    T, VH extends ItemViewHolder<T>> {

    /**
     * 展示在屏幕
     */
    private static final int FLAG_ATTACHED = 1 << 1;
    /**
     * 从屏幕上移除
     */
    private static final int FLAG_DETACHED = 1 << 2;

    /**
     * 状态标识
     */
    private int mFlags;

    private V mItemView;
    private P mItemPresenter;

    /**
     * 创建 ItemView 和 ItemPresenter，并分别初始化，如果当前存在活跃的 ItemView 和 ItemPresenter，则先销毁活跃的 ItemView 和 ItemPresenter
     */
    private void createVP(@NonNull VH holder) {
        Context context = holder.itemView.getContext();
        mItemView = onCreateView();
        mItemPresenter = onCreatePresenter();
        mItemView.attachItemPresenter(mItemPresenter);
        mItemPresenter.attachItemView(mItemView);
        mItemView.attachContext(context);
        mItemPresenter.attachItem(holder.getItem());
        mItemView.attachViewHolder(holder);
        setupItemView(mItemView);
        setupPresenter(mItemPresenter);
    }

    /**
     * callBind 必然在 callAttach 前调用，但 callAttach 与 callDetach 之间也可能存在多次调用 callBind
     * <p>
     * callBind 后如果对应的视图处于可见区域则底层自动调用一次 callAttach
     */
    final void callBind(@NonNull VH holder) {
        // 销毁 bind 前存在的 V 和 P
        // 因为重新 bind 了，所以我们即使使用同一 Unit 实例也要销毁之前使用的 V 和 P
        destroyVP();
        createVP(holder);
        mItemView.updateView(holder);
        onBind();
        if (isAttached()) {
            callAttach();
        }
    }

    /**
     * bind 结束时回调
     */
    @CallSuper
    protected void onBind() {
    }

    /**
     * MvpItemUnit 对应的 Item 的视图被吸附到 RecyclerView 上或触发 bind 时 Item 的视图依旧可见时的回调
     */
    final void callAttach() {
        removeFlags(FLAG_DETACHED);
        addFlags(FLAG_ATTACHED);
        onAttach();
        mItemView.onAttach();
        mItemPresenter.onAttach();
    }

    /**
     * attach 结束后回调
     */
    @CallSuper
    protected void onAttach() {
    }

    /**
     * {@link MvpItemUnit} 对应的 Item 的视图从 RecyclerView 中移除时回调
     */
    final void callDetach() {
        removeFlags(FLAG_ATTACHED);
        addFlags(FLAG_DETACHED);
        onDetach();
        mItemView.onDetach();
        mItemPresenter.onDetach();
    }

    /**
     * detach 结束后回调
     */
    @CallSuper
    protected void onDetach() {
    }

    /**
     * {@link MvpItemUnit} 对应的 {@link MvpItemBinder} 被回收时回调
     */
    final void callDestroy() {
        destroyVP();
    }

    /**
     * 触发 ItemView 和 ItemPresenter 的销毁，并清除引用
     * <p>
     * 目前会在 ItemBinder 的 onBindViewHolder 或 ItemViewHolder 被回收后触发
     */
    private void destroyVP() {
        // MvpItemUnit destroy 前处于 attach 状态这种情况目前会在 RecyclerView 的 itemChange 后触发 onBindViewHolder 后出现
        if (isAttached()) {
            callDetach();
        }
        if (mItemView != null) {
            mItemView.onDestroy();
        }
        if (mItemPresenter != null) {
            mItemPresenter.onDestroy();
        }
        mItemView = null;
        mItemPresenter = null;
    }

    /**
     * @return ItemView 实例
     */
    protected abstract V onCreateView();

    /**
     * @return ItemPresenter 实例
     */
    protected abstract P onCreatePresenter();

    /**
     * {@link MvpItemUnit} 对应的 Item 的视图是否 attach 到 RecyclerView
     */
    public boolean isAttached() {
        if (mItemView == null) {
            return false;
        }
        VH holder = mItemView.getViewHolder();
        return holder != null && holder.itemView != null && holder.itemView.getParent() != null;
    }

    /**
     * 是否具有 Attach Flag
     */
    public boolean hasAttachFlag() {
        return hasFlags(FLAG_ATTACHED);
    }

    private void addFlags(int flags) {
        mFlags |= flags;
    }

    private void removeFlags(int flags) {
        mFlags &= ~flags;
    }

    private boolean hasFlags(int flags) {
        return (mFlags & flags) != 0;
    }

    protected P getItemPresenter() {
        return mItemPresenter;
    }

    protected V getItemView() {
        return mItemView;
    }

    /**
     * 配置 {@link MvpItemView}，用于业务扩展
     *
     * @param mvpItemView 创建的 {@link MvpItemView}  实例
     */
    protected void setupItemView(@NonNull V mvpItemView) {
    }

    /**
     * 配置 {@link MvpItemView}，用于业务扩展，如 C 端通过 {@link MvpItemUnit} 为 {@link MvpItemPresenter} 添加 ScopeContext
     *
     * @param mvpItemPresenter 创建的 {@link MvpItemPresenter}  实例
     */
    protected void setupPresenter(@NonNull P mvpItemPresenter) {
    }
}

