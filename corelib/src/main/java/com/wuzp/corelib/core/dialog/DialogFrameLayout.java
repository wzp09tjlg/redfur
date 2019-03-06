package com.wuzp.corelib.core.dialog;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

public class DialogFrameLayout extends FrameLayout {

    private int mDialogCount = 0;
    private Animator frameAnimator;

    public DialogFrameLayout(@NonNull Context context) {
        super(context);
    }

    public DialogFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DialogFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void onDialogChange(@Nullable Dialog to, @Nullable Dialog from, boolean isPush) {
        int count = mDialogCount;
        if (isPush) {
            count ++;
        } else {
            count --;
        }
        onDialogCountChanged(mDialogCount, count, isPush);
        mDialogCount = count;
    }

    protected void onDialogCountChanged(int src, int dest, boolean isPush) {
        AnimatorSet animator = null;
        if (dest == 1 && isPush) {
            if (frameAnimator != null) {
                frameAnimator.end();
                frameAnimator = null;
            }
            setAlpha(0);
            setBackgroundColor(Color.parseColor("#AD333740"));
            animator = new AnimatorSet();
            float start = getAlpha();
            animator.play(ObjectAnimator.ofFloat(this, View.ALPHA, start, 1));
        } else if (dest == 0 && !isPush) {
            if (frameAnimator != null) {
                frameAnimator.end();
                frameAnimator = null;
            }
            animator = new AnimatorSet();
            float start = getAlpha();
            animator.play(ObjectAnimator.ofFloat(this, View.ALPHA, start, 0));
        }

        if (frameAnimator == null && animator != null) {
            frameAnimator = animator;
            frameAnimator.setDuration(400);
            frameAnimator.start();
        }
    }

}

