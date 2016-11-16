package com.pili.pldroid.playerdemo;

import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.pili.pldroid.player.AVOptions;
import com.pili.pldroid.player.PLMediaPlayer;
import com.pili.pldroid.playerdemo.utils.Utils;

import java.io.IOException;

/**
 *  This demo shows how to use PLMediaPlayer API playing audio stream
 */
public class PLAudioPlayerActivity extends AppCompatActivity {

    private static final String TAG = PLAudioPlayerActivity.class.getSimpleName();

    private static final int MESSAGE_ID_RECONNECTING = 0x01;

    private PLMediaPlayer mMediaPlayer;
    private String mAudioPath;
    private View mLoadingView;
    private AVOptions mAVOptions;
    private boolean mIsStopped = false;
    private boolean mIsActivityPaused = true;
    private Toast mToast = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_player);
        mLoadingView = findViewById(R.id.LoadingView);
        mAudioPath = getIntent().getStringExtra("videoPath");

        mAVOptions = new AVOptions();

        int isLiveStreaming = getIntent().getIntExtra("liveStreaming", 1);
        // the unit of timeout is ms
        mAVOptions.setInteger(AVOptions.KEY_PREPARE_TIMEOUT, 10 * 1000);
        mAVOptions.setInteger(AVOptions.KEY_GET_AV_FRAME_TIMEOUT, 10 * 1000);
        // Some optimization with buffering mechanism when be set to 1
        mAVOptions.setInteger(AVOptions.KEY_LIVE_STREAMING, isLiveStreaming);
        if (isLiveStreaming == 1) {
            mAVOptions.setInteger(AVOptions.KEY_DELAY_OPTIMIZATION, 1);
        }

        // 1 -> hw codec enable, 0 -> disable [recommended]
        int codec = getIntent().getIntExtra("mediaCodec", 0);
        mAVOptions.setInteger(AVOptions.KEY_MEDIACODEC, codec);

        // whether start play automatically after prepared, default value is 1
        mAVOptions.setInteger(AVOptions.KEY_START_ON_PREPARED, 0);

        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        audioManager.requestAudioFocus(null, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);

        prepare();
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
        mIsActivityPaused = false;
        // mMediaPlayer.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mIsActivityPaused = true;
        // mMediaPlayer.pause();
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

    public void release() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    private void prepare() {
        if (mMediaPlayer == null) {
            mMediaPlayer = new PLMediaPlayer(getApplicationContext(), mAVOptions);
            mMediaPlayer.setOnPreparedListener(mOnPreparedListener);
            mMediaPlayer.setOnCompletionListener(mOnCompletionListener);
            mMediaPlayer.setOnErrorListener(mOnErrorListener);
            mMediaPlayer.setOnInfoListener(mOnInfoListener);
            mMediaPlayer.setOnBufferingUpdateListener(mOnBufferingUpdateListener);
            mMediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        }
        try {
            mMediaPlayer.setDataSource(mAudioPath);
            mMediaPlayer.prepareAsync();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private PLMediaPlayer.OnPreparedListener mOnPreparedListener = new PLMediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(PLMediaPlayer mp) {
            Log.i(TAG, "On Prepared !");
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
                case PLMediaPlayer.MEDIA_INFO_AUDIO_RENDERING_START:
                    mLoadingView.setVisibility(View.GONE);
                    break;
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
        }
    };

    /**
     *  Listen the event of playing complete
     *  For playing local file, it's called when reading the file EOF
     *  For playing network stream, it's called when the buffered bytes played over
     *
     *  If setLooping(true) is called, the player will restart automatically
     *  And ｀onCompletion｀ will not be called
     */
    private PLMediaPlayer.OnCompletionListener mOnCompletionListener = new PLMediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(PLMediaPlayer mp) {
            Log.d(TAG, "Play Completed !");
        }
    };

    private PLMediaPlayer.OnErrorListener mOnErrorListener = new PLMediaPlayer.OnErrorListener() {
        @Override
        public boolean onError(PLMediaPlayer mp, int errorCode) {
            boolean isNeedReconnect = false;
            Log.e(TAG, "Error happened, errorCode = " + errorCode);
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
                    isNeedReconnect = true;
                    break;
                case PLMediaPlayer.ERROR_CODE_EMPTY_PLAYLIST:
                    showToastTips("Empty playlist !");
                    break;
                case PLMediaPlayer.ERROR_CODE_STREAM_DISCONNECTED:
                    showToastTips("Stream disconnected !");
                    isNeedReconnect = true;
                    break;
                case PLMediaPlayer.ERROR_CODE_IO_ERROR:
                    showToastTips("Network IO Error !");
                    isNeedReconnect = true;
                    break;
                case PLMediaPlayer.ERROR_CODE_UNAUTHORIZED:
                    showToastTips("Unauthorized Error !");
                    break;
                case PLMediaPlayer.ERROR_CODE_PREPARE_TIMEOUT:
                    showToastTips("Prepare timeout !");
                    isNeedReconnect = true;
                    break;
                case PLMediaPlayer.ERROR_CODE_READ_FRAME_TIMEOUT:
                    showToastTips("Read frame timeout !");
                    isNeedReconnect = true;
                    break;
                case PLMediaPlayer.MEDIA_ERROR_UNKNOWN:
                    break;
                default:
                    showToastTips("unknown error !");
                    break;
            }
            // Todo pls handle the error status here, reconnect or call finish()
            release();
            if (isNeedReconnect) {
                sendReconnectMessage();
            } else {
                finish();
            }
            // Return true means the error has been handled
            // If return false, then `onCompletion` will be called
            return true;
        }
    };

    private void showToastTips(final String tips) {
        if (mIsActivityPaused) {
            return;
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mToast != null) {
                    mToast.cancel();
                }
                mToast = Toast.makeText(PLAudioPlayerActivity.this, tips, Toast.LENGTH_SHORT);
                mToast.show();
            }
        });
    }

    private void sendReconnectMessage() {
        showToastTips("正在重连...");
        mLoadingView.setVisibility(View.VISIBLE);
        mHandler.removeCallbacksAndMessages(null);
        mHandler.sendMessageDelayed(mHandler.obtainMessage(MESSAGE_ID_RECONNECTING), 500);
    }

    protected Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what != MESSAGE_ID_RECONNECTING) {
                return;
            }
            if (mIsActivityPaused || !Utils.isLiveStreamingAvailable()) {
                finish();
                return;
            }
            if (!Utils.isNetworkAvailable(PLAudioPlayerActivity.this)) {
                sendReconnectMessage();
                return;
            }
            // The PLMediaPlayer has moved to the Error state, if you want to retry, must reset first !
            prepare();
        }
    };
}
