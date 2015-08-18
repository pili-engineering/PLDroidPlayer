package com.pili.pldroid.playerdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.pili.pldroid.player.AudioPlayer;
import com.pili.pldroid.player.PlayerCode;
import com.pili.pldroid.playerdemo.R;
import com.pili.pldroid.playerdemo.common.Util;
import com.pili.pldroid.playerdemo.widget.MediaController;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

public class AudioPlayerActivity extends Activity implements
        IjkMediaPlayer.OnCompletionListener,
        IjkMediaPlayer.OnInfoListener,
        IjkMediaPlayer.OnErrorListener,
        IjkMediaPlayer.OnPreparedListener {
    private static final String TAG = "AudioPlayerActivity";
    private static final int REQ_DELAY_MILLS = 3000;

    private View mBufferingIndicator;
    private MediaController mMediaController;
    private AudioPlayer mAudioPlayer;

    private String mAudioPath;
    private Button mBackBtn;
    private long mLastPosition = 0;
    private boolean mIsLiveStream = false;

    private int mReqDelayMills = REQ_DELAY_MILLS;
    private boolean mIsCompleted = false;
    private Runnable mVideoReconnect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_player);

        mAudioPath = getIntent().getStringExtra("audioPath");

        Intent intent = getIntent();
        String intentAction = intent.getAction();
        if (!TextUtils.isEmpty(intentAction) && intentAction.equals(Intent.ACTION_VIEW)) {
            mAudioPath = intent.getDataString();
        }

        mBackBtn = (Button) findViewById(R.id.back_btn);
        mBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });
        mBufferingIndicator = findViewById(R.id.buffering_indicator);

        boolean useFastForward = true;
        boolean disableProgressBar = false;
        // Tip: you can custom the variable depending on your situation
        mIsLiveStream = !Util.isUrlLocalFile(mAudioPath);
        if (mIsLiveStream) {
            disableProgressBar = true;
            useFastForward = false;
        }
        mMediaController = new MediaController(this, useFastForward, disableProgressBar);
        mAudioPlayer = new AudioPlayer(this);

        mMediaController.setMediaPlayer(mAudioPlayer);
        mAudioPlayer.setMediaController(mMediaController);
        mAudioPlayer.setOnErrorListener(this);
        mAudioPlayer.setOnCompletionListener(this);
        mAudioPlayer.setOnInfoListener(this);
        mAudioPlayer.setOnPreparedListener(this);
        mAudioPlayer.setAudioPath(mAudioPath);

        mAudioPlayer.start();
        mBufferingIndicator.setVisibility(View.VISIBLE);
    }

    @Override
    public void onCompletion(IMediaPlayer mp) {
        Log.d(TAG, "onCompletion");
        mIsCompleted = true;
        mBufferingIndicator.setVisibility(View.GONE);
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
                mVideoReconnect = new Runnable() {
                    @Override
                    public void run() {
                        mAudioPlayer.setAudioPath(mAudioPath);
                    }
                };
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
        Log.i(TAG, "onResume");
        if (mAudioPlayer != null && !mIsLiveStream && mLastPosition != 0) {
            mAudioPlayer.seekTo(mLastPosition);
            mAudioPlayer.start();
        }
    }

    @Override
    public void onPause() {
//        if (mAudioPlayer != null) {
//            mAudioPlayer.pause();
//            mLastPosition = mAudioPlayer.getCurrentPosition();
//        }
        if (mAudioPlayer != null) {
            mLastPosition = mAudioPlayer.getCurrentPosition();
            mAudioPlayer.stopPlayback();
        }
        super.onPause();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(mAudioPlayer.onKeyDown(keyCode, event)) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
