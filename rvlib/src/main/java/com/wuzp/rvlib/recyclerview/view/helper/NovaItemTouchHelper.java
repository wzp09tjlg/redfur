package com.wuzp.rvlib.recyclerview.view.helper;

import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import com.wuzp.rvlib.recyclerview.binder.ItemViewHolder;
import com.wuzp.rvlib.recyclerview.view.NovaRecyclerView;

/**
 * //////////////////////////////////Internal Class//////////////////////////////////
 * <p>
 * 对 {@link ItemTouchHelper} 的功能选择性精简和修改
 * <p>
 * {@link ItemTouchHelper} 支持对 {@link  RecyclerView.ViewHolder } 生成的视图进行 swipe 和 drag ,
 * 两者都可以实现在不为 {@link  RecyclerView.ViewHolder } 生成的视图手动设置触摸事件监控的情况下来控制视图滑动，
 * 但使用 swipe 时，滑动超过一定距离，在释放时会触发将视图滚出可见区域的动画，无法通过使用 {@link ItemTouchHelper.Callback}
 * 的子类来避免此问题， 使用 drag 时，需要长按 Item 或为视图设置触摸事件手动触发 {@link ItemTouchHelper#startDrag(RecyclerView.ViewHolder)}
 * 不符合我们的使用情况
 * <p>
 * 为了避免手动的为视图设置触摸事件监控，考虑到 {@link ItemTouchHelper}  的局限性，参考 {@link ItemTouchHelper}
 * 实现 {@link NovaItemTouchHelper} 以和 {@link ItemViewHolder} 配合实现对符合规范的 {@link ItemViewHolder}
 * 完成滑动控制，相比于 {@link ItemTouchHelper} 将所有滑动反馈和动画控制全部下放给业务层来处理，只负责拦截指定
 * {@link ItemViewHolder} 创建的视图上的滑动事件，并将滑动位置变化通知通过 {@link ItemViewHolder#onMove(int, float, int, float)} 传递给业务层。
 */

public class NovaItemTouchHelper extends NovaTouchHelper implements RecyclerView.OnChildAttachStateChangeListener {

    /**
     * NovaItemTouchHelper is in idle state. At this state, either there is no related motion event by
     * the user or latest motion events have not yet triggered move.
     */
    private static final int ACTION_STATE_IDLE = 0;
    /**
     * A View is currently being scroll.
     */
    private static final int ACTION_STATE_SCROLL = 1;

    private int mActionState = ACTION_STATE_IDLE;
    private int mActivePointerId = ACTIVE_POINTER_ID_NONE;

    /**
     * The direction flags
     */
    private int mSelectedFlags;

    /**
     * The coordinates of the selected view at the time it is selected. We record these values
     * when action starts so that we can consistently position it even if LayoutManager moves the
     * View.
     */
    private float mSelectedStartX;
    private float mSelectedStartY;

    /**
     * Currently selected view holder
     */
    public ItemViewHolder mSelected;

    @Override
    protected void onAttachToRecyclerView(NovaRecyclerView recyclerView) {
        recyclerView.addOnChildAttachStateChangeListener(this);
    }

    @Override
    public void onDetachToRecyclerView(NovaRecyclerView recyclerView) {
        recyclerView.removeOnChildAttachStateChangeListener(this);
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent event) {
        final int action = MotionEventCompat.getActionMasked(event);
        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                mActivePointerId = event.getPointerId(0);
                mInitialTouchX = event.getX();
                mInitialTouchY = event.getY();
                break;
            }
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP: {
                mActivePointerId = ACTIVE_POINTER_ID_NONE;
                select(null, ACTION_STATE_IDLE);
                break;
            }
            default: {
                if (mActivePointerId != ACTIVE_POINTER_ID_NONE) {
                    checkSelectForTouch(action, event, event.findPointerIndex(mActivePointerId));
                }
                break;
            }
        }
        return mSelected != null;
    }

    @Override
    public void onTouchEvent(RecyclerView recyclerView, MotionEvent event) {
        if (mActivePointerId == ACTIVE_POINTER_ID_NONE) {
            return;
        }

        final int action = MotionEventCompat.getActionMasked(event);
        final int activePointerIndex = event.findPointerIndex(mActivePointerId);
        checkSelectForTouch(action, event, activePointerIndex);
        ItemViewHolder viewHolder = mSelected;
        if (viewHolder == null) {
            return;
        }
        switch (action) {
            case MotionEvent.ACTION_MOVE: {
                // Find the index of the active pointer and fetch its position
                if (activePointerIndex >= 0) {
                    updateDxDy(event, mSelectedFlags, activePointerIndex);
                    moveViewHolder();
                }
                break;
            }
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                select(null, ACTION_STATE_IDLE);
                mActivePointerId = ACTIVE_POINTER_ID_NONE;
                break;
            case MotionEvent.ACTION_POINTER_UP: {
                final int pointerIndex = MotionEventCompat.getActionIndex(event);
                final int pointerId = event.getPointerId(pointerIndex);
                if (pointerId == mActivePointerId) {
                    // This was our active pointer going up. Choose a new
                    // active pointer and adjust accordingly.
                    final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
                    mActivePointerId = event.getPointerId(newPointerIndex);
                    updateDxDy(event, mSelectedFlags, pointerIndex);
                }
                break;
            }
        }
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        if (!disallowIntercept) {
            return;
        }

        select(null, ACTION_STATE_IDLE);
    }

    /**
     * 确定当前被触摸的符合规范的 {@link ItemViewHolder}
     *
     * @param selected
     * @param actionState
     */
    private void select(RecyclerView.ViewHolder selected, int actionState) {
        if (mSelected == selected && mActionState == actionState) {
            return;
        }

        if (mSelected != null) {
            mSelected.onMoveFinished();
            mSelected = null;
        }

        mActionState = actionState;

        if (selected != null) {
            mSelectedStartX = selected.itemView.getLeft();
            mSelectedStartY = selected.itemView.getTop();
            mSelected = (ItemViewHolder) selected;
            mSelectedFlags = mSelected.getMoveDirections();
        }

        final ViewParent rvParent = mRecyclerView.getParent();
        if (rvParent != null) {
            rvParent.requestDisallowInterceptTouchEvent(mSelected != null);
        }
    }

    /**
     * 将处理后的拖拽信息通知给 {@link ItemViewHolder}, 以供业务层进行相应处理
     */
    private void moveViewHolder() {
        getSelectedDxDy(mTmpPosition);
        float dx = mTmpPosition[0];
        float dy = mTmpPosition[1];

        float absDX = Math.abs(dx);
        float absDY = Math.abs(dy);

        int horizontalDirection = LEFT;
        int verticalDirection = UP;
        float absMoveX = 0;
        float absMoveY = 0;

        if (absDX > 0) {
            if (dx < 0) {
                horizontalDirection = LEFT;
            } else {
                horizontalDirection = RIGHT;
            }

            int maxMoveX = mSelected.getMaxMoveX();

            if (maxMoveX == INFINITE || absDX <= maxMoveX) {
                absMoveX = absDX;
            } else {
                absMoveX = maxMoveX;
            }
        }

        if (absDY > 0) {
            if (dy < 0) {
                verticalDirection = UP;
            } else {
                verticalDirection = DOWN;
            }

            int maxMoveY = mSelected.getMaxMoveY();

            if (maxMoveY == INFINITE || absDX <= maxMoveY) {
                absMoveY = absDY;
            } else {
                absMoveY = maxMoveY;
            }
        }

        mSelected.onMove(horizontalDirection, absMoveX, verticalDirection, absMoveY);
    }

    /**
     * 检索当前触摸事件对应符合规范的 {@link ItemViewHolder}
     *
     * @param action
     * @param motionEvent
     * @param pointerIndex
     * @return
     */
    private boolean checkSelectForTouch(int action, MotionEvent motionEvent, int pointerIndex) {
        if (pointerIndex < 0) {
            return false;
        }
        if (mSelected != null || action != MotionEvent.ACTION_MOVE) {
            return false;
        }
        if (mRecyclerView.getScrollState() == RecyclerView.SCROLL_STATE_DRAGGING) {
            return false;
        }
        final RecyclerView.ViewHolder vh = findTouchedViewHolder(motionEvent);
        if (vh == null || !(vh instanceof ItemViewHolder)) {
            return false;
        }
        ItemViewHolder itemViewHolder = (ItemViewHolder) vh;
        if (!itemViewHolder.isMovable()) {
            return false;
        }

        int moveDirections = itemViewHolder.getMoveDirections();

        // mDx and mDy are only set in allowed directions. We use custom x/y here instead of
        // updateDxDy to avoid swiping if user moves more in the other direction
        final float x = motionEvent.getX(pointerIndex);
        final float y = motionEvent.getY(pointerIndex);

        // Calculate the distance moved
        final float dx = x - mInitialTouchX;
        final float dy = y - mInitialTouchY;
        // swipe target is chose w/o applying flags so it does not really check if swiping in that
        // direction is allowed. This why here, we use mDx mDy to check slope value again.
        final float absDx = Math.abs(dx);
        final float absDy = Math.abs(dy);

        if (absDx < mSlop && absDy < mSlop) {
            return false;
        }
        if (absDx > absDy) {
            if (dx < 0 && (moveDirections & LEFT) == 0) {
                return false;
            }
            if (dx > 0 && (moveDirections & RIGHT) == 0) {
                return false;
            }
        } else {
            if (dy < 0 && (moveDirections & UP) == 0) {
                return false;
            }
            if (dy > 0 && (moveDirections & DOWN) == 0) {
                return false;
            }
        }
        mDx = mDy = 0f;
        mActivePointerId = motionEvent.getPointerId(0);
        select(vh, ACTION_STATE_SCROLL);
        return true;
    }

    /**
     * 检索当前触摸事件触摸位置对应的 {@link ItemViewHolder}
     *
     * @param motionEvent
     * @return
     */
    private RecyclerView.ViewHolder findTouchedViewHolder(MotionEvent motionEvent) {
        final RecyclerView.LayoutManager lm = mRecyclerView.getLayoutManager();
        if (mActivePointerId == ACTIVE_POINTER_ID_NONE) {
            return null;
        }
        final int pointerIndex = motionEvent.findPointerIndex(mActivePointerId);
        final float dx = motionEvent.getX(pointerIndex) - mInitialTouchX;
        final float dy = motionEvent.getY(pointerIndex) - mInitialTouchY;
        final float absDx = Math.abs(dx);
        final float absDy = Math.abs(dy);

        if (absDx < mSlop && absDy < mSlop) {
            return null;
        }
        if (absDx > absDy && lm.canScrollHorizontally()) {
            return null;
        } else if (absDy > absDx && lm.canScrollVertically()) {
            return null;
        }
        View child = findChildView(motionEvent);
        if (child == null) {
            return null;
        }
        return mRecyclerView.getChildViewHolder(child);
    }

    private View findChildView(MotionEvent event) {
        // first check elevated views, if none, then call RV
        final float x = event.getX();
        final float y = event.getY();
        if (mSelected != null) {
            final View selectedView = mSelected.itemView;
            if (hitTest(selectedView, x, y, mSelectedStartX + mDx, mSelectedStartY + mDy)) {
                return selectedView;
            }
        }
        return mRecyclerView.findChildViewUnder(x, y);
    }

    private static boolean hitTest(View child, float x, float y, float left, float top) {
        return x >= left &&
            x <= left + child.getWidth() &&
            y >= top &&
            y <= top + child.getHeight();
    }

    private void updateDxDy(MotionEvent ev, int directionFlags, int pointerIndex) {
        final float x = ev.getX(pointerIndex);
        final float y = ev.getY(pointerIndex);

        // Calculate the distance moved
        mDx = x - mInitialTouchX;
        mDy = y - mInitialTouchY;
        if ((directionFlags & LEFT) == 0) {
            mDx = Math.max(0, mDx);
        }
        if ((directionFlags & RIGHT) == 0) {
            mDx = Math.min(0, mDx);
        }
        if ((directionFlags & UP) == 0) {
            mDy = Math.max(0, mDy);
        }
        if ((directionFlags & DOWN) == 0) {
            mDy = Math.min(0, mDy);
        }
    }

    private void getSelectedDxDy(float[] outPosition) {
        if ((mSelectedFlags & (LEFT | RIGHT)) != 0) {
            outPosition[0] = mSelectedStartX + mDx - mSelected.itemView.getLeft();
        } else {
            outPosition[0] = ViewCompat.getTranslationX(mSelected.itemView);
        }
        if ((mSelectedFlags & (UP | DOWN)) != 0) {
            outPosition[1] = mSelectedStartY + mDy - mSelected.itemView.getTop();
        } else {
            outPosition[1] = ViewCompat.getTranslationY(mSelected.itemView);
        }
    }

    @Override
    public void onChildViewAttachedToWindow(View view) {
    }

    @Override
    public void onChildViewDetachedFromWindow(View view) {
        final RecyclerView.ViewHolder holder = mRecyclerView.getChildViewHolder(view);
        if (holder == null) {
            return;
        }
        if (mSelected != null && holder == mSelected) {
            select(null, ACTION_STATE_IDLE);
        }
    }
}
