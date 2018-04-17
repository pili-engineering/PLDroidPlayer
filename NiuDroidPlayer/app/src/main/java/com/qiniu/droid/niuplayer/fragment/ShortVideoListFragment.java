package com.qiniu.droid.niuplayer.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.qiniu.droid.niuplayer.R;
import com.qiniu.droid.niuplayer.model.VideoItem;
import com.qiniu.droid.niuplayer.model.ModelFactory;

import java.util.ArrayList;

import static com.qiniu.droid.niuplayer.utils.Config.SHORT_VIDEO_PATH_PREFIX;

public class ShortVideoListFragment extends Fragment implements FragmentLifecycle {

    private RecyclerView mVideoList;
    private ArrayList<VideoItem> mItemList;
    private ShortVideoListAdapter mShortVideoListAdapter;
    private volatile boolean mShouldPlay;
    private int mCurrentPosition = -1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_short_video_list, container, false);
        initShortVideoData(rootView);
        return rootView;
    }

    private void initShortVideoData(final View rootView) {
        ModelFactory.createVideoItemListByURL(SHORT_VIDEO_PATH_PREFIX, new ModelFactory.OnResultListener() {
            @Override
            public void onSuccess(int statusCode, Object data) {
                mItemList = (ArrayList<VideoItem>) data;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initView(rootView);
                    }
                });
            }

            @Override
            public void onFailure() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "获得短视频列表失败", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    private void initView(View root) {
        mVideoList = root.findViewById(R.id.video_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mVideoList.setLayoutManager(layoutManager);
        mVideoList.setHasFixedSize(true);

        PagerSnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(mVideoList);

        mShortVideoListAdapter = new ShortVideoListAdapter(mItemList);
        mVideoList.setAdapter(mShortVideoListAdapter);
        mVideoList.addOnScrollListener(mOnScrollListener);

        if (mShouldPlay) {
            mVideoList.post(new Runnable() {
                @Override
                public void run() {
                    startCurVideoView();
                    mShouldPlay = false;
                }
            });
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mShortVideoListAdapter != null) {
            mShortVideoListAdapter.pauseCurVideoView();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mShortVideoListAdapter != null) {
            mShortVideoListAdapter.stopCurVideoView();
        }
    }

    @Override
    public void onFragmentPause() {
        if (mShortVideoListAdapter != null) {
            mShortVideoListAdapter.stopCurVideoView();
        }
    }

    @Override
    public void onFragmentResume() {
        if (mShortVideoListAdapter != null) {
            mShortVideoListAdapter.startCurVideoView();
        } else {
            mShouldPlay = true;
        }
    }

    @Override
    public void onActivityPause() {
        if (mShortVideoListAdapter != null) {
            mShortVideoListAdapter.stopCurVideoView();
        }
    }

    @Override
    public void onActivityResume() {
        if (mShortVideoListAdapter != null) {
            mShortVideoListAdapter.startCurVideoView();
        }
    }

    @Override
    public void onActivityDestroy() {
        if (mShortVideoListAdapter != null) {
            mShortVideoListAdapter.stopCurVideoView();
        }
    }

    @Override
    public void onBackPressed() {
        getActivity().finish();
    }

    private RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                startCurVideoView();
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
        }
    };

    private void startCurVideoView() {
        LinearLayoutManager layoutManager = (LinearLayoutManager) mVideoList.getLayoutManager();
        int visibleItemPosition = layoutManager.findLastCompletelyVisibleItemPosition();

        if (visibleItemPosition >= 0 && mCurrentPosition != visibleItemPosition) {
            mShortVideoListAdapter.stopCurVideoView();
            mCurrentPosition = visibleItemPosition;
            View holderView = mVideoList.findViewWithTag(mCurrentPosition);
            if (holderView != null) {
                ShortVideoListAdapter.ViewHolder viewHolder = (ShortVideoListAdapter.ViewHolder) mVideoList.getChildViewHolder(holderView);
                mShortVideoListAdapter.setCurViewHolder(viewHolder);
                mShortVideoListAdapter.startCurVideoView();
            }
        }
    }
}
