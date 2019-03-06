package com.wuzp.rvlib.recyclerview.view;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.wuzp.rvlib.recyclerview.adapter.NovaRecyclerAdapter;
import com.wuzp.rvlib.recyclerview.annotation.PositionType;
import com.wuzp.rvlib.recyclerview.binder.ItemBinder;
import com.wuzp.rvlib.recyclerview.data.BaseDataManager;
import com.wuzp.rvlib.recyclerview.decorator.ItemDecorator;

import static com.wuzp.rvlib.recyclerview.decorator.ItemDecorator.POSITION_BOTTOM;
import static com.wuzp.rvlib.recyclerview.decorator.ItemDecorator.POSITION_LEFT;
import static com.wuzp.rvlib.recyclerview.decorator.ItemDecorator.POSITION_MIDDLE;
import static com.wuzp.rvlib.recyclerview.decorator.ItemDecorator.POSITION_RIGHT;
import static com.wuzp.rvlib.recyclerview.decorator.ItemDecorator.POSITION_TOP;

/**
 * This is an internal class. Should not be extended by client code. Used to manage the different
 * {@link RecyclerView.ItemDecoration} for different {@link ItemBinder}. It will delegate the {@link
 * RecyclerView.ItemDecoration}
 *
 * @author https://github.com/DevAhamed/MultiViewAdapter,zhengtongyu
 */
public class ItemDecorationManager extends RecyclerView.ItemDecoration {

    private final NovaRecyclerAdapter mAdapter;

    public ItemDecorationManager(NovaRecyclerAdapter adapter) {
        mAdapter = adapter;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
        RecyclerView.State state) {
        boolean isReverseLayout = getIsReverseLayout(parent);
        int adapterPosition = parent.getChildAdapterPosition(view);
        if (adapterPosition < 0) {
            return;
        }
        ItemBinder binder = getBinderForPosition(adapterPosition);
        if (binder == null) {
            return;
        }
        if (binder.isItemDecorationEnabled()) {
            int itemPosition = getItemPositionInDataManager(parent.getChildAdapterPosition(view));
            BaseDataManager baseDataManager = getDataManager(adapterPosition);
            int positionType = getPositionType(parent, adapterPosition, itemPosition,
                baseDataManager.size(), isReverseLayout);
            binder.getItemOffsets(outRect, itemPosition, positionType);
        }
    }

    @Override
    public void onDraw(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
        drawItemDecoration(canvas, parent, false);
    }

    @Override
    public void onDrawOver(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
        drawItemDecoration(canvas, parent, true);
    }

    /**
     * 绘制 Item 的分割线
     *
     * @param drawOver 是否是在 itemView 上绘制
     */
    private void drawItemDecoration(Canvas canvas, RecyclerView parent, boolean drawOver) {
        boolean isReverseLayout = getIsReverseLayout(parent);
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            int adapterPosition = parent.getChildAdapterPosition(child);
            if (adapterPosition < 0) {
                return;
            }
            ItemBinder binder = getBinderForPosition(adapterPosition);
            if (binder == null) {
                continue;
            }
            if (binder.isItemDecorationEnabled()) {
                int itemPosition = getItemPositionInDataManager(adapterPosition);
                int positionType = getPositionType(parent, adapterPosition, itemPosition,
                    getDataManager(adapterPosition).size(), isReverseLayout);
                if (drawOver) {
                    binder.onDrawOver(canvas, parent, child, itemPosition, positionType);
                } else {
                    binder.onDraw(canvas, parent, child, itemPosition, positionType);
                }
            }
        }
    }

    private boolean getIsReverseLayout(RecyclerView parent) {
        return parent.getLayoutManager() instanceof LinearLayoutManager && ((LinearLayoutManager) parent
            .getLayoutManager()).getReverseLayout();
    }

    private @PositionType
    int getPositionType(RecyclerView parent, int adapterPosition,
        int itemPosition, int dataManagerSize, boolean isReverseLayout) {
        if (parent.getLayoutManager() instanceof GridLayoutManager) {
            return getPositionTypeGrid(parent, itemPosition, adapterPosition, dataManagerSize, isReverseLayout);
        } else if (parent.getLayoutManager() instanceof LinearLayoutManager) {
            return getPositionTypeLinear(itemPosition, dataManagerSize, isReverseLayout);
        }
        return POSITION_MIDDLE;
    }

    private @PositionType
    int getPositionTypeGrid(RecyclerView parent, int itemPosition, int adapterPosition,
        int dataManagerSize, boolean isReverseLayout) {
        GridLayoutManager gridLayoutManager = (GridLayoutManager) parent.getLayoutManager();
        int spanSize = gridLayoutManager.getSpanCount();

        int itemPositionType = POSITION_MIDDLE;
        int totalSpanCount = 0;
        boolean isFirstRow = true;

        int beginPosition = adapterPosition - itemPosition;

        for (int looper = beginPosition; looper <= adapterPosition; looper++) {
            int currentSpanCount = gridLayoutManager.getSpanSizeLookup().getSpanSize(looper);
            if (totalSpanCount + currentSpanCount > spanSize) {
                totalSpanCount = currentSpanCount;
                isFirstRow = false;
            } else {
                totalSpanCount += currentSpanCount;
            }

            if (looper == adapterPosition) {
                if (totalSpanCount - currentSpanCount == 0) {
                    itemPositionType |= POSITION_LEFT;
                }
                if (totalSpanCount == spanSize) {
                    itemPositionType |= POSITION_RIGHT;
                }
                if (isFirstRow) {
                    itemPositionType |= isReverseLayout ? POSITION_BOTTOM : POSITION_TOP;
                }
                if (isLastRow(gridLayoutManager, adapterPosition,
                    beginPosition + dataManagerSize, totalSpanCount, spanSize)) {
                    itemPositionType |= isReverseLayout ? POSITION_TOP : POSITION_BOTTOM;
                }
            }
        }
        return itemPositionType;
    }

    private boolean isLastRow(GridLayoutManager gridLayoutManager, int adapterPosition, int endPosition,
        int totalSpanCount, int spanSize) {
        for (int looper = adapterPosition + 1; looper < endPosition; looper++) {
            int currentSpanCount = gridLayoutManager.getSpanSizeLookup().getSpanSize(looper);
            if (totalSpanCount + currentSpanCount > spanSize) {
                return false;
            } else {
                totalSpanCount += currentSpanCount;
            }
        }
        return true;
    }

    private @PositionType
    int getPositionTypeLinear(int itemPosition, int dataManagerSize, boolean isReverseLayout) {
        if (dataManagerSize == 1) {
            int itemPositionType = ItemDecorator.POSITION_FIRST_ITEM;
            itemPositionType |= ItemDecorator.POSITION_LAST_ITEM;
            return itemPositionType;
        }
        boolean isLastItemInDataManager = (itemPosition == dataManagerSize - 1);
        boolean isFirstItem = isReverseLayout ? isLastItemInDataManager : itemPosition == 0;
        boolean isLastItem = isReverseLayout ? itemPosition == 0 : isLastItemInDataManager;
        return isFirstItem ? ItemDecorator.POSITION_FIRST_ITEM
            : isLastItem ? ItemDecorator.POSITION_LAST_ITEM : ItemDecorator.POSITION_MIDDLE_ITEM;
    }

    private int getItemPositionInDataManager(int positionInRV) {
        return mAdapter.getItemPositionInDataManager(positionInRV);
    }

    private BaseDataManager getDataManager(int positionInRV) {
        return mAdapter.getDataManager(positionInRV);
    }

    private ItemBinder getBinderForPosition(int positionInRV) {
        return mAdapter.getBinderForPosition(positionInRV);
    }
}