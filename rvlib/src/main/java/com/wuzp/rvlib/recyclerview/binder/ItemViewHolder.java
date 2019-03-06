package com.wuzp.rvlib.recyclerview.binder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.wuzp.rvlib.recyclerview.view.helper.NovaItemTouchHelper;

public class ItemViewHolder<T> extends RecyclerView.ViewHolder {

    private int mPositionInManager;
    private T mItem;

    public ItemViewHolder(View itemView) {
        super(itemView);
    }

    public T getItem() {
        return mItem;
    }

    public void setItem(T item) {
        mItem = item;
    }

    public int getPositionInManager() {
        return mPositionInManager;
    }

    public void setPositionInManager(int positionInManager) {
        mPositionInManager = positionInManager;
    }

    protected <S> S findViewById(int id) {
        return (S) itemView.findViewById(id);
    }

    /**
     * 用于判断该 {@link ItemViewHolder} 生成的 item 是否可以接收移动触摸回调
     * 当滚动方向为 {@link NovaItemTouchHelper#NONE } 时不接收移动触摸回调，业务层可以继承并增加自己的逻辑，
     * 比如动画过程中禁止滚动
     *
     * @return
     */
    public boolean isMovable() {
        return getMoveDirections() != NovaItemTouchHelper.NONE;
    }

    /**
     * 需要支持触摸事件回调的 {@link ItemViewHolder} 需要覆写该方法来表明希望接收哪些方向上的触摸操作
     * <p>
     * 当 {@link RecyclerView} 支持水平滑动时 {@link RecyclerView.LayoutManager#canScrollHorizontally}
     * 可使用 {@link NovaItemTouchHelper#UP } {@link NovaItemTouchHelper#DOWN }
     * 当 {@link RecyclerView} 支持垂直滑动时 {@link RecyclerView.LayoutManager#canScrollVertically}
     * 可使用 {@link NovaItemTouchHelper#LEFT } {@link NovaItemTouchHelper#RIGHT }
     *
     * @return {@link NovaItemTouchHelper#UP } {@link NovaItemTouchHelper#DOWN } {@link NovaItemTouchHelper#LEFT }
     * {@link NovaItemTouchHelper#RIGHT } 中任意值，或将多组值通过 | 运算组合后返回
     */
    public int getMoveDirections() {
        return NovaItemTouchHelper.NONE;
    }

    /**
     * 需要支持拖拽事件回调的 {@link ItemViewHolder} 需要覆写该方法来表明希望接收哪些方向上的拖拽操作
     *
     * @return {@link NovaItemTouchHelper#UP } {@link NovaItemTouchHelper#DOWN } {@link NovaItemTouchHelper#LEFT }
     * {@link NovaItemTouchHelper#RIGHT } 中任意值，或将多组值通过 | 运算组合后返回
     */
    public int getDragDirections() {
        return NovaItemTouchHelper.NONE;
    }

    /**
     * 用户对 {@link ItemViewHolder} 创建的 {@link View} 进行的移动触摸事件的回调方法 (不会被拖拽操作触发)
     *
     * @param horizontalDirection 水平移动的方向
     * @param absMoveX            x 轴上移动的距离
     * @param verticalDirection   垂直移动的方向
     * @param absMoveY            y 轴上移动的距离
     */
    public void onMove(int horizontalDirection, float absMoveX, int verticalDirection, float absMoveY) {
    }

    /**
     * 用户的移动触摸操作结束后的回调(不会被拖拽操作触发)
     */
    public void onMoveFinished() {
    }

    /**
     * @return X 轴最远移动的距离，{@link NovaItemTouchHelper#INFINITE} 代表无限远
     */
    public int getMaxMoveX() {
        return NovaItemTouchHelper.INFINITE;
    }

    /**
     * @return Y 轴最远移动的距离，{@link NovaItemTouchHelper#INFINITE} 代表无限远
     */
    public int getMaxMoveY() {
        return NovaItemTouchHelper.INFINITE;
    }
}
