package com.pili.pldroid.playerdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.pili.pldroid.player.AVOptions;
import com.pili.pldroid.player.PLMediaPlayer;
import com.pili.pldroid.player.widget.PLVideoTextureView;
import com.pili.pldroid.playerdemo.widget.MediaController;

/**
 *  This is a demo activity of PLVideoTextureView
 */
public class PLVideoTextureActivity extends AppCompatActivity {

    private MediaController mMediaController;
    private PLVideoTextureView mVideoView;
    private Toast mToast = null;
    private String mVideoPath = null;
    private int mRotation = 0;
    private int mDisplayAspectRatio = PLVideoTextureView.ASPECT_RATIO_FIT_PARENT; //default

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_pl_video_texture);
        mVideoView = (PLVideoTextureView) findViewById(R.id.VideoView);

        View loadingView = findViewById(R.id.LoadingView);
        mVideoView.setBufferingIndicator(loadingView);

        mVideoPath = getIntent().getStringExtra("videoPath");

        // If you want to fix display orientation such as landscape, you can use the code show as follow
        //
        // if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
        //     mVideoView.setPreviewOrientation(0);
        // }
        // else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
        //     mVideoView.setPreviewOrientation(270);
        // }

        mVideoPath = getIntent().getStringExtra("videoPath");

        AVOptions options = new AVOptions();

        if (isLiveStreaming(mVideoPath)) {
            // the unit of timeout is ms
            options.setInteger(AVOptions.KEY_GET_AV_FRAME_TIMEOUT, 10 * 1000);
            // Some optimization with buffering mechanism when be set to 1
            options.setInteger(AVOptions.KEY_LIVE_STREAMING, 1);
        }

        // 1 -> hw codec enable, 0 -> disable [recommended]
        int codec = getIntent().getIntExtra("mediaCodec", 0);
        options.setInteger(AVOptions.KEY_MEDIACODEC, codec);

        // whether start play automatically after prepared, default value is 1
        options.setInteger(AVOptions.KEY_START_ON_PREPARED, 0);

        mVideoView.setAVOptions(options);

        // You can mirror the display
        // mVideoView.setMirror(true);

        // You can also use a custom `MediaController` widget
        mMediaController = new MediaController(this, false, isLiveStreaming(mVideoPath));
        mVideoView.setMediaController(mMediaController);

        mVideoView.setOnCompletionListener(mOnCompletionListener);
        mVideoView.setOnErrorListener(mOnErrorListener);

        mVideoView.setVideoPath(mVideoPath);
        mVideoView.start();
    }

    @Override
    protected void onPause() {
        mVideoView.pause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mVideoView.start();
    }

    @Override
    protected void onDestroy() {
        mVideoView.stopPlayback();
        super.onDestroy();
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
                showToastTips("Origin mode");
                break;
            case PLVideoTextureView.ASPECT_RATIO_FIT_PARENT:
                showToastTips("Fit parent !");
                break;
            case PLVideoTextureView.ASPECT_RATIO_PAVED_PARENT:
                showToastTips("Paved parent !");
                break;
            case PLVideoTextureView.ASPECT_RATIO_16_9:
                showToastTips("16 : 9 !");
                break;
            case PLVideoTextureView.ASPECT_RATIO_4_3:
                showToastTips("4 : 3 !");
                break;
            default:
                break;
        }
    }

    private PLMediaPlayer.OnErrorListener mOnErrorListener = new PLMediaPlayer.OnErrorListener() {
        @Override
        public boolean onError(PLMediaPlayer mp, int errorCode) {
            switch (errorCode) {
                case PLMediaPlayer.ERROR_CODE_INVALID_URI:
                    showToastTips("Invalid URL !");
                    break;
                case PLMediaPlayer.ERROR_CODE_404_NOT_FOUND:
                    showToastTips("404 resource not found !");
                    break;
                case PLMediaPlayer.ERROR_CODE_CONNECTION_REFUSED:
                    showToastTips("Connection refused !");
                    break;
                case PLMediaPlayer.ERROR_CODE_CONNECTION_TIMEOUT:
                    showToastTips("Connection timeout !");
                    break;
                case PLMediaPlayer.ERROR_CODE_EMPTY_PLAYLIST:
                    showToastTips("Empty playlist !");
                    break;
                case PLMediaPlayer.ERROR_CODE_STREAM_DISCONNECTED:
                    showToastTips("Stream disconnected !");
                    break;
                case PLMediaPlayer.ERROR_CODE_IO_ERROR:
                    showToastTips("Network IO Error !");
                    break;
                case PLMediaPlayer.MEDIA_ERROR_UNKNOWN:
                default:
                    showToastTips("unknown error !");
                    break;
            }
            // Todo pls handle the error status here, retry or call finish()
            finish();
            // If you want to retry, do like this:
            // mVideoView.setVideoPath(mVideoPath);
            // Return true means the error has been handled
            // If return false, then `onCompletion` will be called
            return true;
        }
    };

    private PLMediaPlayer.OnCompletionListener mOnCompletionListener = new PLMediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(PLMediaPlayer plMediaPlayer) {
            showToastTips("Play Completed !");
            finish();
        }
    };

    private void showToastTips(final String tips) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mToast != null) {
                    mToast.cancel();
                }
                mToast = Toast.makeText(PLVideoTextureActivity.this, tips, Toast.LENGTH_SHORT);
                mToast.show();
            }
        });
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
