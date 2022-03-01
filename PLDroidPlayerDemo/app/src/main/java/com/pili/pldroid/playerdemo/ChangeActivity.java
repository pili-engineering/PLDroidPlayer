package com.pili.pldroid.playerdemo;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.pili.pldroid.player.AVOptions;
import com.pili.pldroid.player.PLOnCompletionListener;
import com.pili.pldroid.player.PLOnErrorListener;
import com.pili.pldroid.player.PLOnInfoListener;
import com.pili.pldroid.player.PLOnPreparedListener;
import com.pili.pldroid.player.PLOnVideoSizeChangedListener;
import com.pili.pldroid.player.widget.PLVideoTextureView;
import com.pili.pldroid.player.widget.PLVideoView;

public class ChangeActivity extends FragmentActivity implements View.OnClickListener {

    private String mLiveUrl;
    //    private PLVideoView mVideoView;
    private PLVideoTextureView mVideoView;
    private Button main2_btn10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change);
        mLiveUrl = getIntent().getStringExtra("key_url");
        main2_btn10=findViewById(R.id.main2_btn10);
        mVideoView=findViewById(R.id.video_view);
        main2_btn10.setOnClickListener(this);
        initLive();
    }

    private void initLive() {
//        mVideoView = new PLVideoView(this);
//        mBinding.main2VideoLayout.removeAllViews();
//        mBinding.main2VideoLayout.addView(mVideoView);
        mVideoView.setOnPreparedListener(new PLOnPreparedListener() {
            @Override
            public void onPrepared(int i) {
                Log.i("==plVideo==", "onPrepared：" + i);
                mVideoView.start();
            }
        });
        mVideoView.setOnInfoListener(new PLOnInfoListener() {
            @Override
            public void onInfo(int i, int i1, Object o) {
                if (i == PLOnInfoListener.MEDIA_INFO_VIDEO_RENDERING_START) {
                    Log.i("==plVideo==", "onInfo：" + i + "，" + i1);
                }
            }
        });
        mVideoView.setOnCompletionListener(new PLOnCompletionListener() {
            @Override
            public void onCompletion() {
                Log.i("==plVideo==", "onCompletion：");
            }
        });
        mVideoView.setOnVideoSizeChangedListener(new PLOnVideoSizeChangedListener() {
            @Override
            public void onVideoSizeChanged(int i, int i1) {
                Log.i("==plVideo==", "onVideoSizeChanged：" + i + "，" + i1);
            }
        });
        mVideoView.setOnErrorListener(new PLOnErrorListener() {
            @Override
            public boolean onError(int i, Object o) {
                Log.i("==plVideo==", "onError：" + i);
                return false;
            }
        });

//        mVideoView.setDisplayAspectRatio(PLVideoView.ASPECT_RATIO_ORIGIN);
        mVideoView.setDisplayAspectRatio(PLVideoView.ASPECT_RATIO_FIT_PARENT);
//        mVideoView.setDisplayAspectRatio(PLVideoView.ASPECT_RATIO_PAVED_PARENT);
//        mVideoView.setDisplayAspectRatio(PLVideoView.ASPECT_RATIO_16_9);
//        mVideoView.setDisplayAspectRatio(PLVideoView.ASPECT_RATIO_4_3);

        mVideoView.setAVOptions(getAVOptions());

        mVideoView.setVideoPath(url1);
    }

    public final static int MEDIA_CODEC_SW_DECODE = 0;
    public final static int MEDIA_CODEC_HW_DECODE = 1;
    public final static int MEDIA_CODEC_AUTO = 2;

    private AVOptions getAVOptions() {
        AVOptions options = new AVOptions();

        // DNS 服务器设置
        // 若不设置此项，则默认使用 DNSPod 的 httpdns 服务
        // 若设置为 127.0.0.1，则会使用系统的 DNS 服务器
        // 若设置为其他 DNS 服务器地址，则会使用设置的服务器
//        options.setString(AVOptions.KEY_DNS_SERVER, server);

        // DNS 缓存设置
        // 若不设置此项，则每次播放未缓存的域名时都会进行 DNS 解析，并将结果缓存
        // 参数为 String[]，包含了要缓存 DNS 结果的域名列表
        // SDK 在初始化时会解析列表中的域名，并将结果缓存
//        options.setStringArray(AVOptions.KEY_DOMAIN_LIST, domainList);

        // 解码方式:
        // codec＝AVOptions.MEDIA_CODEC_HW_DECODE，硬解
        // codec=AVOptions.MEDIA_CODEC_SW_DECODE, 软解
        // codec=AVOptions.MEDIA_CODEC_AUTO, 硬解优先，失败后自动切换到软解
        // 默认值是：MEDIA_CODEC_SW_DECODE
        options.setInteger(AVOptions.KEY_MEDIACODEC, AVOptions.MEDIA_CODEC_SW_DECODE);

        // 是否开启直播优化，1 为开启，0 为关闭。若开启，视频暂停后再次开始播放时会触发追帧机制
        // 默认为 0
        options.setInteger(AVOptions.KEY_LIVE_STREAMING, 1);

        // 快开模式，启用后会加快该播放器实例再次打开相同协议的视频流的速度
        options.setInteger(AVOptions.KEY_FAST_OPEN, 1);

        // 打开重试次数，设置后若打开流地址失败，则会进行重试
        options.setInteger(AVOptions.KEY_OPEN_RETRY_TIMES, 5);

        // 预设置 SDK 的 log 等级， 0-4 分别为 v/d/i/w/e
        options.setInteger(AVOptions.KEY_LOG_LEVEL, 0);

        // 打开视频时单次 http 请求的超时时间，一次打开过程最多尝试五次
        // 单位为 ms
        options.setInteger(AVOptions.KEY_PREPARE_TIMEOUT, 10 * 1000);

        // 默认的缓存大小，单位是 ms
        // 默认值是：500
        options.setInteger(AVOptions.KEY_CACHE_BUFFER_DURATION, 500);

        // 最大的缓存大小，单位是 ms
        // 默认值是：2000，若设置值小于 KEY_CACHE_BUFFER_DURATION 则不会生效
        options.setInteger(AVOptions.KEY_MAX_CACHE_BUFFER_DURATION, 4000);

        // 设置拖动模式，1 位精准模式，即会拖动到时间戳的那一秒；0 为普通模式，会拖动到时间戳最近的关键帧。默认为 0
        options.setInteger(AVOptions.KEY_SEEK_MODE, 0);

        // 设置 HLS DRM 密钥
//        byte[] key = {0x##,0x##,0x##,0x##,0x##, ……};
//        options.setByteArray(AVOptions.KEY_DRM_KEY, key);

        // 设置 MP4 DRM 密钥
//        String key = "AbcDefgh";
//        options.setString(AVOptions.KEY_COMP_DRM_KEY, key);

        // 设置偏好的视频格式，设置后会加快对应格式视频流的打开速度，但播放其他格式会出错
        // m3u8 = 1, mp4 = 2, flv = 3
//        options.setInteger(AVOptions.KEY_PREFER_FORMAT, 1);

        // 开启解码后的视频数据回调
        // 默认值为 0，设置为 1 则开启
        options.setInteger(AVOptions.KEY_VIDEO_DATA_CALLBACK, 1);

        // 开启解码后的音频数据回调
        // 默认值为 0，设置为 1 则开启
        options.setInteger(AVOptions.KEY_AUDIO_DATA_CALLBACK, 1);

        // 设置开始播放位置
        // 默认不开启，单位为 ms
//        options.setInteger(AVOptions.KEY_START_POSITION, 10 * 1000);

        // 请在开始播放之前配置
//        mVideoView.setAVOptions(options);
        return options;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main2_btn1:
                mVideoView.setDisplayAspectRatio(PLVideoView.ASPECT_RATIO_ORIGIN);
                break;
            case R.id.main2_btn2:
                mVideoView.setDisplayAspectRatio(PLVideoView.ASPECT_RATIO_FIT_PARENT);
                break;
            case R.id.main2_btn3:
                mVideoView.setDisplayAspectRatio(PLVideoView.ASPECT_RATIO_PAVED_PARENT);
                break;
            case R.id.main2_btn4:
                mVideoView.setDisplayAspectRatio(PLVideoView.ASPECT_RATIO_16_9);
                break;
            case R.id.main2_btn5:
                mVideoView.setDisplayAspectRatio(PLVideoView.ASPECT_RATIO_4_3);
                break;
            case R.id.main2_btn6:
                mVideoView.start();
                break;
            case R.id.main2_btn7:
                mVideoView.pause();
                break;
            case R.id.main2_btn8:
                mVideoView.stop();
                break;
            case R.id.main2_btn9:
                mVideoView.stopPlayback();
                break;
            case R.id.main2_btn10:
                if (first) {
                    first = false;
                    mVideoView.setVideoPath(url4);
                } else {
                    first = true;
                    mVideoView.setVideoPath(url1);
                }
//                mVideoView.start();
//                initLive();
                break;
        }
    }

    private boolean first = true;
    private String url1 = "rtmp://pili-rtmp.qnsdk.com/sdk-live/timestamp";
    private String url2 = "rtmp://pili-live-rtmp.nuannuanapp.com/mengquwwvd/8710070_stream1645168497652";
    private String url3 = "rtmp://pili-live-rtmp.nuannuanapp.com/mengquwwvd/8979207_stream1645164069821";
    private String url4 = "rtmp://pili-publish.qnsdk.com/sdk-live/6666";

    @Override
    protected void onPause() {
        super.onPause();
        mVideoView.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mVideoView.stopPlayback();
    }
}