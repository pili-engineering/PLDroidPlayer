package com.qiniu.droid.niuplayer.fragment;


public interface FragmentLifecycle {
    void onFragmentPause();
    void onFragmentResume();
    void onBackPressed();
    void onActivityPause();
    void onActivityResume();
    void onActivityDestroy();
}
