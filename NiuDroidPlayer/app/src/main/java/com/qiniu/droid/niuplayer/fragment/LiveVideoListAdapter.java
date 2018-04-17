package com.qiniu.droid.niuplayer.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.pili.pldroid.player.AVOptions;
import com.qiniu.droid.niuplayer.PLVideoTextureActivity;
import com.qiniu.droid.niuplayer.R;

import static com.qiniu.droid.niuplayer.utils.Config.LIVE_TEST_URL;

public class LiveVideoListAdapter extends RecyclerView.Adapter<LiveVideoListAdapter.ViewHolder> {

    private Activity mActivity;

    public LiveVideoListAdapter(Activity activity) {
        mActivity = activity;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        String mPath;
        ImageView mCoverImage;

        public ViewHolder(View itemView) {
            super(itemView);
            mCoverImage = itemView.findViewById(R.id.cover_image);
            mCoverImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mActivity, PLVideoTextureActivity.class);
                    intent.putExtra("videoPath", mPath);
                    intent.putExtra("mediaCodec", AVOptions.MEDIA_CODEC_AUTO);
                    intent.putExtra("liveStreaming", 1);
                    mActivity.startActivity(intent);
                }
            });
        }
    }

    @Override
    public LiveVideoListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.view_live_video, parent, false);
        LiveVideoListAdapter.ViewHolder viewHolder = new LiveVideoListAdapter.ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mPath = LIVE_TEST_URL;
    }

    @Override
    public int getItemCount() {
        return 1;
    }
}
