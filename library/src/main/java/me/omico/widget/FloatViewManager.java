package me.omico.widget;

import android.content.Context;
import android.view.View;
import android.view.WindowManager;

/**
 * Created by Omico on 2017/2/7.
 */

class FloatViewManager {

    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mLayoutParams;
    private View mView;

    FloatViewManager(Context context, WindowManager.LayoutParams layoutParams, View view) {
        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        mLayoutParams = layoutParams;
        mView = view;
    }

    final void add() {
        mWindowManager.addView(mView, mLayoutParams);
    }

    final void remove() {
        mWindowManager.removeView(mView);
    }
}
