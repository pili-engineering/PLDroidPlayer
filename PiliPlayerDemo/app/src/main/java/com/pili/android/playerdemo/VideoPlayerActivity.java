package com.pili.android.playerdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.pili.android.piliplayer.widget.VideoView;
import com.pili.android.playerdemo.common.Util;
import com.pili.android.playerdemo.widget.MediaController;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

public class VideoPlayerActivity extends Activity implements
        IjkMediaPlayer.OnCompletionListener,
        IjkMediaPlayer.OnInfoListener,
        IjkMediaPlayer.OnErrorListener,
        IjkMediaPlayer.OnPreparedListener {
    private static final String TAG = "VideoPlayerActivity";
    private VideoView mVideoView;
    private View mBufferingIndicator;
    private MediaController mMediaController;

    private String mVideoPath;
    private Button mBackBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        mVideoPath = getIntent().getStringExtra("videoPath");

        Intent intent = getIntent();
        String intentAction = intent.getAction();
        if (!TextUtils.isEmpty(intentAction) && intentAction.equals(Intent.ACTION_VIEW)) {
            mVideoPath = intent.getDataString();
        }

        mBackBtn = (Button) findViewById(R.id.back_btn);
        mBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mVideoView.stopPlayback();
                onBackPressed();
                finish();
            }
        });
        mBufferingIndicator = findViewById(R.id.buffering_indicator);
        boolean useFastForward = true;
        boolean disableProgressBar = false;

        // Tip: you can custom the variable depending on your situation
        if (!Util.isUrlLocalFile(mVideoPath)) {
            disableProgressBar = true;
            useFastForward = false;
        }
        mMediaController = new MediaController(this, useFastForward, disableProgressBar);

        mVideoView = (VideoView) findViewById(R.id.video_view);
        mMediaController.setMediaPlayer(mVideoView);
        mVideoView.setMediaController(mMediaController);
//        mVideoView.setMediaBufferingIndicator(mBufferingIndicator);
        mVideoView.setVideoPath(mVideoPath);

        mVideoView.setOnErrorListener(this);
        mVideoView.setOnCompletionListener(this);
        mVideoView.setOnInfoListener(this);
        mVideoView.setOnPreparedListener(this);

        mVideoView.requestFocus();
        mVideoView.start();
        mBufferingIndicator.setVisibility(View.VISIBLE);
    }

    @Override
    public void onCompletion(IMediaPlayer iMediaPlayer) {
        Log.d(TAG, "onCompletion");
        mBufferingIndicator.setVisibility(View.GONE);
    }

    @Override
    public boolean onError(IMediaPlayer iMediaPlayer, int framework_err, int impl_err) {
        Log.d(TAG, "onInfo framework_err=" + framework_err + ", impl_err=" + impl_err);
        return false;
    }

    @Override
    public boolean onInfo(IMediaPlayer iMediaPlayer, int what, int extra) {
        Log.d(TAG, "onInfo what=" + what + ", extra=" + extra);
        if (what == IMediaPlayer.MEDIA_INFO_BUFFERING_START) {
            Log.i(TAG, "onInfo: (MEDIA_INFO_BUFFERING_START)");
            if (mBufferingIndicator != null)
                mBufferingIndicator.setVisibility(View.VISIBLE);
        } else if (what == IMediaPlayer.MEDIA_INFO_BUFFERING_END) {
            Log.i(TAG, "onInfo: (MEDIA_INFO_BUFFERING_END)");
            if (mBufferingIndicator != null)
                mBufferingIndicator.setVisibility(View.GONE);
        }
        return false;
    }

    @Override
    public void onPrepared(IMediaPlayer iMediaPlayer) {
        Log.d(TAG, "onPrepared");
        mBufferingIndicator.setVisibility(View.GONE);
    }
}
