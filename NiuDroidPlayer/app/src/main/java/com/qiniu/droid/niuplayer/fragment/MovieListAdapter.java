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
import com.qiniu.droid.niuplayer.widget.MediaController;

import java.util.ArrayList;

import static com.qiniu.droid.niuplayer.utils.Utils.createAVOptions;
import static com.qiniu.droid.niuplayer.widget.PlayConfigView.BACKSTAGE_PLAY_TAG;

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.ViewHolder> {

    public interface OnFullScreenListener {
        void onFullScreen(PLVideoTextureView videoView, MediaController mediaController);
    }

    private MovieListAdapter.OnFullScreenListener mOnFullScreenListener;
    private MovieListAdapter.ViewHolder mCurViewHolder;
    private DisplayImageOptions mDisplayImageOptions;
    private ArrayList<VideoItem> mItemList;

    public MovieListAdapter(ArrayList<VideoItem> arrayList) {
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

    public void setOnFullScreenListener(MovieListAdapter.OnFullScreenListener listener) {
        mOnFullScreenListener = listener;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        PLVideoTextureView videoView;
        String videoPath;
        String coverPath;
        ImageButton stopPlayImage;
        ImageView coverImage;
        TextView detailText;
        TextView nameText;
        View loadingView;
        ImageButton fullScreenImage;

        public ViewHolder(View itemView) {
            super(itemView);

            coverImage = itemView.findViewById(R.id.cover_image);
            stopPlayImage = itemView.findViewById(R.id.cover_stop_play);
            videoView = itemView.findViewById(R.id.video_texture_view);
            detailText = itemView.findViewById(R.id.detail_text);
            nameText = itemView.findViewById(R.id.name_text);
            loadingView = itemView.findViewById(R.id.loading_view);
            fullScreenImage = itemView.findViewById(R.id.full_screen_image);
            final MediaController mediaController = itemView.findViewById(R.id.media_controller);

            videoView.setAVOptions(createAVOptions());
            videoView.setBufferingIndicator(loadingView);
            videoView.setMediaController(mediaController);
            videoView.setDisplayAspectRatio(PLVideoTextureView.ASPECT_RATIO_PAVED_PARENT);
            videoView.setLooping(true);
            videoView.setOnInfoListener(new PLOnInfoListener() {
                @Override
                public void onInfo(int i, int i1) {
                    if (i == PLOnInfoListener.MEDIA_INFO_VIDEO_RENDERING_START) {
                        coverImage.setVisibility(View.GONE);
                        stopPlayImage.setVisibility(View.GONE);
                        mediaController.hide();
                    }
                }
            });

            coverImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    stopCurVideoView();
                    mCurViewHolder = ViewHolder.this;
                    startCurVideoView();
                }
            });

            fullScreenImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnFullScreenListener != null) {
                        mOnFullScreenListener.onFullScreen(ViewHolder.this.videoView, mediaController);
                    }
                }
            });
        }
    }

    @Override
    public MovieListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.view_movie_video, parent, false);
        MovieListAdapter.ViewHolder viewHolder = new MovieListAdapter.ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        VideoItem item = mItemList.get(position);
        holder.videoPath = item.getVideoPath();
        holder.coverPath = item.getCoverPath();
        holder.detailText.setText(item.getTime());
        holder.nameText.setText(item.getName());
        ImageLoader.getInstance().displayImage(holder.coverPath, holder.coverImage, mDisplayImageOptions);
    }

    @Override
    public int getItemCount() {
        return mItemList.size();
    }

    @Override
    public void onViewDetachedFromWindow(MovieListAdapter.ViewHolder holder) {
        holder.videoView.pause();
        holder.loadingView.setVisibility(View.GONE);
        holder.coverImage.setVisibility(View.VISIBLE);
        holder.stopPlayImage.setVisibility(View.VISIBLE);
    }

    public void startCurVideoView() {
        if (mCurViewHolder != null) {
            mCurViewHolder.videoView.setVideoPath(mCurViewHolder.videoPath);
            mCurViewHolder.videoView.start();
            mCurViewHolder.loadingView.setVisibility(View.VISIBLE);
            mCurViewHolder.stopPlayImage.setVisibility(View.GONE);
        }
    }

    public void restartCurVideoView() {
        if (mCurViewHolder != null) {
            mCurViewHolder.videoView.start();
            mCurViewHolder.stopPlayImage.setVisibility(View.GONE);
        }
    }

    public boolean isCurVideoPlaying() {
        return mCurViewHolder != null && mCurViewHolder.videoView.isPlaying();
    }

    public boolean needBackstagePlay() {
        return mCurViewHolder != null && BACKSTAGE_PLAY_TAG.equals(mCurViewHolder.videoView.getTag());
    }

    public void pauseCurVideoView() {
        if (mCurViewHolder != null) {
            mCurViewHolder.videoView.pause();
            mCurViewHolder.loadingView.setVisibility(View.GONE);
        }
    }

    private void resetConfig() {
        if (mCurViewHolder != null) {
            mCurViewHolder.videoView.setRotation(0);
            mCurViewHolder.videoView.setMirror(false);
            mCurViewHolder.videoView.setDisplayAspectRatio(PLVideoTextureView.ASPECT_RATIO_PAVED_PARENT);
        }
    }

    public void stopCurVideoView() {
        if (mCurViewHolder != null) {
            resetConfig();
            mCurViewHolder.videoView.stopPlayback();
            mCurViewHolder.loadingView.setVisibility(View.GONE);
            mCurViewHolder.coverImage.setVisibility(View.VISIBLE);
            mCurViewHolder.stopPlayImage.setVisibility(View.VISIBLE);
        }
    }
}
