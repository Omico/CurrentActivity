package me.omico.widget;

import android.content.Context;
import android.view.View;
import android.view.WindowManager;

public class FloatWindow {

    private View mView;
    private Context mContext;
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mLayoutParams;

    public FloatWindow(Context context) {
        mContext = context;
    }

    public FloatWindow init(View view, WindowManager.LayoutParams layoutParams) {
        mWindowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        mView = view;
//        mView.setId(R.id.float_view_layout);
        mLayoutParams = layoutParams;
        return this;
    }

    public void attach() {
        mWindowManager.addView(mView, mLayoutParams);
    }

    public void detach() {
        mWindowManager.removeView(mView);
    }

    public void show() {
        mView.setVisibility(View.VISIBLE);
    }

    public void hide() {
        mView.setVisibility(View.GONE);
    }
}