package com.qiniu.droid.niuplayer.fragment;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.pili.pldroid.player.PLOnInfoListener;
import com.pili.pldroid.player.widget.PLVideoTextureView;
import com.qiniu.droid.niuplayer.R;
import com.qiniu.droid.niuplayer.model.VideoItem;

import java.util.ArrayList;

import static com.qiniu.droid.niuplayer.utils.Utils.createAVOptions;

public class ShortVideoListAdapter extends RecyclerView.Adapter<ShortVideoListAdapter.ViewHolder> {
    private ArrayList<VideoItem> mItemList;
    private ViewHolder mCurViewHolder;
    private DisplayImageOptions mDisplayImageOptions;

    public ShortVideoListAdapter(ArrayList<VideoItem> arrayList) {
        mItemList = arrayList;
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
        PLVideoTextureView videoView;
        ImageView coverImage;
        TextView nameText;
        TextView detailText;
        String videoPath;
        String coverPath;
        View topView;
        ImageButton pausePlayImage;
        View holderRootView;

        public ViewHolder(View itemView) {
            super(itemView);
            holderRootView = itemView;
            videoView = itemView.findViewById(R.id.video_texture_view);
            coverImage = itemView.findViewById(R.id.cover_image);
            nameText = itemView.findViewById(R.id.name_text);
            detailText = itemView.findViewById(R.id.detail_text);
            pausePlayImage = itemView.findViewById(R.id.image_pause_play);
            topView = itemView.findViewById(R.id.top_view);

            videoView.setAVOptions(createAVOptions());
            videoView.setDisplayAspectRatio(PLVideoTextureView.ASPECT_RATIO_PAVED_PARENT);
            View loadingView = itemView.findViewById(R.id.loading_view);
            videoView.setBufferingIndicator(loadingView);
            videoView.setOnInfoListener(new PLOnInfoListener() {
                @Override
                public void onInfo(int i, int i1) {
                    if (i == PLOnInfoListener.MEDIA_INFO_VIDEO_RENDERING_START) {
                        coverImage.setVisibility(View.GONE);
                    }
                }
            });

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
        holder.detailText.setText(videoItem.getTime());
        holder.holderRootView.setTag(position);
        holder.videoView.setLooping(true);
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
        holder.videoView.stopPlayback();
    }

    public void setCurViewHolder(ViewHolder viewHolder) {
        mCurViewHolder = viewHolder;
    }

    public void startCurVideoView() {
        if (mCurViewHolder != null && !mCurViewHolder.videoView.isPlaying()) {
            mCurViewHolder.videoView.setVideoPath(mCurViewHolder.videoPath);
            mCurViewHolder.videoView.start();
            mCurViewHolder.pausePlayImage.setVisibility(View.GONE);
        }
    }

    public void pauseCurVideoView() {
        if (mCurViewHolder != null) {
            mCurViewHolder.videoView.pause();
        }
    }

    public void stopCurVideoView() {
        if (mCurViewHolder != null) {
            mCurViewHolder.videoView.stopPlayback();
            mCurViewHolder.coverImage.setVisibility(View.VISIBLE);
        }
    }
}