package me.omico.widget;

import android.content.Context;
import android.view.View;
import android.view.WindowManager;

public class FloatView extends View {

    private View mView;
    private Context mContext;
    private FloatViewManager mFloatViewManager;

    public FloatView(Context context) {
        super(context);
        mContext = context;
    }

    public FloatView init(View view, WindowManager.LayoutParams layoutParams) {
        mView = view;
//        mView.setId(R.id.float_view_layout);
        mFloatViewManager = new FloatViewManager(mContext, layoutParams, mView);
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
        mFloatViewManager.add();
    }

    public void remove() {
        mFloatViewManager.remove();
    }

    public void show() {
        if (mView.getVisibility() == View.GONE) mView.setVisibility(View.VISIBLE);
    }

    public void hide() {
        if (mView.getVisibility() == View.VISIBLE) mView.setVisibility(View.GONE);
    }
}
