package com.qiniu.droid.niuplayer.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pili.pldroid.player.AVOptions;
import com.pili.pldroid.player.widget.PLVideoTextureView;
import com.qiniu.droid.niuplayer.R;
import com.qiniu.droid.niuplayer.utils.Config;

import static com.qiniu.droid.niuplayer.utils.Utils.createAVOptions;

public class PlayConfigView extends LinearLayout implements View.OnClickListener {
    public static final String BACKSTAGE_PLAY_TAG = "backstage_play";

    private PLVideoTextureView mVideoView;
    private boolean mIsMirror;
    private boolean mIsCache;
    private boolean mIsBackstagePlay;
    private int mRotation;
    private TextView mCurSpeedText;
    private TextView mCurDisplayText;
    private TextView mCurSpeedTipText;
    private TextView mSaveTip;

    public PlayConfigView(Context context) {
        super(context);
    }

    public PlayConfigView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View view = LayoutInflater.from(context).inflate(R.layout.view_play_config, this);
        ImageView imageView = view.findViewById(R.id.close_image);
        imageView.setOnClickListener(this);

        ViewGroup viewGroup = view.findViewById(R.id.mirror_group);
        viewGroup.setOnClickListener(this);

        viewGroup = view.findViewById(R.id.rotate_group);
        viewGroup.setOnClickListener(this);

        viewGroup = view.findViewById(R.id.save_group);
        viewGroup.setOnClickListener(this);

        viewGroup = view.findViewById(R.id.backstage_group);
        viewGroup.setOnClickListener(this);

        mCurSpeedTipText = view.findViewById(R.id.speed_current_text);

        TextView textView = view.findViewById(R.id.render_default);
        textView.setOnClickListener(this);

        mCurDisplayText = view.findViewById(R.id.render_full);
        mCurDisplayText.setOnClickListener(this);

        textView = view.findViewById(R.id.render_16_9);
        textView.setOnClickListener(this);

        textView = view.findViewById(R.id.render_4_3);
        textView.setOnClickListener(this);

        textView = view.findViewById(R.id.speed_05);
        textView.setOnClickListener(this);

        textView = view.findViewById(R.id.speed_075);
        textView.setOnClickListener(this);

        mCurSpeedText = view.findViewById(R.id.speed_1);
        mCurSpeedText.setOnClickListener(this);

        textView = view.findViewById(R.id.speed_125);
        textView.setOnClickListener(this);

        textView = view.findViewById(R.id.speed_15);
        textView.setOnClickListener(this);

        mSaveTip = view.findViewById(R.id.save_tip);
    }

    public void setVideoView(PLVideoTextureView videoView) {
        if (videoView.equals(mVideoView)) {
            return;
        }
        mVideoView = videoView;
        resetConfig();
    }

    private void resetConfig() {
        mIsBackstagePlay = false;
        mIsCache = false;
        mIsMirror = false;
        mRotation = 0;

        setPlayDisplayMode(PLVideoTextureView.ASPECT_RATIO_PAVED_PARENT, R.id.render_full);
        setPlaySpeed(1f, R.id.speed_1);
        setVisibility(GONE);
        mVideoView.setMirror(mIsMirror);
        mVideoView.setRotation(mRotation);
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        switch (viewId) {
            case R.id.mirror_group:
                mIsMirror = !mIsMirror;
                mVideoView.setMirror(mIsMirror);
                break;
            case R.id.rotate_group:
                mRotation += 90;
                mVideoView.setRotation(mRotation);
                break;
            case R.id.save_group:
                switchCacheEnable();
                break;
            case R.id.backstage_group:
                switchBackstagePlay();
                break;
            case R.id.render_default:
                setPlayDisplayMode(PLVideoTextureView.ASPECT_RATIO_ORIGIN, viewId);
                break;
            case R.id.render_full:
                setPlayDisplayMode(PLVideoTextureView.ASPECT_RATIO_PAVED_PARENT, viewId);
                break;
            case R.id.render_16_9:
                setPlayDisplayMode(PLVideoTextureView.ASPECT_RATIO_16_9, viewId);
                break;
            case R.id.render_4_3:
                setPlayDisplayMode(PLVideoTextureView.ASPECT_RATIO_4_3, viewId);
                break;
            case R.id.speed_05:
                setPlaySpeed(0.5f, viewId);
                break;
            case R.id.speed_075:
                setPlaySpeed(0.75f, viewId);
                break;
            case R.id.speed_1:
                setPlaySpeed(1f, viewId);
                break;
            case R.id.speed_125:
                setPlaySpeed(1.25f, viewId);
                break;
            case R.id.speed_15:
                setPlaySpeed(1.5f, viewId);
                break;
            case R.id.close_image:
                setVisibility(GONE);
                break;
        }
    }

    private void setPlayDisplayMode(int displayMode, int id) {
        if (mCurDisplayText != null) {
            mCurDisplayText.setTextColor(getResources().getColor(R.color.colorDefaultText));
        }
        mCurDisplayText = findViewById(id);
        mCurDisplayText.setTextColor(getResources().getColor(R.color.colorTextChoice));
        mVideoView.setDisplayAspectRatio(displayMode);
    }

    private void setPlaySpeed(float speed, int id) {
        if (mCurSpeedText != null) {
            mCurSpeedText.setTextColor(getResources().getColor(R.color.colorDefaultText));
        }
        mCurSpeedText = findViewById(id);
        mCurSpeedText.setTextColor(getResources().getColor(R.color.colorTextChoice));
        mVideoView.setPlaySpeed(speed);
        mCurSpeedTipText.setText(mCurSpeedText.getText());
    }

    private void switchCacheEnable() {
        mIsCache = !mIsCache;
        AVOptions options = createAVOptions();
        if (mIsCache) {
            options.setString(AVOptions.KEY_CACHE_DIR, Config.DEFAULT_CACHE_DIR_PATH);
            mSaveTip.setText("缓存已开");
        } else {
            mSaveTip.setText("本地缓存");
        }
        mVideoView.setAVOptions(options);
    }

    private void switchBackstagePlay() {
        mIsBackstagePlay = !mIsBackstagePlay;
        Object tag = mIsBackstagePlay ? BACKSTAGE_PLAY_TAG : null;
        String tip = mIsBackstagePlay ? "后台播放已经开启！" : "后台播放已经关闭！";
        mVideoView.setTag(tag);
        Toast.makeText(this.getContext(), tip, Toast.LENGTH_LONG).show();
    }
}
