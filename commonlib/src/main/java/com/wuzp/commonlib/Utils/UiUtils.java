package com.wuzp.commonlib.Utils;

import android.content.Context;

/**
 * 视图相关的工具类
 * <p>
 * dp2px 和 sp2px
 *
 * @author wuzhenpeng03
 */
public class UiUtils {

    private UiUtils() {
    }

    public static int dip2px(Context context, float dipValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5F);
    }

    public static int sp2px(Context context, float spValue) {
        float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5F);
    }
}
