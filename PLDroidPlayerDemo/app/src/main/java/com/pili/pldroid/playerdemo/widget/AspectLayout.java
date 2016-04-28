package com.pili.pldroid.playerdemo.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.RelativeLayout;

import com.pili.pldroid.player.common.Util;

/**
 * Created by jerikc on 15/11/22.
 */
public class AspectLayout extends RelativeLayout {

    private static final String TAG = "AspectLayout";

    private int mWidthMeasureSpec;

    private int mRootHeight = 0;
    private int mRootWidth = 0;

    public AspectLayout(Context context) {
        super(context);
        initialize(context);
    }

    public AspectLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context);
    }

    public AspectLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context);
    }

    private void initialize(Context ctx) {
    }

    @TargetApi(21)
    public AspectLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.d(TAG, "onMeasure" + " width=[" + MeasureSpec.toString(widthMeasureSpec) +
                "] height=[" + View.MeasureSpec.toString(heightMeasureSpec) + "]");

        Rect r = new Rect();
        getWindowVisibleDisplayFrame(r);
        Pair<Integer, Integer> screenSize = Util.getResolution(getContext());

        if (mRootWidth == 0 && mRootHeight == 0) {
            mRootWidth = getRootView().getWidth();
            mRootHeight = getRootView().getHeight();
        }
        int totalHeight = 0;

        if (screenSize.first > screenSize.second) {
            // land
            totalHeight = mRootWidth > mRootHeight ? mRootHeight : mRootWidth;
        } else {
            // port
            totalHeight = mRootWidth < mRootHeight ? mRootHeight : mRootWidth;
        }

        int nowHeight = r.bottom - r.top;

        if (totalHeight - nowHeight > totalHeight / 4) {
            // soft keyboard show
            super.onMeasure(mWidthMeasureSpec, MeasureSpec.makeMeasureSpec(nowHeight + totalHeight - nowHeight, MeasureSpec.EXACTLY));
            return;
        } else {
            // soft keyboard hide
        }

        mWidthMeasureSpec = widthMeasureSpec;

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
