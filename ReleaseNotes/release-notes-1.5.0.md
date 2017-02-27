# PLDroidPlayer Release Notes for 1.5.0

本次更新:

## 版本

  - 发布 pldroid-player-1.5.0.jar
  - 更新 libpldroidplayer.so

## 功能

  - 新增获取 GOP Time 的消息回调

```
    public static final int MEDIA_INFO_VIDEO_GOP_TIME = 10003;
```

  - 新增获取帧率、码率接口

```
    public int getVideoFps();

    public long getVideoBitrate();
``` 

## 缺陷 

  - 修复使用 PLVideoTextureView 硬解从后台切换前台花屏的问题
  - 修复了播放过程中切换前后台出现的频繁缓冲问题，防止弱网下频繁缓冲导致 Loading 动画闪烁 

## 更新注意事项

  - **需要在 build.gradle 中删除如下配置:**

```
    compile 'com.qiniu.pili:pili-android-qos:0.8.+'
```

  - 修改 onVideoSizeChanged 的接口:

```
    void onVideoSizeChanged(PLMediaPlayer mp, int width, int height, int videoSarNum, int videoSarDen);
```
