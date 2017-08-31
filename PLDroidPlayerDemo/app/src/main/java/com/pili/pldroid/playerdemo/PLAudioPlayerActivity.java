package com.pili.pldroid.playerdemo;

import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.pili.pldroid.player.AVOptions;
import com.pili.pldroid.player.PLMediaPlayer;

import java.io.IOException;

/**
 *  This demo shows how to use PLMediaPlayer API playing audio stream
 */
public class PLAudioPlayerActivity extends AppCompatActivity {

    private static final String TAG = PLAudioPlayerActivity.class.getSimpleName();

    private PLMediaPlayer mMediaPlayer;
    private String mAudioPath;
    private View mLoadingView;
    private AVOptions mAVOptions;
    private boolean mIsStopped = false;
    private Toast mToast = null;

    private TelephonyManager mTelephonyManager;
    private PhoneStateListener mPhoneStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_player);
        mLoadingView = findViewById(R.id.LoadingView);
        mAudioPath = getIntent().getStringExtra("videoPath");

        mAVOptions = new AVOptions();
        // the unit of timeout is ms
        mAVOptions.setInteger(AVOptions.KEY_PREPARE_TIMEOUT, 10 * 1000);
        // 1 -> hw codec enable, 0 -> disable [recommended]
        int codec = getIntent().getIntExtra("mediaCodec", 0);
        mAVOptions.setInteger(AVOptions.KEY_MEDIACODEC, codec);

        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        audioManager.requestAudioFocus(null, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);

        prepare();
        startTelephonyListener();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopTelephonyListener();
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
            mMediaPlayer.setDebugLoggingEnabled(true);
            mMediaPlayer.setLooping(getIntent().getBooleanExtra("loop", false));
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private PLMediaPlayer.OnPreparedListener mOnPreparedListener = new PLMediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(PLMediaPlayer mp, int preparedTime) {
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

    // Listen to the telephone
    private void startTelephonyListener() {
        mTelephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if (mTelephonyManager == null) {
            Log.e(TAG, "Failed to initialize TelephonyManager!!!");
            return;
        }

        mPhoneStateListener = new PhoneStateListener() {

            @Override
            public void onCallStateChanged(int state, String incomingNumber) {
                super.onCallStateChanged(state, incomingNumber);
                switch (state) {
                    case TelephonyManager.CALL_STATE_IDLE:
                        Log.d(TAG, "PhoneStateListener: CALL_STATE_IDLE");
                        if (mMediaPlayer != null) {
                            mMediaPlayer.start();
                        }
                        break;
                    case TelephonyManager.CALL_STATE_OFFHOOK:
                        Log.d(TAG, "PhoneStateListener: CALL_STATE_OFFHOOK");
                        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
                            mMediaPlayer.pause();
                        }
                        break;
                    case TelephonyManager.CALL_STATE_RINGING:
                        Log.d(TAG, "PhoneStateListener: CALL_STATE_RINGING: " + incomingNumber);
                        break;
                }
            }
        };

        try {
            mTelephonyManager.listen(mPhoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void stopTelephonyListener() {
        if (mTelephonyManager != null && mPhoneStateListener != null) {
            mTelephonyManager.listen(mPhoneStateListener, PhoneStateListener.LISTEN_NONE);
            mTelephonyManager = null;
            mPhoneStateListener = null;
        }
    }

    private void showToastTips(final String tips) {
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
}
