package com.pili.pldroid.playerdemo.common;

import android.net.Uri;

/**
 * Created by jerikc on 15/5/30.
 */
public class Util {
    public static boolean isUrlLocalFile(String path) {
        return getPathScheme(path) == null || "file//".equals(getPathScheme(path));
    }

    public static String getPathScheme(String path) {
        return Uri.parse(path).getScheme();
    }
}
