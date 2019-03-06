package com.wuzp.rvlib.recyclerview.view;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import com.wuzp.rvlib.recyclerview.adapter.NovaRecyclerAdapter;
import com.wuzp.rvlib.recyclerview.binder.ItemViewHolder;

/**
 * 用于处理 ItemTouchHelper 拖拽回调的类
 *
 */

class ItemBinderTouchCallback extends ItemTouchHelper.Callback {

    private final NovaRecyclerAdapter mAdapter;

    ItemBinderTouchCallback(NovaRecyclerAdapter adapter) {
        mAdapter = adapter;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return false;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        if (viewHolder instanceof ItemViewHolder) {
            ItemViewHolder itemViewHolder = (ItemViewHolder) viewHolder;
            final int dragFlags = itemViewHolder.getDragDirections();
            final int swipeFlags = 0;
            return makeMovementFlags(dragFlags, swipeFlags);
        }
        return -1;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder source,
        RecyclerView.ViewHolder target) {
        if (source.getItemViewType() != target.getItemViewType()) {
            return false;
        }
        mAdapter.moveData(source.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
    }
}