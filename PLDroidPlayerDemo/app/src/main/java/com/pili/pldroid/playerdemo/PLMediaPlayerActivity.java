package com.pili.pldroid.playerdemo;

import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pili.pldroid.player.AVOptions;
import com.pili.pldroid.player.PLMediaPlayer;
import com.pili.pldroid.playerdemo.utils.Config;

import java.io.IOException;

/**
 * This demo shows how to use PLMediaPlayer API playing video stream
 */
public class PLMediaPlayerActivity extends VideoPlayerBaseActivity {

    private static final String TAG = PLMediaPlayerActivity.class.getSimpleName();

    private SurfaceView mSurfaceView;
    private PLMediaPlayer mMediaPlayer;
    private View mLoadingView;
    private AVOptions mAVOptions;

    private TextView mStatInfoTextView;

    private int mSurfaceWidth = 0;
    private int mSurfaceHeight = 0;

    private String mVideoPath = null;
    private boolean mIsStopped = false;
    private Toast mToast = null;

    private long mLastUpdateStatTime = 0;

    private boolean mDisableLog = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_player);

        mVideoPath = getIntent().getStringExtra("videoPath");
        boolean isLiveStreaming = getIntent().getIntExtra("liveStreaming", 1) == 1;

        Button pauseBtn = findViewById(R.id.BtnPause);
        Button resumeBtn = findViewById(R.id.BtnResume);

        mLoadingView = findViewById(R.id.LoadingView);
        mSurfaceView = findViewById(R.id.SurfaceView);
        mSurfaceView.getHolder().addCallback(mCallback);

        mStatInfoTextView = findViewById(R.id.StatInfoTextView);

        mSurfaceWidth = getResources().getDisplayMetrics().widthPixels;
        mSurfaceHeight = getResources().getDisplayMetrics().heightPixels;

        if (isLiveStreaming) {
            pauseBtn.setEnabled(false);
            resumeBtn.setEnabled(false);
        }

        mAVOptions = new AVOptions();
        mAVOptions.setInteger(AVOptions.KEY_PREPARE_TIMEOUT, 10 * 1000);
        // 1 -> hw codec enable, 0 -> disable [recommended]
        int codec = getIntent().getIntExtra("mediaCodec", AVOptions.MEDIA_CODEC_SW_DECODE);
        mAVOptions.setInteger(AVOptions.KEY_MEDIACODEC, codec);
        mAVOptions.setInteger(AVOptions.KEY_LIVE_STREAMING, isLiveStreaming ? 1 : 0);
        boolean cache = getIntent().getBooleanExtra("cache", false);
        if (!isLiveStreaming && cache) {
            mAVOptions.setString(AVOptions.KEY_CACHE_DIR, Config.DEFAULT_CACHE_DIR);
        }
        mDisableLog = getIntent().getBooleanExtra("disable-log", false);
        mAVOptions.setInteger(AVOptions.KEY_LOG_LEVEL, mDisableLog ? 5 : 0);

        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        audioManager.requestAudioFocus(null, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        release();
        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        audioManager.abandonAudioFocus(null);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    public void onClickPlay(View v) {
        if (mIsStopped) {
            prepare();
        } else {
            mMediaPlayer.start();
        }
    }

    public void onClickPause(View v) {
        if (mMediaPlayer != null) {
            mMediaPlayer.pause();
        }
    }

    public void onClickResume(View v) {
        if (mMediaPlayer != null) {
            mMediaPlayer.start();
        }
    }

    public void onClickStop(View v) {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.reset();
        }
        mIsStopped = true;
        mMediaPlayer = null;
    }

    public void releaseWithoutStop() {
        if (mMediaPlayer != null) {
            mMediaPlayer.setDisplay(null);
        }
    }

    public void release() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    private void prepare() {
        if (mMediaPlayer != null) {
            mMediaPlayer.setDisplay(mSurfaceView.getHolder());
            return;
        }

        try {
            mMediaPlayer = new PLMediaPlayer(this, mAVOptions);
            mMediaPlayer.setDebugLoggingEnabled(true);
            mMediaPlayer.setLooping(getIntent().getBooleanExtra("loop", false));
            mMediaPlayer.setOnPreparedListener(mOnPreparedListener);
            mMediaPlayer.setOnVideoSizeChangedListener(mOnVideoSizeChangedListener);
            mMediaPlayer.setOnCompletionListener(mOnCompletionListener);
            mMediaPlayer.setOnErrorListener(mOnErrorListener);
            mMediaPlayer.setOnInfoListener(mOnInfoListener);
            mMediaPlayer.setOnBufferingUpdateListener(mOnBufferingUpdateListener);
            mMediaPlayer.setDebugLoggingEnabled(!mDisableLog);
            // set replay if completed
            // mMediaPlayer.setLooping(true);
            mMediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
            mMediaPlayer.setDataSource(mVideoPath);
            mMediaPlayer.setDisplay(mSurfaceView.getHolder());
            mMediaPlayer.prepareAsync();
        } catch (UnsatisfiedLinkError e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private SurfaceHolder.Callback mCallback = new SurfaceHolder.Callback() {

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            prepare();
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            // release();
            releaseWithoutStop();
        }
    };

    private PLMediaPlayer.OnVideoSizeChangedListener mOnVideoSizeChangedListener = new PLMediaPlayer.OnVideoSizeChangedListener() {
        public void onVideoSizeChanged(PLMediaPlayer mp, int width, int height) {
            Log.i(TAG, "onVideoSizeChanged: width = " + width + ", height = " + height);
            // resize the display window to fit the screen
            if (width != 0 && height != 0) {
                float ratioW = (float) width / (float) mSurfaceWidth;
                float ratioH = (float) height / (float) mSurfaceHeight;
                float ratio = Math.max(ratioW, ratioH);
                width = (int) Math.ceil((float) width / ratio);
                height = (int) Math.ceil((float) height / ratio);
                FrameLayout.LayoutParams layout = new FrameLayout.LayoutParams(width, height);
                layout.gravity = Gravity.CENTER;
                mSurfaceView.setLayoutParams(layout);
            }
        }
    };

    private PLMediaPlayer.OnPreparedListener mOnPreparedListener = new PLMediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(PLMediaPlayer mp, int preparedTime) {
            Log.i(TAG, "On Prepared ! prepared time = " + preparedTime + " ms");
            mMediaPlayer.start();
            mIsStopped = false;
        }
    };

    private PLMediaPlayer.OnInfoListener mOnInfoListener = new PLMediaPlayer.OnInfoListener() {
        @Override
        public boolean onInfo(PLMediaPlayer mp, int what, int extra) {
            Log.i(TAG, "OnInfo, what = " + what + ", extra = " + extra);
            switch (what) {
                case PLMediaPlayer.MEDIA_INFO_BUFFERING_START:
                    mLoadingView.setVisibility(View.VISIBLE);
                    break;
                case PLMediaPlayer.MEDIA_INFO_BUFFERING_END:
                    mLoadingView.setVisibility(View.GONE);
                    break;
                case PLMediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START:
                    mLoadingView.setVisibility(View.GONE);
                    showToastTips("first video render time: " + extra + "ms");
                    break;
                case PLMediaPlayer.MEDIA_INFO_VIDEO_GOP_TIME:
                    Log.i(TAG, "Gop Time: " + extra);
                    break;
                case PLMediaPlayer.MEDIA_INFO_AUDIO_RENDERING_START:
                    mLoadingView.setVisibility(View.GONE);
                    break;
                case PLMediaPlayer.MEDIA_INFO_SWITCHING_SW_DECODE:
                    Log.i(TAG, "Hardware decoding failure, switching software decoding!");
                    break;
                case PLMediaPlayer.MEDIA_INFO_METADATA:
                    Log.i(TAG, mMediaPlayer.getMetadata().toString());
                    break;
                case PLMediaPlayer.MEDIA_INFO_VIDEO_BITRATE:
                case PLMediaPlayer.MEDIA_INFO_VIDEO_FPS:
                    updateStatInfo();
                    break;
                case PLMediaPlayer.MEDIA_INFO_CONNECTED:
                    Log.i(TAG, "Connected !");
                    break;
                case PLMediaPlayer.MEDIA_INFO_VIDEO_ROTATION_CHANGED:
                    Log.i(TAG, "Rotation changed: " + extra);
                default:
                    break;
            }
            return true;
        }
    };

    private PLMediaPlayer.OnBufferingUpdateListener mOnBufferingUpdateListener = new PLMediaPlayer.OnBufferingUpdateListener() {
        @Override
        public void onBufferingUpdate(PLMediaPlayer mp, int percent) {
            Log.d(TAG, "onBufferingUpdate: " + percent + "%");
            long current =  System.currentTimeMillis();
            if (current - mLastUpdateStatTime > 3000) {
                mLastUpdateStatTime = current;
                updateStatInfo();
            }
        }
    };

    /**
     * Listen the event of playing complete
     * For playing local file, it's called when reading the file EOF
     * For playing network stream, it's called when the buffered bytes played over
     * <p>
     * If setLooping(true) is called, the player will restart automatically
     * And ｀onCompletion｀ will not be called
     */
    private PLMediaPlayer.OnCompletionListener mOnCompletionListener = new PLMediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(PLMediaPlayer mp) {
            Log.d(TAG, "Play Completed !");
            showToastTips("Play Completed !");
            finish();
        }
    };

    private PLMediaPlayer.OnErrorListener mOnErrorListener = new PLMediaPlayer.OnErrorListener() {
        @Override
        public boolean onError(PLMediaPlayer mp, int errorCode) {
            Log.e(TAG, "Error happened, errorCode = " + errorCode);
            switch (errorCode) {
                case PLMediaPlayer.ERROR_CODE_IO_ERROR:
                    /**
                     * SDK will do reconnecting automatically
                     */
                    showToastTips("IO Error !");
                    return false;
                case PLMediaPlayer.ERROR_CODE_OPEN_FAILED:
                    showToastTips("failed to open player !");
                    break;
                case PLMediaPlayer.ERROR_CODE_SEEK_FAILED:
                    showToastTips("failed to seek !");
                    break;
                default:
                    showToastTips("unknown error !");
                    break;
            }
            finish();
            return true;
        }
    };

    private void showToastTips(final String tips) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mToast != null) {
                    mToast.cancel();
                }
                mToast = Toast.makeText(PLMediaPlayerActivity.this, tips, Toast.LENGTH_SHORT);
                mToast.show();
            }
        });
    }

    private void updateStatInfo() {
        long bitrate = mMediaPlayer.getVideoBitrate() / 1024;
        final String stat = bitrate + "kbps, " + mMediaPlayer.getVideoFps() + "fps";
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mStatInfoTextView.setText(stat);
            }
        });
    }
}
