package com.wuzp.rvlib.recyclerview.view.helper;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.ViewConfiguration;
import com.wuzp.rvlib.recyclerview.view.NovaRecyclerView;

/**
 * 用于协助处理 {@link NovaRecyclerView} 触摸事件的基类
 *
 */
abstract class NovaTouchHelper implements RecyclerView.OnItemTouchListener {

    /** 用于表示选中的 View 可以移动无限远 */
    public static final int INFINITE = -1;

    /** No direction, disable for scroll. */
    public static final int NONE = 0;
    /** Up direction, used for scroll control. */
    public static final int UP = 1;
    /** Down direction, used for scroll control. */
    public static final int DOWN = 1 << 1;
    /** Left direction, used for scroll control. */
    public static final int LEFT = 1 << 2;
    /** Right direction, used for scroll control. */
    public static final int RIGHT = 1 << 3;

    protected static final int ACTIVE_POINTER_ID_NONE = -1;

    protected int mSlop;

    /** The reference coordinates for the action start. this is the initial touch point. */
    protected float mInitialTouchX;
    protected float mInitialTouchY;

    /** The diff between the last event and initial touch. */
    protected float mDx;
    protected float mDy;

    /** Re-use array to calculate dx dy for a ViewHolder */
    protected final float[] mTmpPosition = new float[2];

    protected NovaRecyclerView mRecyclerView;

    /**
     * Attaches the NovaTouchHelper to the provided RecyclerView. If TouchHelper is already
     * attached to a RecyclerView, it will first detach from the previous one. You can call this
     * method with {@code null} to detach it from the current RecyclerView.
     *
     * @param recyclerView The RecyclerView instance to which you want to add this helper or
     *                     {@code null} if you want to remove NovaItemTouchHelper from the current
     *                     RecyclerView.
     */
    public void attachToRecyclerView(@Nullable NovaRecyclerView recyclerView) {
        if (mRecyclerView == recyclerView) {
            return;
        }
        detachToRecyclerView();
        mRecyclerView = recyclerView;
        if (mRecyclerView != null) {
            mSlop = ViewConfiguration.get(mRecyclerView.getContext()).getScaledTouchSlop();
            mRecyclerView.addOnItemTouchListener(this);
            onAttachToRecyclerView(recyclerView);
        }
    }

    public void detachToRecyclerView() {
        if (mRecyclerView != null) {
            mRecyclerView.removeOnItemTouchListener(this);
            onDetachToRecyclerView(mRecyclerView);
        }
        mRecyclerView = null;
    }

    protected void onAttachToRecyclerView(NovaRecyclerView recyclerView) {
    }

    protected void onDetachToRecyclerView(NovaRecyclerView recyclerView) {
    }
}
