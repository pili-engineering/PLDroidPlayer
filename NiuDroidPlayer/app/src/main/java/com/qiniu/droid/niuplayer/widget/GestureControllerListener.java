package com.qiniu.droid.niuplayer.widget;


import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.WindowManager;

public class GestureControllerListener implements GestureDetector.OnGestureListener {
    private float mCurVolume;
    private Activity mActivity;

    public GestureControllerListener(Activity activity) {
        mActivity = activity;
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return true;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {
    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return true;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        float touchX = e1.getX();
        WindowManager wm = (WindowManager) mActivity
                .getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();

        final double FLING_MIN_DISTANCE = 0.5;
        final double FLING_MIN_VELOCITY = 0.5;
        if (e1.getY() - e2.getY() > FLING_MIN_DISTANCE
                && Math.abs(distanceY) > FLING_MIN_VELOCITY) {
            if (touchX > width / 2) {
                setVolume(0.1f);
            } else {
                setBrightness(10);
            }
        }
        if (e1.getY() - e2.getY() < FLING_MIN_DISTANCE
                && Math.abs(distanceY) > FLING_MIN_VELOCITY) {
            if (touchX > width / 2) {
                setVolume(-0.1f);
            } else {
                setBrightness(-10);
            }
        }
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {
    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    public void setVolume(float volume) {
        AudioManager audioManager = (AudioManager) mActivity.getSystemService(Context.AUDIO_SERVICE);
        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC); // 获取系统最大音量

        if (mCurVolume == 0) {
            mCurVolume = (float) (audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
        }
        mCurVolume += volume;
        if (mCurVolume > maxVolume) {
            mCurVolume = maxVolume;
        } else if (mCurVolume < 0) {
            mCurVolume = 0;
        }
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, (int) mCurVolume, 0);
    }

    public void setBrightness(float brightness) {
        WindowManager.LayoutParams lp = mActivity.getWindow().getAttributes();
        lp.screenBrightness = lp.screenBrightness + brightness / 255.0f;
        if (lp.screenBrightness > 1) {
            lp.screenBrightness = 1;
        } else if (lp.screenBrightness < 0.01) {
            lp.screenBrightness = (float) 0.01;
        }
        mActivity.getWindow().setAttributes(lp);
    }
}

