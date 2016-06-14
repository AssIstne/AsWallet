package com.assistne.aswallet.home;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.Canvas;

import com.assistne.aswallet.R;
import com.orhanobut.logger.Logger;

public class DividerItemDecoration extends RecyclerView.ItemDecoration {

    private Drawable mDivider;

    public DividerItemDecoration(Context context, AttributeSet attrs) {
        final TypedArray a = context.obtainStyledAttributes(attrs, new int [] { android.R.attr.listDivider });
        mDivider = a.getDrawable(0);
        a.recycle();
    }

    public DividerItemDecoration(Drawable divider) { mDivider = divider; }

    @Override
    public void getItemOffsets (Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        if (mDivider == null) return;
        if (getOrientation(parent) == LinearLayoutManager.VERTICAL) outRect.bottom = mDivider.getIntrinsicHeight();
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (mDivider == null) { super.onDrawOver(c, parent, state); return; }

        if (getOrientation(parent) == LinearLayoutManager.VERTICAL) {
            final int left = 0;
            final int right = parent.getWidth() - parent.getPaddingRight();
            final int childCount = parent.getChildCount();

            for (int i=0; i < childCount; i++) {
                final View child = parent.getChildAt(i);
                final int size = mDivider.getIntrinsicHeight();
                final int top = child.getBottom();
                final int bottom = top + size;
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
            }
        }
    }

    private int getOrientation(RecyclerView parent) {
        if (parent.getLayoutManager() instanceof LinearLayoutManager) {
            LinearLayoutManager layoutManager = (LinearLayoutManager) parent.getLayoutManager();
            return layoutManager.getOrientation();
        } else throw new IllegalStateException("DividerItemDecoration can only be used with a LinearLayoutManager.");
    }

}
