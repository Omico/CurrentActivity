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
        this.mContext = context;
    }

    public FloatWindow init(View view, WindowManager.LayoutParams layoutParams) {
        this.mWindowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        this.mView = view;
//        mView.setId(R.id.float_view_layout);
        this.mLayoutParams = layoutParams;
        return this;
    }

    public FloatWindow init(View view, WindowManager.LayoutParams layoutParams, int gravity) {
        this.init(view, layoutParams);
        this.setLayoutParamsGravity(gravity);
        return this;
    }

    public FloatWindow init(View view) {
        this.init(view, getLayoutParams());
        return this;
    }

    public FloatWindow setLayoutParams(int width, int height, int type, int flags, int format) {
        this.mLayoutParams = new WindowManager.LayoutParams(width, height, type, flags, format);
        return this;
    }

    public FloatWindow setLayoutParams(int width, int height, int type, int flags, int format, int gravity) {
        this.setLayoutParams(width, height, type, flags, format);
        this.setLayoutParamsGravity(gravity);
        return this;
    }

    public FloatWindow setLayoutParams(WindowManager.LayoutParams layoutParams) {
        this.mLayoutParams = layoutParams;
        return this;
    }

    public FloatWindow setLayoutParams(WindowManager.LayoutParams layoutParams, int gravity) {
        this.mLayoutParams = layoutParams;
        this.setLayoutParamsGravity(gravity);
        return this;
    }

    public WindowManager.LayoutParams getLayoutParams() {
        return mLayoutParams;
    }

    public FloatWindow setLayoutParamsGravity(int gravity) {
        this.mLayoutParams.gravity = gravity;
        return this;
    }

    public int getLayoutParamsGravity() {
        return mLayoutParams.gravity;
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