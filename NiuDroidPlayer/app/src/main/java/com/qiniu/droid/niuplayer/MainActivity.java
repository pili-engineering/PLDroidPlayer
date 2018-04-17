package com.qiniu.droid.niuplayer;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import com.bugsnag.android.Bugsnag;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.pili.pldroid.player.AVOptions;
import com.qiniu.droid.niuplayer.fragment.FragmentLifecycle;
import com.qiniu.droid.niuplayer.fragment.LiveVideoListFragment;
import com.qiniu.droid.niuplayer.fragment.MovieListFragment;
import com.qiniu.droid.niuplayer.fragment.ShortVideoListFragment;
import com.qiniu.droid.niuplayer.model.ModelFactory;
import com.qiniu.droid.niuplayer.model.UpgradeInfo;
import com.qiniu.droid.niuplayer.utils.PermissionChecker;
import com.qiniu.droid.niuplayer.widget.CommomDialog;
import com.qiniu.droid.niuplayer.widget.ScrollEnableViewPager;
import com.qiniu.droid.niuplayer.widget.UpgradeDialog;

import java.io.File;

import static com.qiniu.droid.niuplayer.ScanActivity.SCAN_URL_OK;
import static com.qiniu.droid.niuplayer.model.ModelFactory.createUpgradeInfoByURL;
import static com.qiniu.droid.niuplayer.utils.Config.DEFAULT_CACHE_DIR_NAME;
import static com.qiniu.droid.niuplayer.utils.Config.UPGRADE_URL_PREFIX;
import static com.qiniu.droid.niuplayer.utils.Utils.getAppProcessName;
import static com.qiniu.droid.niuplayer.utils.Utils.getAppVersionCode;
import static com.qiniu.droid.niuplayer.utils.Utils.wrapTabIndicatorToTitle;


public class MainActivity extends AppCompatActivity {
    private static String[] TAB_TITLE = {"短视频", "长视频", "直播"};

    private TabLayout mTabLayout;
    private FragmentViewPageAdapter mViewPageAdapter;
    private ScrollEnableViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        setContentView(com.qiniu.droid.niuplayer.R.layout.activity_main);
        Bugsnag.init(this);
        isStoragePermissionOK();
        initImageLoader();
        initView();
        checkUpgrade();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == IntentIntegrator.REQUEST_CODE) {

            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if (result != null) {
                if (result.getContents() == null) {
                    Toast.makeText(this, "无法识别二维码", Toast.LENGTH_LONG).show();
                } else {
                    jumpToVideoTextureActivity(result.getContents());
                }
            }
        } else if (resultCode == SCAN_URL_OK && requestCode == IntentIntegrator.REQUEST_CODE) {
            String path = data.getStringExtra("url");
            if (path != null && !path.isEmpty()) {
                jumpToVideoTextureActivity(path);
            }
        }
    }

    public void showPathDialog() {
        new CommomDialog(this, R.style.dialog, "添加播放地址", new CommomDialog.OnCloseListener() {
            @Override
            public void onClick(Dialog dialog, boolean confirm) {
                if (confirm) {
                    String path = ((CommomDialog) dialog).getEditString();
                    jumpToVideoTextureActivity(path);
                }
            }
        }).setTitle("添加播放地址").setPositiveButton("播放").show();
    }

    public void setTabViewVisible(boolean isVisible) {
        mTabLayout.setVisibility(isVisible ? View.VISIBLE : View.GONE);
        mViewPager.setScrollEnable(isVisible);
    }

    public boolean isCameraPermissionOK() {
        PermissionChecker checker = new PermissionChecker(this);
        boolean isPermissionOK = Build.VERSION.SDK_INT < Build.VERSION_CODES.M || checker.isCameraPermissionOK();
        if (!isPermissionOK) {
            Toast.makeText(this, "Camera permissions is necessary !!!", Toast.LENGTH_SHORT).show();
        }
        return isPermissionOK;
    }

    public boolean isStoragePermissionOK() {
        PermissionChecker checker = new PermissionChecker(this);
        boolean isPermissionOK = Build.VERSION.SDK_INT < Build.VERSION_CODES.M || checker.isStoragePermissionOK();
        if (!isPermissionOK) {
            Toast.makeText(this, "Storage permissions is necessary !!!", Toast.LENGTH_SHORT).show();
        }
        return isPermissionOK;
    }

    public void scanQrcode() {
        if (isCameraPermissionOK()) {
            IntentIntegrator integrator = new IntentIntegrator(this);
            integrator.setCaptureActivity(ScanActivity.class);
            integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
            integrator.setOrientationLocked(true);
            integrator.setCameraId(0);
            integrator.setBeepEnabled(true);
            integrator.initiateScan();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mViewPageAdapter != null && mViewPageAdapter.getCurrentFragment() != null) {
            mViewPageAdapter.getCurrentFragment().onActivityResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mViewPageAdapter != null && mViewPageAdapter.getCurrentFragment() != null) {
            mViewPageAdapter.getCurrentFragment().onActivityPause();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mViewPageAdapter != null && mViewPageAdapter.getCurrentFragment() != null) {
            mViewPageAdapter.getCurrentFragment().onActivityDestroy();
        }
    }

    @Override
    public void onBackPressed() {
        if (mViewPageAdapter != null) {
            mViewPageAdapter.getCurrentFragment().onBackPressed();
        } else {
            super.onBackPressed();
        }
    }

    private void initImageLoader() {
        File cacheDir = StorageUtils.getOwnCacheDirectory(this, DEFAULT_CACHE_DIR_NAME);//sdcard目录
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .diskCache(new UnlimitedDiskCache(cacheDir)) // default 可以自定义缓存路径
                .diskCacheSize(50 * 1024 * 1024)
                .diskCacheFileCount(100)
                .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
                .memoryCacheSize(2 * 1024 * 1024)
                .memoryCacheSizePercentage(13)
                .build();
        ImageLoader.getInstance().init(config);
    }

    private void initView() {
        mViewPager = findViewById(com.qiniu.droid.niuplayer.R.id.view_pager);
        mViewPager.setOffscreenPageLimit(2);
        mViewPageAdapter = new FragmentViewPageAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mViewPageAdapter);

        mTabLayout = findViewById(com.qiniu.droid.niuplayer.R.id.tab_layout);
        mTabLayout.setupWithViewPager(mViewPager);

        wrapTabIndicatorToTitle(mTabLayout, 70, 70);
        mViewPager.setCurrentItem(0);
    }

    private void jumpToVideoTextureActivity(String path) {
        Intent intent = new Intent(this, PLVideoTextureActivity.class);
        intent.putExtra("videoPath", path);
        intent.putExtra("mediaCodec", AVOptions.MEDIA_CODEC_AUTO);
        intent.putExtra("liveStreaming", 1);
        this.startActivity(intent);
    }

    private void checkUpgrade() {
        String pageName = getAppProcessName(this);
        String url = UPGRADE_URL_PREFIX + pageName;
        createUpgradeInfoByURL(url, new ModelFactory.OnResultListener() {
            @Override
            public void onSuccess(int statusCode, Object data) {
                final UpgradeInfo upgradeInfo = (UpgradeInfo) data;
                String appVersion = getAppVersionCode(MainActivity.this) + "";
                if (upgradeInfo.getVersion().compareTo(appVersion) > 0) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            UpgradeDialog.show(MainActivity.this, upgradeInfo.getDescription(), upgradeInfo.getDownloadURL());
                        }
                    });
                }
            }

            @Override
            public void onFailure() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "更新失败", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    private class FragmentViewPageAdapter extends FragmentStatePagerAdapter {
        private FragmentLifecycle mCurrentFragment;

        public FragmentViewPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TAB_TITLE[position];
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new ShortVideoListFragment();
                case 1:
                    return new MovieListFragment();
                default:
                    return new LiveVideoListFragment();
            }
        }

        @Override
        public int getCount() {
            return TAB_TITLE.length;
        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            if (getCurrentFragment() != object) {
                if (mCurrentFragment != null) {
                    mCurrentFragment.onFragmentPause();
                }
                mCurrentFragment = ((FragmentLifecycle) object);
                mCurrentFragment.onFragmentResume();
            }
            super.setPrimaryItem(container, position, object);
        }

        public FragmentLifecycle getCurrentFragment() {
            return mCurrentFragment;
        }
    }
}
