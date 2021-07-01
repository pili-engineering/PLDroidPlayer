package com.qiniu.droid.niuplayer.utils;

import android.os.Environment;

public class Config {
    public static final String SDCARD_DIR = Environment.getExternalStorageDirectory().getAbsolutePath();
    public static final String DEFAULT_CACHE_DIR_NAME = "PLDroidPlayer";
    public static final String DEFAULT_CACHE_DIR_PATH = SDCARD_DIR + "/PLDroidPlayer";
    public static final String VIDEO_PATH_PREFIX = "http://demo-videos.qnsdk.com/";
    public static final String COVER_PATH_PREFIX = "http://demo-videos.qnsdk.com/snapshoot/";
    public static final String COVER_PATH_SUFFIX = ".jpg";
    public static final String MOVIE_PATH_PREFIX = "https://api-demo.qnsdk.com/v1/kodo/bucket/demo-videos?prefix=movies";
    public static final String SHORT_VIDEO_PATH_PREFIX = "https://api-demo.qnsdk.com/v1/kodo/bucket/demo-videos?prefix=shortvideo";
    public static final String LIVE_TEST_URL = "rtmp://live.hkstv.hk.lxdns.com/live/hks";
    public static final String UPGRADE_URL_PREFIX = "https://api-demo.qnsdk.com/v1/upgrade/app?appId=";

}
