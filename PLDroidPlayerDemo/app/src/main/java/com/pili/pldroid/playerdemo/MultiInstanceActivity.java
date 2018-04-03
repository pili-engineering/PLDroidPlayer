package com.pili.pldroid.playerdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.pili.pldroid.player.widget.PLVideoView;
import com.pili.pldroid.playerdemo.utils.Utils;

public class MultiInstanceActivity extends AppCompatActivity {

    private PLVideoView mFirstVideoView;
    private PLVideoView mSecondVideoView;

    private String mPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_instance);

        mFirstVideoView = findViewById(R.id.first_view);
        mSecondVideoView = findViewById(R.id.second_view);

        mPath = getIntent().getStringExtra("videoPath");
    }

    @Override
    protected void onPause() {
        super.onPause();
        mFirstVideoView.pause();
        mSecondVideoView.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mFirstVideoView.stopPlayback();
        mSecondVideoView.stopPlayback();
    }

    public void click(View v) {
        switch (v.getId()) {
            case R.id.first_prepare:
                mFirstVideoView.setVideoPath(mPath);
                break;
            case R.id.second_prepare:
                mSecondVideoView.setVideoPath(mPath);
                break;
            case R.id.first_play:
                mFirstVideoView.start();
                break;
            case R.id.second_play:
                mSecondVideoView.start();
            case R.id.first_download:
                mFirstVideoView.setBufferingEnabled(false);
                break;
            case R.id.second_download:
                mSecondVideoView.setBufferingEnabled(false);
                break;
            case R.id.first_length:
                Utils.showToastTips(this, "First buffer: " + mFirstVideoView.getHttpBufferSize());
                break;
            case R.id.second_length:
                Utils.showToastTips(this, "Second buffer: " + mSecondVideoView.getHttpBufferSize());
            default:
                break;
        }
    }
}
