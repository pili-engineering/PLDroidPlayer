# PLDroidPlayer Release Notes for 1.5.1

本次更新:

## 版本

  - 发布 pldroid-player-1.5.1.jar

## 缺陷 

  - 修复部分机型网络切换时产生的崩溃问题
  - 修复码流分辨率改变导致的硬解花屏问题

## 更新注意事项

  - **需要在 build.gradle 中删除如下配置:**

```
    compile 'com.qiniu.pili:pili-android-qos:0.8.+'
```

  - 修改 onVideoSizeChanged 的接口:

```
    void onVideoSizeChanged(PLMediaPlayer mp, int width, int height, int videoSarNum, int videoSarDen);
```
