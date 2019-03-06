package com.wuzp.rvlib.recyclerview.view;


import android.content.Context;
import android.support.annotation.MainThread;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.AttributeSet;
import com.wuzp.rvlib.recyclerview.adapter.NovaRecyclerAdapter;
import com.wuzp.rvlib.recyclerview.binder.ItemViewHolder;
import com.wuzp.rvlib.recyclerview.binder.mvp.MvpItemBinder;
import com.wuzp.rvlib.recyclerview.listener.ItemDragListener;
import com.wuzp.rvlib.recyclerview.view.helper.NovaItemTouchHelper;
import com.wuzp.rvlib.recyclerview.view.layoutmanager.INovaLayoutManager;

/**
 * 在 mvp 架构中，优先使用 assemblyunit 包的 BaseRecyclerView 而非单独对 NovaRecyclerView 进行封装
 */
public class NovaRecyclerView extends RecyclerView implements ItemDragListener, INovaRecyclerView {

    /**
     * 是否启用分割线
     */
    private boolean mItemDecorationEnable = false;
    /**
     * 是否启用底部加载
     */
    private boolean mFootLoadMoreEnable = false;

    /**
     * 是否开启 Item Mvp 功能
     */
    private boolean mItemMvpEnable = false;

    private INovaLayoutManager mNovaLayoutManager;

    private NovaRecyclerAdapter mNovaRecyclerAdapter;

    private OnLoadMoreScrollListener mOnLoadMoreScrollListener;

    private ItemTouchHelper mItemDragHelper;
    private NovaItemTouchHelper mItemTouchHelper;

    private MvpItemBinder.NovaOnChildAttachStateChangeListener mOnChildAttachStateChangeListener;

    private MvpItemBinder.NovaRecyclerListener mRecyclerListener;

    public NovaRecyclerView(Context context) {
        super(context);
        init();
    }

    public NovaRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public NovaRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        mOnLoadMoreScrollListener = new OnLoadMoreScrollListener();
        mOnChildAttachStateChangeListener = new MvpItemBinder.NovaOnChildAttachStateChangeListener(this);
        mRecyclerListener = new MvpItemBinder.NovaRecyclerListener(this);
    }

    @Override
    public void setNovaLayoutManager(INovaLayoutManager novaLayoutManager) {
        if (!(novaLayoutManager instanceof LayoutManager)) {
            throw new IllegalStateException("novaLayoutManager must be instance of LayoutManager");
        }
        setLayoutManager((LayoutManager) novaLayoutManager);
    }

    @Override
    public void setLayoutManager(LayoutManager layoutManager) {
        if (!(layoutManager instanceof INovaLayoutManager)) {
            throw new IllegalStateException("NovaRecyclerView only accept INovaLayoutManager");
        }
        mNovaLayoutManager = ((INovaLayoutManager) layoutManager);
        if (mNovaRecyclerAdapter != null) {
            mNovaLayoutManager.init(mNovaRecyclerAdapter);
        }
        super.setLayoutManager(layoutManager);
    }

    @Override
    public void setAdapter(RecyclerView.Adapter adapter) {
        if (!(adapter instanceof NovaRecyclerAdapter)) {
            throw new IllegalStateException("NovaRecyclerView only accept NovaRecyclerAdapter");
        }
        mNovaRecyclerAdapter = (NovaRecyclerAdapter) adapter;
        if (mNovaLayoutManager != null) {
            mNovaLayoutManager.init(mNovaRecyclerAdapter);
        }
        super.setAdapter(mNovaRecyclerAdapter);
    }

    @MainThread
    @Override
    public void setFootLoadMoreEnable(boolean footLoadMoreEnable) {
        if (mFootLoadMoreEnable == footLoadMoreEnable) {
            return;
        }
        mFootLoadMoreEnable = footLoadMoreEnable;
        if (footLoadMoreEnable) {
            removeOnScrollListener(mOnLoadMoreScrollListener);
            addOnScrollListener(mOnLoadMoreScrollListener);
        } else {
            removeOnScrollListener(mOnLoadMoreScrollListener);
        }
    }

    @Override
    public boolean isScrollable() {
        return mNovaLayoutManager != null && mNovaLayoutManager.findFirstCompletelyVisibleItemPosition() > 0;
    }

    @Override
    public void setLoadMoreListener(INovaRecyclerView.LoadMoreListener loadMoreListener) {
        mOnLoadMoreScrollListener.setLoadMoreListener(loadMoreListener);
    }

    /**
     * 控制 RecyclerView 中分割线显示的管理类
     */
    private ItemDecorationManager mItemDecorationManager;

    public final ItemDecorationManager getItemDecorationManager() {
        if (mItemDecorationManager == null) {
            mItemDecorationManager = new ItemDecorationManager(mNovaRecyclerAdapter);
        }
        return mItemDecorationManager;
    }

    @MainThread
    @Override
    public void setItemDecorationEnable(boolean isEnable) {
        if (mItemDecorationEnable == isEnable) {
            return;
        }
        if (isEnable) {
            addItemDecoration(getItemDecorationManager());
        } else {
            removeItemDecoration(getItemDecorationManager());
        }
        mItemDecorationEnable = isEnable;
    }

    @MainThread
    @Override
    public void setItemTouchControlEnable(boolean isEnable) {
        if (isEnable) {
            if (mItemTouchHelper == null) {
                mItemTouchHelper = new NovaItemTouchHelper();
            }
            mItemTouchHelper.attachToRecyclerView(this);
        } else {
            if (mItemTouchHelper != null) {
                mItemTouchHelper.detachToRecyclerView();
            }
        }
    }

    @MainThread
    @Override
    public void setItemDragEnable(boolean isEnable) {
        if (isEnable) {
            if (mItemDragHelper == null) {
                mItemDragHelper = new ItemTouchHelper(new ItemBinderTouchCallback(mNovaRecyclerAdapter));
            }
            mItemDragHelper.attachToRecyclerView(this);
        } else {
            if (mItemDragHelper != null) {
                mItemDragHelper.attachToRecyclerView(null);
            }
        }
    }

    @Override
    public void setItemMvpEnable(boolean isEnable) {
        if (mItemMvpEnable == isEnable) {
            return;
        }
        mItemMvpEnable = isEnable;
        if (isEnable) {
            addOnChildAttachStateChangeListener(mOnChildAttachStateChangeListener);
            super.setRecyclerListener(mRecyclerListener);
        } else {
            removeOnChildAttachStateChangeListener(mOnChildAttachStateChangeListener);
            if (!mRecyclerListener.hasRecyclerListener()) {
                super.setRecyclerListener(null);
            }
        }
        mRecyclerListener.setItemMvpEnable(isEnable);
    }

    @Override
    public void setRecyclerListener(RecyclerListener listener) {
        if (listener == null && !mItemMvpEnable) {
            super.setRecyclerListener(null);
        }
        mRecyclerListener.setRecyclerListener(listener);
        super.setRecyclerListener(mRecyclerListener);
    }

    @MainThread
    @Override
    public void smoothScrollToItem(Object item) {
        int position = mNovaRecyclerAdapter.getItemPositionForItem(item);

        if (position >= 0) {
            super.smoothScrollToPosition(position);
        }
    }

    @MainThread
    @Override
    public void scrollToItem(Object item) {
        int position = mNovaRecyclerAdapter.getItemPositionForItem(item);

        if (position >= 0) {
            LayoutManager layoutManager = getLayoutManager();
            if (layoutManager != null) {
                layoutManager.scrollToPosition(position);
            }
        }
    }

    @Override
    public void startDrag(ItemViewHolder viewHolder) {
        if (mItemDragHelper != null) {
            mItemDragHelper.startDrag(viewHolder);
        }
    }

    /**
     * RecyclerView 的滚动监听器用于触发滚动到底部时加载更多
     */
    private class OnLoadMoreScrollListener extends RecyclerView.OnScrollListener {

        private INovaRecyclerView.LoadMoreListener mLoadMoreListener;

        void setLoadMoreListener(INovaRecyclerView.LoadMoreListener loadMoreListener) {
            mLoadMoreListener = loadMoreListener;
        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            int visibleItemCount = layoutManager.getChildCount();

            boolean triggerCondition = visibleItemCount > 0
                && newState == RecyclerView.SCROLL_STATE_IDLE
                && canTriggerLoadMore(recyclerView);

            if (triggerCondition) {
                onLoadMore();
            }
        }

        private boolean canTriggerLoadMore(RecyclerView recyclerView) {
            LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            int lastVisibleItem = layoutManager.findLastVisibleItemPosition();
            int totalItemCount = layoutManager.getItemCount();
            return totalItemCount - 1 == lastVisibleItem;
        }

        public void onLoadMore() {
            if (mLoadMoreListener != null) {
                mLoadMoreListener.onLoadMore();
            }
        }
    }
}

