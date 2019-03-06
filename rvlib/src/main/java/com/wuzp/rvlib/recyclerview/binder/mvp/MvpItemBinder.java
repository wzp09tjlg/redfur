package com.wuzp.rvlib.recyclerview.binder.mvp;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.wuzp.rvlib.recyclerview.adapter.NovaRecyclerAdapter;
import com.wuzp.rvlib.recyclerview.binder.ItemBinder;
import com.wuzp.rvlib.recyclerview.binder.ItemViewHolder;
import com.wuzp.rvlib.recyclerview.decorator.ItemDecorator;
import com.wuzp.rvlib.recyclerview.view.NovaRecyclerView;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * 具备承载 RV item Mvp 能力的 ItemBinder
 *
 */
public abstract class MvpItemBinder<T, VH extends ItemViewHolder<T>, U extends MvpItemUnit> extends ItemBinder<T, VH> {

    private HashMap<RecyclerView.ViewHolder, U> mUnitMap = new HashMap<>();

    public MvpItemBinder() {
        init();
    }

    public MvpItemBinder(ItemDecorator itemDecorator) {
        super(itemDecorator);
        init();
    }

    /**
     * 初始化
     */
    private void init() {
        onCreateRvContainerLifecycle().subscribe(new RvLifecycleObserver() {
            @Override
            public void onAttach() {
                Iterator<U> valuesIterator = mUnitMap.values().iterator();
                while (valuesIterator.hasNext()) {
                    U unit = valuesIterator.next();
                    if (unit.isAttached() && !unit.hasAttachFlag()) {
                        unit.callAttach();
                    }
                }
            }

            @Override
            public void onDetach() {
                Iterator<U> valuesIterator = mUnitMap.values().iterator();
                while (valuesIterator.hasNext()) {
                    U unit = valuesIterator.next();
                    if (unit.hasAttachFlag()) {
                        unit.callDetach();
                    }
                }
            }

            @Override
            public void onDestroy() {
                destroyAndClearAllUnit();
            }
        });
    }

    /**
     * 判定 ViewHolder 是否与当前 {@link MvpItemBinder} 绑定，理论上 {@link MvpItemBinder} 与 {@link ItemViewHolder}
     * 必然是一一绑定的所以不需要额外判定条件
     *
     * @param holder ViewHolder 实例
     */
    public boolean canBindHolder(RecyclerView.ViewHolder holder) {
        return bindHolderType().equals(holder.getClass());
    }

    /**
     * @return 绑定的 {@link ItemViewHolder} 类型，避免错误通知
     */
    protected abstract Class<VH> bindHolderType();

    /**
     * 获取 {@link MvpItemUnit} 实例
     * <p>
     * 暂未持有从回收池获取 {@link MvpItemUnit} 实例的逻辑
     *
     * @return {@link MvpItemUnit} 实例
     */
    @NonNull
    private U obtainItemUnit() {
        U unit = onCreateItemUnit();
        setupItemUnit(unit);
        return unit;
    }

    /**
     * 调用 {@link MvpItemUnit#callAttach} 方法表明 MvpItemUnit 已处于可见状态
     *
     * @param holder {@link MvpItemUnit} 对应的 ViewHolder
     */
    private final void attachItemUnit(RecyclerView.ViewHolder holder) {
        getItemUnit(holder).callAttach();
    }

    /**
     * 调用  {@link MvpItemUnit#callDetach} 方法表明 MvpItemUnit 已处于不可见状态
     *
     * @param holder {@link MvpItemUnit} 对应的 ViewHolder
     */
    private void detachItemUnit(RecyclerView.ViewHolder holder) {
        getItemUnit(holder).callDetach();
    }

    /**
     * 回收 {@link MvpItemUnit} 实例
     * <p>
     * 暂未增加回收逻辑，而是直接销毁
     *
     * @param holder {@link MvpItemUnit} 对应的 ViewHolder
     */
    private void recycleItemUnit(RecyclerView.ViewHolder holder) {
        getItemUnit(holder).callDestroy();
        mUnitMap.remove(holder);
    }

    /**
     * 获取 {@link RecyclerView.ViewHolder} 对应的 {@link MvpItemUnit} 实例
     */
    @NonNull
    private U getItemUnit(RecyclerView.ViewHolder holder) {
        U mvpItemUnit = mUnitMap.get(holder);
        if (mvpItemUnit == null) {
            mvpItemUnit = obtainItemUnit();
            mUnitMap.put(holder, mvpItemUnit);
        }
        return mvpItemUnit;
    }

    @Override
    public void bind(VH holder, T item) {
        getItemUnit(holder).callBind(holder);
    }

    /**
     * @return 当前 {@link MvpItemBinder} 对应的 View，用于简化业务端代码
     */
    protected final View getItemView(LayoutInflater inflater, ViewGroup parent) {
        return inflater.inflate(getItemViewLayoutId(), parent, false);
    }

    /**
     * @return 当前 {@link MvpItemBinder} 对应的 layoutId，用于简化业务端代码
     */
    protected abstract int getItemViewLayoutId();

    /**
     * 获取 {@link RvLifecycleObservable} 实例
     *
     * @return
     */
    @NonNull
    protected abstract RvLifecycleObservable onCreateRvContainerLifecycle();

    /**
     * 创建当前 {@link MvpItemBinder} 对应的 {@link MvpItemUnit} 实例
     *
     * @return 当前 {@link MvpItemBinder} 对应的 {@link MvpItemUnit} 实例
     */
    @NonNull
    protected abstract U onCreateItemUnit();

    /**
     * 配置 {@link MvpItemUnit}，用于业务扩展，如 C 端通过 {@link MvpItemUnit} 为 {@link MvpItemPresenter} 添加 ScopeContext
     *
     * @param unit 创建的 {@link MvpItemUnit}  实例
     */
    protected void setupItemUnit(@NonNull U unit) {
    }

    /**
     * RecyclerView 所属的页面在销毁后不会触发 RecyclerView 的 {@link RecyclerView.RecyclerListener} 的 onViewRecycled 方法
     * 所以需要在 RecyclerView 销毁后自动调用此方法
     */
    private void destroyAndClearAllUnit() {
        Iterator<U> mvpItemUnitIterator = mUnitMap.values().iterator();
        while (mvpItemUnitIterator.hasNext()) {
            mvpItemUnitIterator.next().callDestroy();
        }
        mUnitMap.clear();
    }

    /**
     * 获取指定 {@link RecyclerView.ViewHolder} 对应的 {@link MvpItemBinder}
     *
     * @param recyclerView 承载 {@link RecyclerView.ViewHolder} 的 RecyclerView
     * @param holder       指定的 {@link RecyclerView.ViewHolder}
     * @return 指定的 {@link RecyclerView.ViewHolder} 对应的 {@link MvpItemBinder}
     */
    @Nullable
    private static MvpItemBinder getMatchBinder(NovaRecyclerView recyclerView, RecyclerView.ViewHolder holder) {
        NovaRecyclerAdapter adapter = (NovaRecyclerAdapter) recyclerView.getAdapter();
        List<ItemBinder> registeredBinderList = adapter.getRegisteredBinderList();

        int registeredBinderListSize = registeredBinderList.size();
        for (int i = 0; i < registeredBinderListSize; i++) {
            ItemBinder itemBinder = registeredBinderList.get(i);
            if (itemBinder instanceof MvpItemBinder) {
                MvpItemBinder mvpItemBinder = (MvpItemBinder) itemBinder;
                if (mvpItemBinder.canBindHolder(holder)) {
                    return mvpItemBinder;
                }
            }
        }
        return null;
    }

    /**
     * 配合 Mvp 能力的 Item 使用的 {@link RecyclerView.OnChildAttachStateChangeListener}
     */
    public static class NovaOnChildAttachStateChangeListener implements RecyclerView.OnChildAttachStateChangeListener {
        private NovaRecyclerView mRecyclerView;

        public NovaOnChildAttachStateChangeListener(@NonNull NovaRecyclerView recyclerView) {
            mRecyclerView = recyclerView;
        }

        @Override
        public void onChildViewAttachedToWindow(View view) {
            RecyclerView.ViewHolder holder = mRecyclerView.getChildViewHolder(view);
            MvpItemBinder matchMvpItemBinder = getMatchBinder(mRecyclerView, holder);
            if (matchMvpItemBinder != null) {
                matchMvpItemBinder.attachItemUnit(holder);
            }
        }

        @Override
        public void onChildViewDetachedFromWindow(View view) {
            RecyclerView.ViewHolder holder = mRecyclerView.getChildViewHolder(view);
            MvpItemBinder matchMvpItemBinder = getMatchBinder(mRecyclerView, holder);
            if (matchMvpItemBinder != null) {
                matchMvpItemBinder.detachItemUnit(holder);
            }
        }
    }

    /**
     * 配合 Mvp 能力的 Item 使用的 {@link RecyclerView.OnChildAttachStateChangeListener}
     * <p>
     * 因为 RecyclerView 设置 NovaRecyclerListener 是通过 set 而非 add 所以需要添加 mItemMvpEnable 和 mRecyclerListener 以
     * 确认是否支持 Mvp 能力及是否需要分发给其他 {@link RecyclerView.RecyclerListener}
     */
    public static class NovaRecyclerListener implements RecyclerView.RecyclerListener {

        /**
         * 是否支持 ItemMvp 能力
         */
        private boolean mItemMvpEnable;
        private NovaRecyclerView mRecyclerView;
        /**
         * 业务层设置的 {@link RecyclerView.RecyclerListener}
         */
        private RecyclerView.RecyclerListener mRecyclerListener;

        public NovaRecyclerListener(@NonNull NovaRecyclerView recyclerView) {
            mRecyclerView = recyclerView;
        }

        public void setRecyclerListener(RecyclerView.RecyclerListener recyclerListener) {
            mRecyclerListener = recyclerListener;
        }

        public void setItemMvpEnable(boolean itemMvpEnable) {
            mItemMvpEnable = itemMvpEnable;
        }

        public boolean hasRecyclerListener() {
            return mRecyclerListener != null;
        }

        @Override
        public void onViewRecycled(RecyclerView.ViewHolder holder) {
            if (mRecyclerListener != null) {
                mRecyclerListener.onViewRecycled(holder);
            }
            if (mItemMvpEnable) {
                MvpItemBinder matchMvpItemBinder = getMatchBinder(mRecyclerView, holder);
                if (matchMvpItemBinder != null) {
                    matchMvpItemBinder.recycleItemUnit(holder);
                }
            }
        }
    }
}
