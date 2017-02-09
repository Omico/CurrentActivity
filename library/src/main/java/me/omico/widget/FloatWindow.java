package me.omico.widget;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

public class FloatWindow {

    private View mView;
    private Context mContext;
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mLayoutParams;

    private int moveX, moveY;
    private int deviationAmount = 3;

    private View.OnTouchListener mOnTouchListener;
    private View.OnClickListener mOnClickListener;
    private View.OnLongClickListener mOnLongClickListener;

    public FloatWindow(Context context) {
        this.mContext = context;
    }

    public FloatWindow init(View view, WindowManager.LayoutParams layoutParams) {
        this.mWindowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        this.mView = view;
        this.mLayoutParams = layoutParams;

        if (mOnTouchListener != null) {
            mView.setOnTouchListener(mOnTouchListener);
        } else {
            this.setDefaultFloatWindowGestureListener();
        }
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

    public void updateFloatWindowPosition(int x, int y, int currentX, int currentY) {
        mLayoutParams.x = x - currentX;
        mLayoutParams.y = y - currentY;
        mWindowManager.updateViewLayout(mView, mLayoutParams);
    }

    public FloatWindow setFloatWindowOnTouchListener(View.OnTouchListener onTouchListener) {
        mOnTouchListener = onTouchListener;
        return this;
    }

    public FloatWindow setOnFloatWindowClickListener(View.OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
        return this;
    }

    public FloatWindow setOnFloatWindowLongClickListener(View.OnLongClickListener onLongClickListener) {
        mOnLongClickListener = onLongClickListener;
        return this;
    }

    public int getGestureDeviationAmount() {
        return deviationAmount;
    }

    public FloatWindow setGestureDeviationAmount(int deviationAmount) {
        this.deviationAmount = deviationAmount;
        return this;
    }

    private void setDefaultFloatWindowGestureListener() {
        mView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int startX = (int) event.getRawX();
                int startY = (int) event.getRawY();
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        moveX = (int) event.getX();
                        moveY = (int) event.getY();
                        break;
                    }
                    case MotionEvent.ACTION_MOVE: {
                        updateFloatWindowPosition(startX, startY, moveX, moveY);
                        break;
                    }
                    case MotionEvent.ACTION_UP:
                        int endX = (int) event.getX();
                        int endY = (int) event.getY();
                        if (Math.abs(moveX - endX) < deviationAmount || Math.abs(moveY - endY) < deviationAmount) {
                            if (mOnClickListener != null) {
                                mView.setOnClickListener(mOnClickListener);
                            }
                            if (mOnLongClickListener != null) {
                                mView.setOnLongClickListener(mOnLongClickListener);
                            }
                        }
                        updateFloatWindowPosition(startX, startY, moveX, moveY);
                        break;
                }
                return false;
            }
        });
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