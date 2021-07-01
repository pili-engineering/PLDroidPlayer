package com.qiniu.droid.niuplayer.model;


public class UpgradeInfo {
    private String mAppID;
    private String mVersion;
    private String mDescription;
    private String mDownloadURL;
    private String mCreateTime;

    public void setAppID(String appID) {
        this.mAppID = appID;
    }

    public void setVersion(String version) {
        this.mVersion = version;
    }

    public void setDescription(String description) {
        this.mDescription = description;
    }

    public void setDownloadURL(String downloadURL) {
        this.mDownloadURL = downloadURL;
    }

    public void setCreateTime(String createTime) {
        this.mCreateTime = createTime;
    }

    public String getAppID() {
        return mAppID;
    }

    public String getVersion() {
        return mVersion;
    }

    public String getDescription() {
        return mDescription;
    }

    public String getDownloadURL() {
        return mDownloadURL;
    }

    public String getCreateTime() {
        return mCreateTime;
    }
}
