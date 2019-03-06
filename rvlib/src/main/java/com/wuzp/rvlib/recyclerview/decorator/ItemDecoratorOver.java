package com.wuzp.rvlib.recyclerview.decorator;

import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.wuzp.rvlib.recyclerview.annotation.PositionType;

public interface ItemDecoratorOver extends ItemDecorator {

    /**
     * Draw any appropriate decorations into the Canvas supplied to the RecyclerView.
     * Any content drawn by this method will be drawn after the item views are drawn
     * and will thus appear over the views.
     *
     * @param canvas       Canvas to draw into
     * @param parent       RecyclerView this ItemDecoration is drawing into
     * @param child        The child for which item decoration is being drawn
     * @param position     index of the element inside the data manager
     * @param positionType denotes whether the item's position. Check {@link PositionType} for more
     *                     info
     */
    void onDrawOver(Canvas canvas, RecyclerView parent, View child, int position,
        @PositionType int positionType);
}
