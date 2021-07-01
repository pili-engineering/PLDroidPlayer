package com.qiniu.droid.niuplayer.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.design.widget.TabLayout;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.pili.pldroid.player.AVOptions;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class Utils {
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public static String getAppProcessName(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> infos = manager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo info : infos) {
            if (info.pid == pid)
                return info.processName;
        }
        return "";
    }

    public static int getAppVersionCode(Context context) {
        int versioncode = 0;
        try {
            // ---get the package info---
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versioncode = pi.versionCode;
        } catch (Exception e) {
            Log.e("VersionInfo", "Exception", e);
        }
        return versioncode;
    }

    public static void wrapTabIndicatorToTitle(TabLayout tabLayout, int externalMargin, int internalMargin) {
        View tabStrip = tabLayout.getChildAt(0);
        if (tabStrip instanceof ViewGroup) {
            ViewGroup tabStripGroup = (ViewGroup) tabStrip;
            int childCount = ((ViewGroup) tabStrip).getChildCount();
            for (int i = 0; i < childCount; i++) {
                View tabView = tabStripGroup.getChildAt(i);
                //set minimum width to 0 for instead for small texts, indicator is not wrapped as expected
                tabView.setMinimumWidth(0);
                // set padding to 0 for wrapping indicator as title
                tabView.setPadding(0, tabView.getPaddingTop(), 0, tabView.getPaddingBottom());
                // setting custom margin between tabs
                if (tabView.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
                    ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) tabView.getLayoutParams();
                    if (i == 0) {
                        // left
                        setTabMargin(layoutParams, externalMargin, internalMargin);
                    } else if (i == childCount - 1) {
                        // right
                        setTabMargin(layoutParams, internalMargin, externalMargin);
                    } else {
                        // internal
                        setTabMargin(layoutParams, internalMargin, internalMargin);
                    }
                }
            }

            tabLayout.requestLayout();
        }
    }

    private static void setTabMargin(ViewGroup.MarginLayoutParams layoutParams, int start, int end) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            layoutParams.setMarginStart(start);
            layoutParams.setMarginEnd(end);
        } else {
            layoutParams.leftMargin = start;
            layoutParams.rightMargin = end;
        }
    }

    public static boolean savePhotoToSDCard(Bitmap photoBitmap, String path, String photoName) {
        boolean ret = false;
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File photoFile = new File(path, photoName + ".jpg");
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(photoFile);
            if (photoBitmap != null) {
                if (photoBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)) {
                    fileOutputStream.flush();
                    ret = true;
                }
            }
        } catch (FileNotFoundException e) {
            photoFile.delete();
            e.printStackTrace();
        } catch (IOException e) {
            photoFile.delete();
            e.printStackTrace();
        } finally {
            try {
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return ret;
    }

    public static AVOptions createAVOptions() {
        AVOptions options = new AVOptions();
        // the unit of timeout is ms
        options.setInteger(AVOptions.KEY_PREPARE_TIMEOUT, 10 * 1000);
        options.setInteger(AVOptions.KEY_LIVE_STREAMING, 0);
//        options.setString(AVOptions.KEY_COMP_DRM_KEY, "test123");
        // 1 -> hw codec enable, 0 -> disable [recommended]
        options.setInteger(AVOptions.KEY_MEDIACODEC, AVOptions.MEDIA_CODEC_SW_DECODE);
        options.setInteger(AVOptions.KEY_PREFER_FORMAT, AVOptions.PREFER_FORMAT_MP4);
        boolean disableLog = false;
        options.setInteger(AVOptions.KEY_LOG_LEVEL, disableLog ? 5 : 0);
        return options;
    }
}
