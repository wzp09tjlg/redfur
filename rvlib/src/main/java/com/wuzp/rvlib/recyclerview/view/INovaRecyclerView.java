package com.wuzp.rvlib.recyclerview.view;

import android.support.annotation.MainThread;
import com.wuzp.rvlib.recyclerview.view.layoutmanager.INovaLayoutManager;

/**
 * 定义 NovaRecyclerView 可以暴露的方法
 *
 */
public interface INovaRecyclerView {

    /**
     * 设置封装后的 NovaLayoutManager
     *
     * @param novaLayoutManager INovaLayoutManager 实现类
     */
    void setNovaLayoutManager(INovaLayoutManager novaLayoutManager);

    /**
     * 设置是否开启上拉加载
     *
     * @param footLoadMoreEnable 是否开启上拉加载
     */
    @MainThread
    void setFootLoadMoreEnable(boolean footLoadMoreEnable);

    /**
     * 设置上拉加载监听器
     *
     * @param loadMoreListener 上拉加载监听器
     */
    void setLoadMoreListener(LoadMoreListener loadMoreListener);

    /**
     * @return 是否可滚动, 即数据是否充满一屏
     */
    boolean isScrollable();

    /**
     * 控制 {@link ItemBinder} 中设置的分割线的显示
     *
     * @param isEnable 是否显示设置的分割线
     */
    @MainThread
    void setItemDecorationEnable(boolean isEnable);

    /**
     * 为 {@link RecyclerView} 添加 item 触摸控制，当 {@link NovaRecyclerAdapter#mBinders} 中的 {@link ItemBinder }
     * 可以生成符合规范的 {@link ItemViewHolder} 时，允许对 {@link ItemViewHolder} 生成的视图进行滑动控制
     *
     * @param isEnable 是否添加 item 触摸控制
     */
    @MainThread
    void setItemTouchControlEnable(boolean isEnable);

    /**
     * 为 {@link RecyclerView} 添加 item 拖拽控制
     *
     * @param isEnable 是否添加 item 拖拽控制
     */
    @MainThread
    void setItemDragEnable(boolean isEnable);

    /**
     * 开启 Item Mvp 功能
     * <p>
     * 会带来额外性能消耗
     *
     * @param isEnable 是都开启 Item Mvp 功能
     */
    void setItemMvpEnable(boolean isEnable);

    /**
     * 获取指定数据对应的 position，并以其作为参数传入到 smoothScrollToPosition 中 smoothScrollToPosition 并不会将 position
     * 对应的 Item 滚至 RecyclerView 顶部，希望实现此功能需要为 LayoutManager 配置 SmoothScroller ，现已为 INovaLayoutManager
     * 提供接口 {@link INovaLayoutManager#setSmoothScroller(RecyclerView.SmoothScroller)} 以配置
     * SmoothScroller
     *
     * @param data 指定的数据
     * @see <a href="https://stackoverflow.com/questions/38416146/recyclerview-smoothscroll-to-position-in-the-center-android">StackOverflow上的讨论</a>
     */
    @MainThread
    void smoothScrollToItem(Object data);

    /**
     * 立刻滚动到指定 data
     *
     * @param data
     */
    @MainThread
    void scrollToItem(Object data);

    /**
     * 上拉加载监听器
     */
    interface LoadMoreListener {
        void onLoadMore();
    }
}

