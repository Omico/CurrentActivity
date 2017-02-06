package me.omico.currentactivity.ui.widget;

import android.content.Context;
import android.view.View;
import android.view.WindowManager;

import me.omico.currentactivity.R;

public final class FloatView {

    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mLayoutParams;
    private View mView;

    public FloatView(Context context) {
        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    }

    public FloatView init(View view, WindowManager.LayoutParams layoutParams) {
        mView = view;
//        mView.setId(R.id.float_view_layout);
        mLayoutParams = layoutParams;
        return this;
    }

    public FloatView setOnFloatViewClickListener(final View.OnClickListener onClickListener) {
        if (onClickListener == null) {
            mView.setOnClickListener(null);
        } else {
            mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickListener.onClick(v);
                }
            });
        }
        return this;
    }

    public FloatView setOnFloatViewLongClickListener(final boolean held, final View.OnLongClickListener onLongClickListener) {
        if (onLongClickListener == null) {
            mView.setOnLongClickListener(null);
        } else {
            mView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    onLongClickListener.onLongClick(v);
                    return held;
                }
            });
        }
        return this;
    }

    public void add() {
        mWindowManager.addView(mView, mLayoutParams);
    }

    public void remove() {
        mWindowManager.removeView(mView);
    }

    public void show() {
        if (mView.getVisibility() == View.GONE) mView.setVisibility(View.VISIBLE);
    }

    public void hide() {
        if (mView.getVisibility() == View.VISIBLE) mView.setVisibility(View.GONE);
    }
}
