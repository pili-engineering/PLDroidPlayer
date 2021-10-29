package com.qiniu.droid.niuplayer.fragment;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Choreographer;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.pili.pldroid.player.AVOptions;
import com.pili.pldroid.player.PLOnErrorListener;
import com.pili.pldroid.player.PLOnInfoListener;
import com.pili.pldroid.player.PLOnVideoFrameListener;
import com.pili.pldroid.player.widget.PLShortVideoTextureView;
import com.pili.pldroid.player.widget.PLVideoTextureView;
import com.qiniu.droid.niuplayer.R;
import com.qiniu.droid.niuplayer.model.VideoItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import static com.qiniu.droid.niuplayer.utils.Utils.createAVOptions;

public class ShortVideoListAdapter extends RecyclerView.Adapter<ShortVideoListAdapter.ViewHolder> {
    private ArrayList<VideoItem> mItemList;
    private ViewHolder mCurViewHolder;
    private DisplayImageOptions mDisplayImageOptions;
    private long startsystemtime;
    private PLShortVideoTextureView mVideoView;
    private int mCacheStartIndex = -1;
    private int mCacheEndIndex = -1;
    private final static int CACHE_RANGE = 5;
    private HashSet<Integer> mCacheIndexes = new HashSet<Integer>();
    private AVOptions mAvOption;


    public ShortVideoListAdapter(ArrayList<VideoItem> arrayList, PLShortVideoTextureView videoView) {
        mItemList = arrayList;
        mVideoView = videoView;
        mAvOption = createAVOptions();
        mVideoView.setAVOptions(mAvOption);

        mDisplayImageOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.defualt_bg)            //加载图片时的图片
                .showImageForEmptyUri(R.drawable.defualt_bg)         //没有图片资源时的默认图片
                .showImageOnFail(R.drawable.defualt_bg)              //加载失败时的图片
                .cacheInMemory(true)                               //启用内存缓存
                .cacheOnDisk(true)                                 //启用外存缓存
                .considerExifParams(true)                          //启用EXIF和JPEG图像格式
                .build();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        FrameLayout videoViewFrameLayout;
        PLShortVideoTextureView videoView;
        ImageView coverImage;
        TextView nameText;
        TextView detailText;
        int index;
        String videoPath;
        String coverPath;
        View topView;
        ImageButton pausePlayImage;
        View holderRootView;
        PLOnErrorListener mPLOnErrorListener = new PLOnErrorListener() {
            @Override
            public boolean onError(int i, Object extraData) {
               Log.d("aa", String.valueOf(i));
                return false;
            }
        };
        PLOnInfoListener mPLOnInfoListener = new PLOnInfoListener() {
            @Override
            public void onInfo(int i, int i1, Object extraData) {
                if (i == PLOnInfoListener.MEDIA_INFO_VIDEO_RENDERING_START) {
                    coverImage.setVisibility(View.GONE);
                    mVideoView.getTextureView().setAlpha(1.0f);
                    Log.d("ShortVideoListAdapter", "first frame time:" + (System.currentTimeMillis() - startsystemtime) + "ms i1=" + i1 + "ms");
                    changeCache();
                }
            }
        };

        PLOnVideoFrameListener mPLOnVideoFrameListener = new PLOnVideoFrameListener() {
            @Override
            public void onVideoFrameAvailable(byte[] data, int size, int width, int height, int format, long ts) {
                Log.d("ShortVideo", "video data ts=" + ts);
            }
        };



        public ViewHolder(View itemView) {
            super(itemView);
            holderRootView = itemView;
            videoViewFrameLayout = itemView.findViewById(R.id.video_texture_view);
            coverImage = itemView.findViewById(R.id.cover_image);
            nameText = itemView.findViewById(R.id.name_text);
            detailText = itemView.findViewById(R.id.detail_text);
            pausePlayImage = itemView.findViewById(R.id.image_pause_play);
            topView = itemView.findViewById(R.id.top_view);


            topView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (videoView.isPlaying()) {
                        videoView.pause();
                        pausePlayImage.setVisibility(View.VISIBLE);
                    } else {
                        videoView.start();
                        pausePlayImage.setVisibility(View.GONE);
                    }
                }
            });
        }

        public void onStop() {
            videoViewFrameLayout.removeView(this.videoView);
        }

        public void onStart(PLShortVideoTextureView videoView) {
            this.videoView = videoView;
            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            lp.gravity = Gravity.CENTER;
            if (this.videoView.getParent() == null) {
                videoViewFrameLayout.addView(this.videoView, lp);
            }
            this.videoView.setDisplayAspectRatio(PLVideoTextureView.ASPECT_RATIO_PAVED_PARENT);

            this.videoView.setLooping(true);
            this.videoView.setOnInfoListener(mPLOnInfoListener);
//            this.videoView.setOnVideoFrameListener(mPLOnVideoFrameListener);
            this.videoView.setOnErrorListener(mPLOnErrorListener);
            View loadingView = itemView.findViewById(R.id.loading_view);
            this.videoView.setBufferingIndicator(loadingView);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.view_short_video, parent, false);
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        VideoItem videoItem = mItemList.get(position);
        holder.videoPath = videoItem.getVideoPath();
        holder.coverPath = videoItem.getCoverPath();
        ImageLoader.getInstance().displayImage(holder.coverPath, holder.coverImage, mDisplayImageOptions);
        holder.nameText.setText(videoItem.getName());
        holder.index = position;
        holder.detailText.setText(videoItem.getTime());
        holder.holderRootView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return mItemList.size();
    }

    @Override
    public void onViewAttachedToWindow(ViewHolder holder) {
        mCurViewHolder = holder;
        holder.pausePlayImage.setVisibility(View.GONE);
        holder.coverImage.setVisibility(View.VISIBLE);
    }

    @Override
    public void onViewDetachedFromWindow(ViewHolder holder) {
        holder.onStop();
    }

    public void setCurViewHolder(ViewHolder viewHolder) {
        mCurViewHolder = viewHolder;
    }

    public void startCurVideoView() {
        if (mCurViewHolder != null) {
            mCurViewHolder.onStart(mVideoView);
            mVideoView.stop();
            mVideoView.setAVOptions(mAvOption);
            mVideoView.setVideoPath(mCurViewHolder.videoPath);
            startsystemtime = System.currentTimeMillis();
            mVideoView.getTextureView().setAlpha(0.0f);

//            mVideoView.start();
            mCurViewHolder.pausePlayImage.setVisibility(View.GONE);
        }
    }

    private void changeCache() {
        //第一次
        if (mCacheStartIndex == -1 && mCacheEndIndex == -1) {
            for (int i = mCurViewHolder.index - 1; i <= mCurViewHolder.index + 3; i++) {
                if (i >= 0 && i < mItemList.size() && !mCacheIndexes.contains(i)) {
                    mVideoView.addCache(mItemList.get(i).getVideoPath());
                    mCacheIndexes.add(i);
                }
            }
            mCacheStartIndex = 0;
        } else {
            //删除 出range的cache
            int remove_index = mCurViewHolder.index - 2;
            if (remove_index >= 0 && remove_index < mItemList.size() && mCacheIndexes.contains(remove_index)) {
                mVideoView.delCache(mItemList.get(remove_index).getVideoPath());
                mCacheIndexes.remove(remove_index);
            }

            remove_index = mCurViewHolder.index + 4;
            if (remove_index >= 0 && remove_index < mItemList.size() && mCacheIndexes.contains(remove_index)) {
                mVideoView.delCache(mItemList.get(remove_index).getVideoPath());
                mCacheIndexes.remove(remove_index);
            }

            //增加新进range的cache
            for (int i = mCurViewHolder.index - 1; i <= mCurViewHolder.index + 3; i++) {
                if (i >= 0 && i < mItemList.size() && !mCacheIndexes.contains(i)) {
                    mVideoView.addCache(mItemList.get(i).getVideoPath());
                    mCacheIndexes.add(i);
                }
            }
        }
    }

    public void pauseCurVideoView() {
        if (mCurViewHolder != null) {
            mVideoView.pause();
        }
    }

    public void stopCurVideoView() {
        if (mCurViewHolder != null) {
            mVideoView.stop();
            mCurViewHolder.coverImage.setVisibility(View.VISIBLE);
        }
    }
}