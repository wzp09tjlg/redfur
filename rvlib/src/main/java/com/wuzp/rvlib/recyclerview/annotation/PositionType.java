package com.wuzp.rvlib.recyclerview.annotation;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.wuzp.rvlib.recyclerview.decorator.ItemDecorator.POSITION_BOTTOM;
import static com.wuzp.rvlib.recyclerview.decorator.ItemDecorator.POSITION_FIRST_ITEM;
import static com.wuzp.rvlib.recyclerview.decorator.ItemDecorator.POSITION_LAST_ITEM;
import static com.wuzp.rvlib.recyclerview.decorator.ItemDecorator.POSITION_LEFT;
import static com.wuzp.rvlib.recyclerview.decorator.ItemDecorator.POSITION_MIDDLE;
import static com.wuzp.rvlib.recyclerview.decorator.ItemDecorator.POSITION_MIDDLE_ITEM;
import static com.wuzp.rvlib.recyclerview.decorator.ItemDecorator.POSITION_RIGHT;
import static com.wuzp.rvlib.recyclerview.decorator.ItemDecorator.POSITION_TOP;

/**
 * PositionType can be used to resolve whether the item is first/last element in the {@link
 * LinearLayoutManager} in the order of display.
 * <p>
 * If adapter uses {@link GridLayoutManager} PositionType refers to relative position of item in the
 * grid.
 *
 * @author https://github.com/DevAhamed/MultiViewAdapter
 */
@Retention(RetentionPolicy.SOURCE)
@IntDef({
    POSITION_TOP, POSITION_LEFT, POSITION_MIDDLE, POSITION_RIGHT, POSITION_BOTTOM,
    POSITION_FIRST_ITEM, POSITION_MIDDLE_ITEM, POSITION_LAST_ITEM
})
public @interface PositionType {

}
