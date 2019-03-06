package com.wuzp.rvlib.recyclerview.decorator;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.ColorInt;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * 一个简单的分割线，允许开发者通过 {@link #setDecoratorColor} 设置颜色，通过 {@link #setDecoratorHeight} 设置高度。
 * <p>
 *
 * @author https://github.com/DevAhamed/MultiViewAdapter
 */

public class SimpleItemDecorator implements ItemDecorator {

    private final int DEFAULT_DECORATOR_COLOR = Color.rgb(100, 100, 100);
    private final int DEFAULT_DECORATOR_HEIGHT = 5;
    private int mDecoratorHeight = DEFAULT_DECORATOR_HEIGHT;
    private final Rect mBounds = new Rect();
    private Paint myPaint = new Paint();

    public SimpleItemDecorator() {
        setDecoratorColor(DEFAULT_DECORATOR_COLOR);
    }

    public SimpleItemDecorator(@ColorInt int color, int height) {
        setDecoratorColor(color);
        setDecoratorHeight(height);
    }

    /**
     * @param decoratorColor 希望显示的分割线颜色
     */
    public void setDecoratorColor(int decoratorColor) {
        myPaint.setColor(decoratorColor);
    }

    /**
     * @param decoratorHeight 希望显示的分割线高度
     */
    public void setDecoratorHeight(int decoratorHeight) {
        mDecoratorHeight = decoratorHeight;
    }

    @Override
    public void getItemOffsets(Rect outRect, int position, int positionType) {
        outRect.set(0, 0, 0, mDecoratorHeight);
    }

    @Override
    public void onDraw(Canvas canvas, RecyclerView parent, View child, int position,
        int positionType) {
        if (parent.getLayoutManager() == null) {
            return;
        }
        draw(canvas, parent, child);
    }

    private void draw(Canvas canvas, RecyclerView parent, View child) {
        canvas.save();
        final int left;
        final int right;
        if (parent.getClipToPadding()) {
            left = parent.getPaddingLeft();
            right = parent.getWidth() - parent.getPaddingRight();
            canvas.clipRect(left, parent.getPaddingTop(), right,
                parent.getHeight() - parent.getPaddingBottom());
        } else {
            left = 0;
            right = parent.getWidth();
        }

        parent.getDecoratedBoundsWithMargins(child, mBounds);
        final int bottom = mBounds.bottom + Math.round(ViewCompat.getTranslationY(child));
        final int top = bottom - mDecoratorHeight;

        canvas.drawRect(left, top, right, bottom, myPaint);
        canvas.restore();
    }
}
