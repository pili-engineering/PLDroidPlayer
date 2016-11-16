package com.pili.pldroid.playerdemo;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.pili.pldroid.player.AVOptions;
import com.pili.pldroid.player.PlayerCode;
import com.pili.pldroid.player.common.Util;
import com.pili.pldroid.player.widget.PLVideoView;
import com.pili.pldroid.player.widget.VideoView;
import com.pili.pldroid.playerdemo.widget.AspectLayout;
import com.pili.pldroid.playerdemo.widget.MediaController;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/**
 *  This is a demo activity of com.pili.pldroid.player.widget.VideoView
 *  @deprecated Use {@link PLVideoView} instead.
 */
public class VideoViewActivity extends VideoPlayerBaseActivity implements
        IjkMediaPlayer.OnCompletionListener,
        IjkMediaPlayer.OnInfoListener,
        IjkMediaPlayer.OnErrorListener,
        IjkMediaPlayer.OnVideoSizeChangedListener,
        IjkMediaPlayer.OnPreparedListener {

    private static final String TAG = "VideoViewActivity";

    private static final int REQ_DELAY_MILLS = 3000;

    private VideoView mVideoView;
    private View mBufferingIndicator;
    private MediaController mMediaController;
    private AspectLayout mAspectLayout;
    private ViewGroup.LayoutParams mLayoutParams;
    private Pair<Integer, Integer> mScreenSize;

    private String mVideoPath;
    private Button mBackBtn;
    private long mLastPosition = 0;
    private boolean mIsLiveStream = false;

    private int mReqDelayMills = REQ_DELAY_MILLS;
    private boolean mIsCompleted = false;
    private Runnable mVideoReconnect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_view);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);

        mVideoPath = getIntent().getStringExtra("videoPath");

        Intent intent = getIntent();
        String intentAction = intent.getAction();
        if (!TextUtils.isEmpty(intentAction) && intentAction.equals(Intent.ACTION_VIEW)) {
            mVideoPath = intent.getDataString();
        }

        mVideoView = (VideoView) findViewById(R.id.video_view);
        mAspectLayout = (AspectLayout)findViewById(R.id.aspect_layout);
        mBufferingIndicator = findViewById(R.id.buffering_indicator);

        mBackBtn = (Button) findViewById(R.id.back_btn);
        mBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mVideoView.stopPlayback();
                onBackPressed();
                finish();
            }
        });

        boolean useFastForward = true;
        boolean disableProgressBar = false;

        Log.i(TAG, "mVideoPath:" + mVideoPath);

        //SharedLibraryNameHelper.getInstance().renameSharedLibrary("pldroidplayer_v7a");

        AVOptions options = new AVOptions();
        int codec = getIntent().getIntExtra("mediaCodec", 0);
        options.setInteger(AVOptions.KEY_MEDIACODEC, codec); // 1 -> hw codec enable, 0 -> disable

        mIsLiveStream = isLiveStreaming(mVideoPath);
        if (mIsLiveStream) {
            options.setInteger(AVOptions.KEY_GET_AV_FRAME_TIMEOUT, 10 * 1000); // the unit of timeout is ms
            options.setInteger(AVOptions.KEY_LIVE_STREAMING, 1);
        }
        // Tip: you can custom the variable depending on your situation
        if (mIsLiveStream) {
            disableProgressBar = true;
            useFastForward = false;
        }
        mMediaController = new MediaController(this, useFastForward, disableProgressBar);
        mMediaController.setMediaPlayer(mVideoView);
        mVideoView.setMediaController(mMediaController);
        mVideoView.setMediaBufferingIndicator(mBufferingIndicator);

        mVideoView.setAVOptions(options);

        mVideoView.setVideoPath(mVideoPath);

        mVideoView.setOnErrorListener(this);
        mVideoView.setOnCompletionListener(this);
        mVideoView.setOnInfoListener(this);
        mVideoView.setOnPreparedListener(this);
        mVideoView.setOnVideoSizeChangedListener(this);

        mVideoView.requestFocus();

        mBufferingIndicator.setVisibility(View.VISIBLE);
    }

    @Override
    public void onCompletion(IMediaPlayer mp) {
        Log.d(TAG, "onCompletion");
        mIsCompleted = true;
        mBufferingIndicator.setVisibility(View.GONE);
        mVideoView.start();
    }

    @Override
    public boolean onError(IMediaPlayer mp, int what, int extra) {
        Log.d(TAG, "onError what=" + what + ", extra=" + extra);
        if (what == -10000) {
            if (extra == PlayerCode.EXTRA_CODE_INVALID_URI || extra == PlayerCode.EXTRA_CODE_EOF) {
                if (mBufferingIndicator != null)
                    mBufferingIndicator.setVisibility(View.GONE);
                return true;
            }
            if (mIsCompleted && extra == PlayerCode.EXTRA_CODE_EMPTY_PLAYLIST) {
                Log.d(TAG, "mVideoView reconnect!!!");
                mVideoView.removeCallbacks(mVideoReconnect);
                mVideoReconnect = new Runnable() {
                    @Override
                    public void run() {
                        mVideoView.setVideoPath(mVideoPath);
                    }
                };
                mVideoView.postDelayed(mVideoReconnect, mReqDelayMills);
                mReqDelayMills += 200;
            } else if (extra == PlayerCode.EXTRA_CODE_404_NOT_FOUND) {
                // NO ts exist
                if (mBufferingIndicator != null)
                    mBufferingIndicator.setVisibility(View.GONE);
            } else if (extra == PlayerCode.EXTRA_CODE_IO_ERROR) {
                // NO rtmp stream exist
                if (mBufferingIndicator != null)
                    mBufferingIndicator.setVisibility(View.GONE);
            }
        }
        // return true means you handle the onError, hence System wouldn't handle it again(popup a dialog).
        return true;
    }

    @Override
    public boolean onInfo(IMediaPlayer mp, int what, int extra) {

        Log.d(TAG, "onInfo what=" + what + ", extra=" + extra);

        if (what == IMediaPlayer.MEDIA_INFO_BUFFERING_START) {
            Log.i(TAG, "onInfo: (MEDIA_INFO_BUFFERING_START)");
            if (mBufferingIndicator != null)
                mBufferingIndicator.setVisibility(View.VISIBLE);
        } else if (what == IMediaPlayer.MEDIA_INFO_BUFFERING_END) {
            Log.i(TAG, "onInfo: (MEDIA_INFO_BUFFERING_END)");
            if (mBufferingIndicator != null)
                mBufferingIndicator.setVisibility(View.GONE);
        } else if (what == IMediaPlayer.MEDIA_INFO_AUDIO_RENDERING_START) {
            Toast.makeText(this, "Audio Start", Toast.LENGTH_LONG).show();
            Log.i(TAG, "duration:" + mVideoView.getDuration());
        } else if (what == IMediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START) {
            Toast.makeText(this, "Video Start", Toast.LENGTH_LONG).show();
            Log.i(TAG, "duration:" + mVideoView.getDuration());
        }

        return true;
    }

    @Override
    public void onPrepared(IMediaPlayer mp) {
        Log.d(TAG, "onPrepared");
        mBufferingIndicator.setVisibility(View.GONE);
        mReqDelayMills = REQ_DELAY_MILLS;
    }

    @Override
    public void onResume() {
        super.onResume();
        mReqDelayMills = REQ_DELAY_MILLS;
        if (mVideoView != null && !mIsLiveStream && mLastPosition != 0) {
            mVideoView.seekTo(mLastPosition);
            mVideoView.start();
        }
    }

    @Override
    public void onPause() {
        if (mVideoView != null) {
            mLastPosition = mVideoView.getCurrentPosition();
            mVideoView.pause();
        }
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        super.onPause();
    }

    @Override
    public void onVideoSizeChanged(IMediaPlayer iMediaPlayer, int width, int height, int sarNum, int sarDen) {

        Log.i(TAG, "onVideoSizeChanged " + iMediaPlayer.getVideoWidth() + "x" + iMediaPlayer.getVideoHeight() + ",width:" + width + ",height:" + height + ",sarDen:" + sarDen + ",sarNum:" + sarNum);

        /*
        if (width > height) {
            // land video
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        } else {
            // port video
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        }*/

        mScreenSize = Util.getResolution(this);

        if (width < mScreenSize.first) {
            height = mScreenSize.first * height / width;
            width = mScreenSize.first;
        }

        if (width * height < mScreenSize.first * mScreenSize.second) {
            width = mScreenSize.second * width / height;
            height = mScreenSize.second;
        }

        mLayoutParams = mAspectLayout.getLayoutParams();
        mLayoutParams.width = width;
        mLayoutParams.height = height;
        mAspectLayout.setLayoutParams(mLayoutParams);
    }

    private boolean isLiveStreaming(String url) {
        if (url.startsWith("rtmp://")
                || (url.startsWith("http://") && url.endsWith(".m3u8"))
                || (url.startsWith("http://") && url.endsWith(".flv"))) {
            return true;
        }
        return false;
    }
}
