package com.assistne.aswallet.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

/**
 * Created by assistne on 16/1/6.
 */
public class SlideLayout extends LinearLayout {
    private int mParentHeight;
    public SlideLayout(Context context) {
        super(context);
    }

    public SlideLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SlideLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public void setDeltaY(float deltaY) {
        if (mParentHeight == 0 && getParent() != null) {
            mParentHeight = ((ViewGroup)getParent()).getHeight();
        }
        deltaY = deltaY > 1 ? 1: deltaY;
        setTranslationY(mParentHeight * deltaY);
    }
}
