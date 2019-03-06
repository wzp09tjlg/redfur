package com.wuzp.rvlib.recyclerview.view.layoutmanager;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.wuzp.rvlib.recyclerview.adapter.NovaRecyclerAdapter;
import com.wuzp.rvlib.recyclerview.mark.StickyHeader;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 代理处理 INovaLayoutManager 实现类中实现相同的方法
 */

class NovaLayoutManagerDelegate {

    private List<Integer> headerPositions = new ArrayList<>();

    private ViewRetriever.RecyclerViewRetriever viewRetriever;

    private StickyHeaderPositioner positioner;

    private INovaLayoutManager mNovaLayoutManager;
    private NovaRecyclerAdapter mAdapter;

    private int headerElevation = StickyHeaderPositioner.NO_ELEVATION;

    void init(INovaLayoutManager novaLayoutManager, @NonNull NovaRecyclerAdapter adapter) {
        mNovaLayoutManager = novaLayoutManager;
        mAdapter = adapter;
    }

    /**
     * 吸顶效果会向视图层级里面注入视图
     * 因此需要释放资源
     */
    void release() {
        if (positioner != null) {
            positioner.reset(LinearLayoutManager.VERTICAL);
        }
    }

    /**
     * Enable or disable elevation for Sticky Headers.
     * <p>
     * If you want to specify a specific amount of elevation, use
     * {@link NovaGridLayoutManager#elevateHeaders(int)}
     *
     * @param elevateHeaders Enable Sticky Header elevation. Default is false.
     */
    void elevateHeaders(boolean elevateHeaders) {
        this.headerElevation = elevateHeaders ?
            StickyHeaderPositioner.DEFAULT_ELEVATION : StickyHeaderPositioner.NO_ELEVATION;
        elevateHeaders(headerElevation);
    }

    /**
     * Enable Sticky Header elevation with a specific amount.
     *
     * @param dp elevation in dp
     */
    void elevateHeaders(int dp) {
        this.headerElevation = dp;
        if (positioner != null) {
            positioner.setElevateHeaders(dp);
        }
    }

    void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        cacheHeaderPositions();
        if (positioner != null) {
            runPositionerInit();
        }
    }

    void onScroll() {
        if (positioner != null) {
            positioner.updateHeaderState(mNovaLayoutManager.findFirstVisibleItemPosition(),
                getVisibleHeaders(), viewRetriever);
        }
    }

    void removeAndRecycleAllViews() {
        if (positioner != null) {
            positioner.clearHeader();
        }
    }

    void onAttachedToWindow(RecyclerView view) {
        if (viewRetriever == null) {
            viewRetriever = new ViewRetriever.RecyclerViewRetriever(view);
        }
        if (positioner == null) {
            positioner = new StickyHeaderPositioner(view);
        }
        positioner.setElevateHeaders(headerElevation);
        if (headerPositions.size() > 0) {
            // Layout has already happened and header positions are cached. Catch positioner up.
            positioner.setHeaderPositions(headerPositions);
            runPositionerInit();
        }
    }

    private void runPositionerInit() {
        positioner.reset(mNovaLayoutManager.getOrientation());
        positioner.updateHeaderState(mNovaLayoutManager.findFirstVisibleItemPosition(),
            getVisibleHeaders(), viewRetriever);
    }

    private Map<Integer, View> getVisibleHeaders() {
        Map<Integer, View> visibleHeaders = new LinkedHashMap<>();

        for (int i = 0; i < mNovaLayoutManager.getChildCount(); i++) {
            View view = mNovaLayoutManager.getChildAt(i);
            int dataPosition = mNovaLayoutManager.getPosition(view);
            if (headerPositions.contains(dataPosition)) {
                visibleHeaders.put(dataPosition, view);
            }
        }
        return visibleHeaders;
    }

    private void cacheHeaderPositions() {
        headerPositions.clear();

        //        List<?> adapterData = headerHandler.getAdapterData();
        //        if (adapterData == null) {
        //            if (positioner != null) {
        //                positioner.setHeaderPositions(headerPositions);
        //            }
        //            return;
        //        }

        int startPosition = 0;

        for (int i = startPosition; i < mAdapter.getItemCount(); i++) {
            if (i >= startPosition + mAdapter.getItemCount()) {
                continue;
            }

            if (mAdapter.getItem(i - startPosition) instanceof StickyHeader) {
                headerPositions.add(i);
            }
        }

        if (positioner != null) {
            positioner.setHeaderPositions(headerPositions);
        }
    }
}
