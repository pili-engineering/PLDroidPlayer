package com.pili.pldroid.playerdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.pili.pldroid.player.widget.PLVideoTextureView;

import java.util.ArrayList;
import java.util.List;

public class PLVideoListActivity extends AppCompatActivity {

    private RecyclerView mVideoListView;
    private VideoListAdapter mAdapter;
    private List<String> mPlayList = new ArrayList<>(5);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_list);
        for (int i = 0; i < 5; ++i) {
            mPlayList.add(i, getIntent().getStringExtra("videoPath"));
        }
        mAdapter = new VideoListAdapter(mPlayList);
        mVideoListView = findViewById(R.id.video_list);
        mVideoListView.setHasFixedSize(true);
        mVideoListView.setLayoutManager(new LinearLayoutManager(this));
        mVideoListView.setAdapter(mAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAdapter.stopAll();
        mVideoListView.setAdapter(null);
    }

    class VideoListAdapter extends RecyclerView.Adapter<VideoListAdapter.ViewHolder> {
        List<String> mVideoPathList;
        List<ViewHolder> mViewHolderList;

        class ViewHolder extends RecyclerView.ViewHolder {
            PLVideoTextureView mVideoView;
            String mVideoPath = "";
            ViewHolder(PLVideoTextureView view) {
                super(view);
                mVideoView = view;
            }
        }

        VideoListAdapter(List<String> list) {
            mVideoPathList = list;
            mViewHolderList = new ArrayList<>();
        }

        void stopAll() {
            for (ViewHolder vh : mViewHolderList) {
                vh.mVideoView.stopPlayback();
            }
        }

        @Override
        public VideoListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            PLVideoTextureView view = (PLVideoTextureView) LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_video_list_item, parent, false);
            view.setDisplayAspectRatio(PLVideoTextureView.ASPECT_RATIO_PAVED_PARENT);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int pos) {
            holder.mVideoPath = mPlayList.get(pos);
        }

        @Override
        public void onViewAttachedToWindow(ViewHolder holder) {
            holder.mVideoView.setVideoPath(holder.mVideoPath);
            holder.mVideoView.start();
            mViewHolderList.add(holder);
        }

        @Override
        public void onViewDetachedFromWindow(ViewHolder holder) {
            holder.mVideoView.pause();
            mViewHolderList.remove(holder);
        }

        @Override
        public int getItemCount() {
            return mVideoPathList.size();
        }
    }
}
