package com.wuzp.rvlib.recyclerview.view.layoutmanager;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.wuzp.rvlib.recyclerview.adapter.NovaRecyclerAdapter;

/**
 * 支持吸顶功能的 GridLayoutManager
 */
public class NovaGridLayoutManager extends GridLayoutManager implements INovaLayoutManager {

    private RecyclerView.SmoothScroller mSmoothScroller;
    private NovaLayoutManagerDelegate mNovaLayoutManagerDelegate;

    public NovaGridLayoutManager(Context context) {
        this(context, VERTICAL, false);
    }

    public NovaGridLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, 1, orientation, reverseLayout);
        mNovaLayoutManagerDelegate = new NovaLayoutManagerDelegate();
    }

    @Override
    public void init(@NonNull NovaRecyclerAdapter adapter) {
        mNovaLayoutManagerDelegate.init(this, adapter);
        setSpanCount(adapter.getSpanCount());
        setSpanSizeLookup(adapter.getSpanSizeLookup());
    }

    /**
     * 吸顶效果会向视图层级里面注入视图
     * 因此需要释放资源
     */
    @Override
    public void release() {
        mNovaLayoutManagerDelegate.release();
    }

    /**
     * Enable or disable elevation for Sticky Headers.
     * <p>
     * If you want to specify a specific amount of elevation, use
     * {@link NovaGridLayoutManager#elevateHeaders(int)}
     *
     * @param elevateHeaders Enable Sticky Header elevation. Default is false.
     */
    public void elevateHeaders(boolean elevateHeaders) {
        mNovaLayoutManagerDelegate.elevateHeaders(elevateHeaders);
    }

    /**
     * Enable Sticky Header elevation with a specific amount.
     *
     * @param dp elevation in dp
     */
    public void elevateHeaders(int dp) {
        mNovaLayoutManagerDelegate.elevateHeaders(dp);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        super.onLayoutChildren(recycler, state);
        mNovaLayoutManagerDelegate.onLayoutChildren(recycler, state);
    }

    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler,
        RecyclerView.State state) {
        int scroll = super.scrollVerticallyBy(dy, recycler, state);
        if (Math.abs(scroll) > 0) {
            mNovaLayoutManagerDelegate.onScroll();
        }
        return scroll;
    }

    @Override
    public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler,
        RecyclerView.State state) {
        int scroll = super.scrollHorizontallyBy(dx, recycler, state);
        if (Math.abs(scroll) > 0) {
            mNovaLayoutManagerDelegate.onScroll();
        }
        return scroll;
    }

    @Override
    public void removeAndRecycleAllViews(RecyclerView.Recycler recycler) {
        super.removeAndRecycleAllViews(recycler);
        mNovaLayoutManagerDelegate.removeAndRecycleAllViews();
    }

    @Override
    public void onAttachedToWindow(RecyclerView view) {
        mNovaLayoutManagerDelegate.onAttachedToWindow(view);
        super.onAttachedToWindow(view);
    }

    @Override
    public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state,
        int position) {
        if (mSmoothScroller == null) {
            super.smoothScrollToPosition(recyclerView, state, position);
            return;
        }
        mSmoothScroller.setTargetPosition(position);
        startSmoothScroll(mSmoothScroller);
    }

    @Override
    public void setSmoothScroller(RecyclerView.SmoothScroller smoothScroller) {
        mSmoothScroller = smoothScroller;
    }
}
