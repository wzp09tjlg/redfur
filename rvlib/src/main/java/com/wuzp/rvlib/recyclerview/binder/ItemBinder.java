package com.wuzp.rvlib.recyclerview.binder;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.wuzp.rvlib.recyclerview.annotation.PositionType;
import com.wuzp.rvlib.recyclerview.decorator.ItemDecorator;
import com.wuzp.rvlib.recyclerview.decorator.ItemDecoratorOver;
import com.wuzp.rvlib.recyclerview.listener.ItemDragListener;

import java.util.ArrayList;
import java.util.List;

/**
 * @author https://github.com/DevAhamed/MultiViewAdapter
 */
public abstract class ItemBinder<T, VH extends ItemViewHolder<T>> {

    private List<ItemDecorator> mItemDecorators;
    private ItemDragListener mItemDragListener;

    public ItemBinder() {
    }

    public ItemBinder(@NonNull ItemDecorator itemDecorator) {
        addDecorator(itemDecorator);
    }

    /**
     * @param inflater LayoutInflater to inflate view
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     *                 an adapter position.
     * @return A new ItemViewHolder that holds a View for the given {@link ItemBinder}.
     */
    public abstract VH create(LayoutInflater inflater, ViewGroup parent);

    /**
     * NovaRecyclerAdapter 收到 onBindViewHolder 回调时调用的方法，用于通知业务层进行数据和视图的绑定
     */
    public final void bindViewHolder(VH holder, T item, int positionInManager) {
        holder.setItem(item);
        holder.setPositionInManager(positionInManager);
        bind(holder, item);
    }

    /**
     * 通知业务层进行数据和视图的绑定
     *
     * @param holder holder The ItemViewHolder which should be updated to represent the contents of
     *               the
     *               item at the given position in the data set.
     * @param item   The object which holds the data
     */
    public abstract void bind(VH holder, T item);

    /**
     * @param item The object from the data set
     * @return boolean value which determines whether the {@link ItemBinder} can bind the
     * item to the ItemViewHolder
     */
    public final boolean canBindData(Object item) {
        return bindDataType().equals(item.getClass()) && extraCanBindCondition((T) item);
    }

    public abstract Class<T> bindDataType();

    /**
     * binder 能否绑定一个 Model 的附加判断条件
     *
     * @param item
     * @return
     */
    public boolean extraCanBindCondition(T item) {
        return true;
    }

    /**
     * 为 ItemBinder 创建的 View 添加分割线 {@link ItemDecorator}
     *
     * @param itemDecorator 添加的分割线
     */
    public final void addDecorator(@NonNull ItemDecorator itemDecorator) {
        addDecorator(itemDecorator, -1);
    }

    /**
     * 允许通过设置优先级指定添加的 {@link ItemDecorator} 在 {@link #mItemDecorators} 的顺序
     *
     * @param itemDecorator 添加的分割线
     * @param priority      优先级，如果大于0且小于 {@link #mItemDecorators} ，则添加在 {@link #mItemDecorators} 中
     *                      优先级指定的位置，否则添加在 {@link #mItemDecorators} 末尾
     */
    public final void addDecorator(@NonNull ItemDecorator itemDecorator, int priority) {
        if (null == mItemDecorators) {
            mItemDecorators = new ArrayList<>();
        }
        if (priority >= 0 && mItemDecorators.size() > priority) {
            mItemDecorators.add(priority, itemDecorator);
        } else {
            mItemDecorators.add(itemDecorator);
        }
    }

    /**
     * 用于显示 Grid 布局
     *
     * @return 视图在一行中显示的列数
     */
    public int getColumnCount() {
        return 1;
    }

    /**
     * 设置 ItemDragListener，用于触发 NovaRecyclerView 中 Item 的拖拽
     *
     * @return
     */
    public final void setItemDragListener(ItemDragListener itemDragListener) {
        mItemDragListener = itemDragListener;
    }

    public final void startDrag(ItemViewHolder itemViewHolder) {
        if (mItemDragListener != null) {
            mItemDragListener.startDrag(itemViewHolder);
        }
    }

    ///////////////////////////////////////////
    /////////// Internal API ahead. ///////////
    ///////////////////////////////////////////

    public boolean isItemDecorationEnabled() {
        return mItemDecorators != null;
    }

    public void getItemOffsets(Rect outRect, int position, @PositionType int positionType) {
        if (null == mItemDecorators) {
            return;
        }
        for (ItemDecorator itemDecorator : mItemDecorators) {
            itemDecorator.getItemOffsets(outRect, position, positionType);
        }
    }

    public void onDraw(Canvas canvas, RecyclerView parent, View child, int position,
        @PositionType int positionType) {
        if (null == mItemDecorators) {
            return;
        }
        for (ItemDecorator itemDecorator : mItemDecorators) {
            itemDecorator.onDraw(canvas, parent, child, position, positionType);
        }
    }

    public void onDrawOver(Canvas canvas, RecyclerView parent, View child, int position,
        @PositionType int positionType) {
        if (null == mItemDecorators) {
            return;
        }
        for (ItemDecorator itemDecorator : mItemDecorators) {
            if (itemDecorator instanceof ItemDecoratorOver) {
                ((ItemDecoratorOver) itemDecorator).onDrawOver(canvas, parent, child, position, positionType);
            }
        }
    }
}
