package com.wuzp.rvlib.recyclerview.view.layoutmanager;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.wuzp.rvlib.recyclerview.adapter.NovaRecyclerAdapter;

/**
 * 暴露 LayoutManager 可以被调用的方法
 *
 */
public interface INovaLayoutManager {

    /**
     * 初始化
     *
     * @param novaRecyclerAdapter NovaRecyclerAdapter 实例，用于为 NovaGridLayoutManager 提供 SpanCount 和对应的 SpanSizeLookup
     *                            及提供对吸顶能力的支持
     */
    void init(NovaRecyclerAdapter novaRecyclerAdapter);

    /**
     * 吸顶效果会向视图层级里面注入视图，因此需要调用此方法以释放资源
     */
    void release();

    /**
     * 为 INovaLayoutManager 设置自定义的 SmoothScroller
     *
     * @param smoothScroller 自定义的 SmoothScroller
     */
    void setSmoothScroller(RecyclerView.SmoothScroller smoothScroller);

    /**
     * {@link LinearLayoutManager#getOrientation}
     */
    int getOrientation();

    /**
     * {@link LinearLayoutManager#getChildCount}
     */
    int getChildCount();

    /**
     * {@link LinearLayoutManager#getChildAt}
     */
    View getChildAt(int index);

    /**
     * {@link LinearLayoutManager#getPosition}
     */
    int getPosition(View view);

    /**
     * {@link LinearLayoutManager#findFirstVisibleItemPosition}
     */
    int findFirstVisibleItemPosition();

    /**
     * {@link LinearLayoutManager#findFirstCompletelyVisibleItemPosition}
     */
    int findFirstCompletelyVisibleItemPosition();
}
