package com.tangramdemo;

import android.content.Context;

/**
 * @author liyongjian
 * @date 2019-08-12.
 * Description：
 */
public class BaseUtil {
    public static int dp2px(Context context, float dipValue) {
        if (context == null)
            return (int) (dipValue * 1.5);
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
}
