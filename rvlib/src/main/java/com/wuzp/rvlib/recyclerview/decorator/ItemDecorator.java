package com.wuzp.rvlib.recyclerview.decorator;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.wuzp.rvlib.recyclerview.annotation.PositionType;
import com.wuzp.rvlib.recyclerview.binder.ItemBinder;
import com.wuzp.rvlib.recyclerview.data.ChildDataListManager;
import com.wuzp.rvlib.recyclerview.view.NovaRecyclerView;

/**
 * Interface to draw the item decoration for each view binder
 * <p>
 * 使用方式：通过调用 {@link ItemBinder#} 的参数为 ItemDecorator 的构造函数
 * 或使用 {@link ItemBinder} 的实现类实例调用 addDecorator 添加 ItemDecorator 的实现类
 * ，然后调用 {@link NovaRecyclerView#setItemDecorationEnable}
 *
 * @author https://github.com/DevAhamed/MultiViewAdapter.
 */
public interface ItemDecorator {
    /**
     * Denotes that the item is the first element in the {@link ChildDataListManager} by the order of
     * display
     */
    int POSITION_FIRST_ITEM = 16;

    /**
     * Denotes that the item is neither first element nor last element in the {@link ChildDataListManager}.
     */
    int POSITION_MIDDLE_ITEM = 32;

    /**
     * Denotes that the item is the last element in the {@link ChildDataListManager} by the order of
     * display
     */
    int POSITION_LAST_ITEM = 64;

    /**
     * Denotes that the item is lies in the left edge of the grid
     */
    int POSITION_LEFT = 1;

    /**
     * Denotes that the item is lies in the top edge of the grid
     */
    int POSITION_TOP = 2;

    /**
     * Denotes that the item is lies in the right edge of the grid
     */
    int POSITION_RIGHT = 4;

    /**
     * Denotes that the item is lies in the bottom edge of the grid
     */
    int POSITION_BOTTOM = 8;

    /**
     * Denotes that the item is lies in the middle of the grid
     */
    int POSITION_MIDDLE = 0;

    /**
     * Retrieve any offsets for the given position. Each field of <code>outRect</code> specifies
     * the number of pixels that the item view should be inset by, similar to padding or margin.
     * The default implementation sets the bounds of outRect to 0 and returns.
     * <p>
     * <p>
     * If this ItemDecoration does not affect the positioning of item views, it should set
     * all four fields of <code>outRect</code> (left, top, right, bottom) to zero
     * before returning.
     *
     * @param outRect      Rect to receive the output.
     * @param position     index of the element inside the data manager
     * @param positionType denotes whether the item's position. Check {@link PositionType} for more
     *                     info
     */
    void getItemOffsets(Rect outRect, int position, @PositionType int positionType);

    /**
     * Draw any appropriate decorations into the Canvas supplied to the RecyclerView.
     * Any content drawn by this method will be drawn before the item views are drawn,
     * and will thus appear underneath the views.
     *
     * @param canvas       Canvas to draw into
     * @param parent       RecyclerView this ItemDecoration is drawing into
     * @param child        The child for which item decoration is being drawn
     * @param position     index of the element inside the data manager
     * @param positionType denotes whether the item's position. Check {@link PositionType} for more
     *                     info
     */
    void onDraw(Canvas canvas, RecyclerView parent, View child, int position,
        @PositionType int positionType);
}
