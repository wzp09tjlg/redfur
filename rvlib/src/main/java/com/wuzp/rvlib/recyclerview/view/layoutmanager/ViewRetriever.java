package com.wuzp.rvlib.recyclerview.view.layoutmanager;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import com.wuzp.rvlib.recyclerview.view.NovaRecyclerView;

/**
 * @author wuzhenpeng03
 */
interface ViewRetriever {

    RecyclerView.ViewHolder getViewHolderForPosition(int headerPositionToShow);

    final class RecyclerViewRetriever implements ViewRetriever {

        private final NovaRecyclerView recyclerView;

        private RecyclerView.ViewHolder currentViewHolder;
        private int currentViewType;

        RecyclerViewRetriever(RecyclerView recyclerView) {
            this.recyclerView = (NovaRecyclerView) recyclerView;
            this.currentViewType = -1;
        }

        @Override
        public RecyclerView.ViewHolder getViewHolderForPosition(int position) {
            int itemViewType = recyclerView.getAdapter().getItemViewType(position);
            if (currentViewType != itemViewType) {
                currentViewType = itemViewType;
                currentViewHolder = recyclerView.getAdapter().createViewHolder(
                    (ViewGroup) recyclerView.getParent(), currentViewType);
            }
            return currentViewHolder;
        }
    }
}
