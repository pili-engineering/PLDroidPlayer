package com.pili.pldroid.playerdemo;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.pili.pldroid.player.AVOptions;
import com.pili.pldroid.player.PLOnBufferingUpdateListener;
import com.pili.pldroid.player.PLOnCompletionListener;
import com.pili.pldroid.player.PLOnErrorListener;
import com.pili.pldroid.player.PLOnInfoListener;
import com.pili.pldroid.player.PLOnVideoSizeChangedListener;
import com.pili.pldroid.player.widget.PLVideoTextureView;
import com.pili.pldroid.playerdemo.utils.Config;
import com.pili.pldroid.playerdemo.utils.Utils;
import com.pili.pldroid.playerdemo.widget.MediaController;

/**
 * This is a demo activity of PLVideoTextureView
 */
public class PLVideoTextureActivity extends VideoPlayerBaseActivity {

    private static final String TAG = PLVideoTextureActivity.class.getSimpleName();

    private PLVideoTextureView mVideoView;
    private Toast mToast = null;
    private int mRotation = 0;
    private int mDisplayAspectRatio = PLVideoTextureView.ASPECT_RATIO_FIT_PARENT; //default
    private TextView mStatInfoTextView;
    private boolean mIsLiveStreaming;

    String videoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pl_video_texture);

        videoPath = getIntent().getStringExtra("videoPath");
        mIsLiveStreaming = getIntent().getIntExtra("liveStreaming", 1) == 1;

        mVideoView = findViewById(R.id.VideoView);

        View loadingView = findViewById(R.id.LoadingView);
        mVideoView.setBufferingIndicator(loadingView);

        View coverView = findViewById(R.id.CoverView);
        mVideoView.setCoverView(coverView);

        mStatInfoTextView = findViewById(R.id.StatInfoTextView);

        // If you want to fix display orientation such as landscape, you can use the code show as follow
        //
        // if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
        //     mVideoView.setPreviewOrientation(0);
        // }
        // else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
        //     mVideoView.setPreviewOrientation(270);
        // }

        // 1 -> hw codec enable, 0 -> disable [recommended]
        int codec = getIntent().getIntExtra("mediaCodec", AVOptions.MEDIA_CODEC_SW_DECODE);
        AVOptions options = new AVOptions();
        // the unit of timeout is ms
        options.setInteger(AVOptions.KEY_PREPARE_TIMEOUT, 10 * 1000);
        options.setInteger(AVOptions.KEY_LIVE_STREAMING, mIsLiveStreaming ? 1 : 0);
        // 1 -> hw codec enable, 0 -> disable [recommended]
        options.setInteger(AVOptions.KEY_MEDIACODEC, codec);
        boolean disableLog = getIntent().getBooleanExtra("disable-log", false);
        options.setInteger(AVOptions.KEY_LOG_LEVEL, disableLog ? 5 : 0);
        boolean cache = getIntent().getBooleanExtra("cache", false);
        if (!mIsLiveStreaming && cache) {
            options.setString(AVOptions.KEY_CACHE_DIR, Config.DEFAULT_CACHE_DIR);
        }
        if (!mIsLiveStreaming) {
            int startPos = getIntent().getIntExtra("start-pos", 0);
            options.setInteger(AVOptions.KEY_START_POSITION, startPos * 1000);
        }
        mVideoView.setAVOptions(options);

        // You can mirror the display
        // mVideoView.setMirror(true);

        // You can also use a custom `MediaController` widget
        MediaController mediaController = new MediaController(this, !mIsLiveStreaming, mIsLiveStreaming);
        mediaController.setOnClickSpeedAdjustListener(mOnClickSpeedAdjustListener);
        mVideoView.setMediaController(mediaController);

        mVideoView.setOnInfoListener(mOnInfoListener);
        mVideoView.setOnVideoSizeChangedListener(mOnVideoSizeChangedListener);
        mVideoView.setOnBufferingUpdateListener(mOnBufferingUpdateListener);
        mVideoView.setOnCompletionListener(mOnCompletionListener);
        mVideoView.setOnErrorListener(mOnErrorListener);
        //mVideoView.setOnPreparedListener(mOnPreparedListener);
        //mVideoView.setOnSeekCompleteListener(mOnSeekCompleteListener);

        mVideoView.setLooping(getIntent().getBooleanExtra("loop", false));

        mVideoView.setVideoPath(videoPath);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mVideoView.pause();
        mToast = null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mVideoView.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mVideoView.stopPlayback();
    }

    public void onClickRotate(View v) {
        mRotation = (mRotation + 90) % 360;
        mVideoView.setDisplayOrientation(mRotation);
    }


    public void onClickSwitchScreen(View v) {
        mDisplayAspectRatio = (mDisplayAspectRatio + 1) % 5;
        mVideoView.setDisplayAspectRatio(mDisplayAspectRatio);
        switch (mVideoView.getDisplayAspectRatio()) {
            case PLVideoTextureView.ASPECT_RATIO_ORIGIN:
                Utils.showToastTips(PLVideoTextureActivity.this, "Origin mode");
                break;
            case PLVideoTextureView.ASPECT_RATIO_FIT_PARENT:
                Utils.showToastTips(PLVideoTextureActivity.this, "Fit parent !");
                break;
            case PLVideoTextureView.ASPECT_RATIO_PAVED_PARENT:
                Utils.showToastTips(PLVideoTextureActivity.this, "Paved parent !");
                break;
            case PLVideoTextureView.ASPECT_RATIO_16_9:
                Utils.showToastTips(PLVideoTextureActivity.this, "16 : 9 !");
                break;
            case PLVideoTextureView.ASPECT_RATIO_4_3:
                Utils.showToastTips(PLVideoTextureActivity.this, "4 : 3 !");
                break;
            default:
                break;
        }
    }

    private PLOnInfoListener mOnInfoListener = new PLOnInfoListener() {
        @Override
        public void onInfo(int what, int extra) {
            Log.i(TAG, "OnInfo, what = " + what + ", extra = " + extra);
            switch (what) {
                case PLOnInfoListener.MEDIA_INFO_BUFFERING_START:
                    break;
                case PLOnInfoListener.MEDIA_INFO_BUFFERING_END:
                    break;
                case PLOnInfoListener.MEDIA_INFO_VIDEO_RENDERING_START:
                    Utils.showToastTips(PLVideoTextureActivity.this, "First video render time: " + extra + "ms");
                    break;
                case PLOnInfoListener.MEDIA_INFO_AUDIO_RENDERING_START:
                    Log.i(TAG, "First audio render time: " + extra + "ms");
                    break;
                case PLOnInfoListener.MEDIA_INFO_VIDEO_FRAME_RENDERING:
                    Log.i(TAG, "video frame rendering, ts = " + extra);
                    break;
                case PLOnInfoListener.MEDIA_INFO_AUDIO_FRAME_RENDERING:
                    Log.i(TAG, "audio frame rendering, ts = " + extra);
                    break;
                case PLOnInfoListener.MEDIA_INFO_VIDEO_GOP_TIME:
                    Log.i(TAG, "Gop Time: " + extra);
                    break;
                case PLOnInfoListener.MEDIA_INFO_SWITCHING_SW_DECODE:
                    Log.i(TAG, "Hardware decoding failure, switching software decoding!");
                    break;
                case PLOnInfoListener.MEDIA_INFO_METADATA:
                    Log.i(TAG, mVideoView.getMetadata().toString());
                    break;
                case PLOnInfoListener.MEDIA_INFO_VIDEO_BITRATE:
                case PLOnInfoListener.MEDIA_INFO_VIDEO_FPS:
                    updateStatInfo();
                    break;
                case PLOnInfoListener.MEDIA_INFO_CONNECTED:
                    Log.i(TAG, "Connected !");
                    break;
                case PLOnInfoListener.MEDIA_INFO_VIDEO_ROTATION_CHANGED:
                    Log.i(TAG, "Rotation changed: " + extra);
                    break;
                default:
                    break;
            }
        }
    };

    private PLOnErrorListener mOnErrorListener = new PLOnErrorListener() {
        @Override
        public boolean onError(int errorCode) {
            Log.e(TAG, "Error happened, errorCode = " + errorCode);
            switch (errorCode) {
                case PLOnErrorListener.ERROR_CODE_IO_ERROR:
                    /**
                     * SDK will do reconnecting automatically
                     */
                    Utils.showToastTips(PLVideoTextureActivity.this, "IO Error !");
                    return false;
                case PLOnErrorListener.ERROR_CODE_OPEN_FAILED:
                    Utils.showToastTips(PLVideoTextureActivity.this, "failed to open player !");
                    break;
                case PLOnErrorListener.ERROR_CODE_SEEK_FAILED:
                    Utils.showToastTips(PLVideoTextureActivity.this, "failed to seek !");
                    return true;
                default:
                    Utils.showToastTips(PLVideoTextureActivity.this, "unknown error !");
                    break;
            }
            finish();
            return true;
        }
    };

    private PLOnCompletionListener mOnCompletionListener = new PLOnCompletionListener() {
        @Override
        public void onCompletion() {
            Log.i(TAG, "Play Completed !");
            Utils.showToastTips(PLVideoTextureActivity.this, "Play Completed !");
            finish();
        }
    };

    private PLOnBufferingUpdateListener mOnBufferingUpdateListener = new PLOnBufferingUpdateListener() {
        @Override
        public void onBufferingUpdate(int precent) {
            Log.i(TAG, "onBufferingUpdate: " + precent);
        }
    };

    private PLOnVideoSizeChangedListener mOnVideoSizeChangedListener = new PLOnVideoSizeChangedListener() {
        @Override
        public void onVideoSizeChanged(int width, int height) {
            Log.i(TAG, "onVideoSizeChanged: width = " + width + ", height = " + height);
        }
    };

    private MediaController.OnClickSpeedAdjustListener mOnClickSpeedAdjustListener = new MediaController.OnClickSpeedAdjustListener() {
        @Override
        public void onClickNormal() {
            // 0x0001/0x0001 = 2
            mVideoView.setPlaySpeed(0X00010001);
        }

        @Override
        public void onClickFaster() {
            // 0x0002/0x0001 = 2
            mVideoView.setPlaySpeed(0X00020001);
        }

        @Override
        public void onClickSlower() {
            // 0x0001/0x0002 = 0.5
            mVideoView.setPlaySpeed(0X00010002);
        }
    };

    private void updateStatInfo() {
        long bitrate = mVideoView.getVideoBitrate() / 1024;
        final String stat = bitrate + "kbps, " + mVideoView.getVideoFps() + "fps";
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mStatInfoTextView.setText(stat);
            }
        });
    }
}
