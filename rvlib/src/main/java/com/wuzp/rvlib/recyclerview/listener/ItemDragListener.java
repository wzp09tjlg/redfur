package com.wuzp.rvlib.recyclerview.listener;

import com.wuzp.rvlib.recyclerview.binder.ItemViewHolder;

/**
 * @author wuzhenpeng03
 */
public interface ItemDragListener {
    /**
     * 触发对 item 的拖拽操作
     *
     * @param viewHolder 需要被拖拽的 ItemViewHolder
     */
    void startDrag(ItemViewHolder viewHolder);
}
