package com.qiniu.droid.niuplayer.fragment;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import com.pili.pldroid.player.PLOnImageCapturedListener;
import com.pili.pldroid.player.widget.PLVideoTextureView;
import com.qiniu.droid.niuplayer.MainActivity;
import com.qiniu.droid.niuplayer.R;
import com.qiniu.droid.niuplayer.model.VideoItem;
import com.qiniu.droid.niuplayer.model.ModelFactory;
import com.qiniu.droid.niuplayer.utils.Config;
import com.qiniu.droid.niuplayer.widget.GestureControllerListener;
import com.qiniu.droid.niuplayer.widget.MediaController;
import com.qiniu.droid.niuplayer.widget.PlayConfigView;

import java.util.ArrayList;

import static com.qiniu.droid.niuplayer.utils.Config.MOVIE_PATH_PREFIX;
import static com.qiniu.droid.niuplayer.utils.Utils.savePhotoToSDCard;

public class MovieListFragment extends Fragment implements FragmentLifecycle, View.OnClickListener, MovieListAdapter.OnFullScreenListener, View.OnTouchListener {

    private RecyclerView mVideoList;
    private PLVideoTextureView mCurVideoView;
    private FrameLayout mFullScreenGroup;
    private ArrayList<VideoItem> mItemList;
    private MovieListAdapter mMovieListAdapter;
    private ViewGroup mTitleBar;
    private ViewGroup mCurViewHolder;
    private PlayConfigView mPlayConfigView;
    private MediaController mLandscapeMC;
    private MediaController mPortraitMC;
    private GestureDetector mGestureDetector;
    private boolean mNeedRestart;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_list, container, false);
        initMovieData(rootView);
        return rootView;
    }

    private void initView(View root) {
        mVideoList = root.findViewById(R.id.video_list);
        mTitleBar = root.findViewById(R.id.title_bar);
        mFullScreenGroup = root.findViewById(R.id.full_screen_group);
        mFullScreenGroup.setVisibility(View.GONE);
        mLandscapeMC = root.findViewById(R.id.media_controller);
        mPlayConfigView = root.findViewById(R.id.play_config_view);

        ImageButton imageButton = root.findViewById(R.id.more_image_btn);
        imageButton.setOnClickListener(this);
        imageButton = root.findViewById(R.id.path_image);
        imageButton.setOnClickListener(this);
        imageButton = root.findViewById(R.id.back_image_btn);
        imageButton.setOnClickListener(this);
        imageButton = root.findViewById(R.id.scan_image);
        imageButton.setOnClickListener(this);
        imageButton = root.findViewById(R.id.screen_short_image);
        imageButton.setOnClickListener(this);

        mLandscapeMC.setOnTouchListener(this);
        mPlayConfigView.setOnTouchListener(this);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mVideoList.setLayoutManager(layoutManager);
        mVideoList.setHasFixedSize(true);
        mMovieListAdapter = new MovieListAdapter(mItemList);
        mMovieListAdapter.setOnFullScreenListener(this);
        mVideoList.setAdapter(mMovieListAdapter);

        mGestureDetector = new GestureDetector(new GestureControllerListener(getActivity()));
    }

    private void initMovieData(final View rootView) {
        ModelFactory.createVideoItemListByURL(MOVIE_PATH_PREFIX, new ModelFactory.OnResultListener() {
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
                getMainActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "获得长视频列表失败", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    private void onPortraitChanged() {
        if (mCurVideoView == null) {
            return;
        }
        mFullScreenGroup.setVisibility(View.GONE);
        mFullScreenGroup.removeAllViews();

        mVideoList.setVisibility(View.VISIBLE);
        mTitleBar.setVisibility(View.VISIBLE);

        mCurVideoView.setDisplayAspectRatio(PLVideoTextureView.ASPECT_RATIO_PAVED_PARENT);
        mCurViewHolder.addView(mCurVideoView, -1);
        mCurVideoView.setMediaController(mPortraitMC);
        mPortraitMC.setAnchorView(mCurVideoView);

        getMainActivity().setTabViewVisible(true);
    }

    private void onLandscapeChanged() {
        if (mCurVideoView == null) {
            return;
        }
        ViewGroup viewGroup = (ViewGroup) mCurVideoView.getParent();
        viewGroup.removeAllViews();
        mVideoList.setVisibility(View.GONE);

        mCurViewHolder = viewGroup;

        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.gravity = Gravity.CENTER;
        mFullScreenGroup.addView(mCurVideoView, layoutParams);
        mFullScreenGroup.setVisibility(View.VISIBLE);
        mCurVideoView.setDisplayAspectRatio(PLVideoTextureView.ASPECT_RATIO_PAVED_PARENT);
        mTitleBar.setVisibility(View.GONE);
        getMainActivity().setTabViewVisible(false);

        mCurVideoView.setMediaController(mLandscapeMC);
        mLandscapeMC.setOnShownListener(new MediaController.OnShownListener() {
            @Override
            public void onShown() {
                if (mPlayConfigView.getVisibility() == View.VISIBLE) {
                    mPlayConfigView.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            onPortraitChanged();
        } else {
            onLandscapeChanged();
        }
    }

    private void captureImage() {
        if (!getMainActivity().isStoragePermissionOK()) {
            return;
        }
        mCurVideoView.setOnImageCapturedListener(new PLOnImageCapturedListener() {
            @Override
            public void onImageCaptured(byte[] data) {
                Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
                if (savePhotoToSDCard(bmp, Config.DEFAULT_CACHE_DIR_PATH, Long.toString(System.currentTimeMillis()))) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext().getApplicationContext(), "屏幕截取成功，已保存在 ：" + Config.DEFAULT_CACHE_DIR_PATH, Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
        mCurVideoView.captureImage(100);
    }

    public MainActivity getMainActivity() {
        return (MainActivity) getActivity();
    }

    @Override
    public void onFragmentPause() {
        if (mMovieListAdapter != null) {
            mMovieListAdapter.stopCurVideoView();
        }
    }

    @Override
    public void onFragmentResume() {
    }

    @Override
    public void onActivityPause() {
        if (mMovieListAdapter != null && !mMovieListAdapter.needBackstagePlay()) {
            mNeedRestart = mMovieListAdapter.isCurVideoPlaying();
            if (mNeedRestart) {
                mMovieListAdapter.pauseCurVideoView();
            } else {
                mMovieListAdapter.stopCurVideoView();
            }
        }
    }

    @Override
    public void onActivityResume() {
        if (mMovieListAdapter != null) {
            if (mNeedRestart) {
                mMovieListAdapter.restartCurVideoView();
                mNeedRestart = false;
            }
        }
    }

    @Override
    public void onActivityDestroy() {
        if (mMovieListAdapter != null) {
            mMovieListAdapter.stopCurVideoView();
        }
    }

    @Override
    public void onBackPressed() {
        if (mFullScreenGroup.getVisibility() == View.VISIBLE) {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            getActivity().finish();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.more_image_btn:
                mLandscapeMC.hide();
                mPlayConfigView.setVisibility(View.VISIBLE);
                break;
            case R.id.path_image:
                getMainActivity().showPathDialog();
                break;
            case R.id.back_image_btn:
                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                break;
            case R.id.scan_image:
                getMainActivity().scanQrcode();
                break;
            case R.id.screen_short_image:
                captureImage();
                break;
        }
    }

    @Override
    public void onFullScreen(PLVideoTextureView videoView, MediaController mediaController) {
        if (videoView == null) {
            return;
        }
        mCurVideoView = videoView;
        mPortraitMC = mediaController;
        mPlayConfigView.setVideoView(mCurVideoView);

        if (mFullScreenGroup.getVisibility() != View.VISIBLE) {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (view.getId()) {
            case R.id.play_config_view:
                return true;
            case R.id.media_controller:
                mGestureDetector.onTouchEvent(motionEvent);
                return false;
        }
        return false;
    }
}
