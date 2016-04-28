# PLDroidPlayer Release Notes for 1.2.0

## 简介
PLDroidPlayer 为 Android 开发者提供直播播放 SDK

## 记录

### 播放器
  - 发布 pldroid-player-1.2.0.jar
  - 删除了 ijkmediaplayer.jar
  - 实现播放器秒开优化，网络条件好的情况下，可以实现秒开
  - 新增接口类：`PLMediaPlayer`，`PLVideoView`，`PLVideoTextureView`
  - `PLMediaPlayer` 类提供了播放器的核心功能，播放器界面全部由开发者定制
  - 相比于 `VideoView` 类，`PLVideoView` 和 `PLVideoTextureView` 新增了 `setDisplayAspectRatio` 接口，用于配置画面预览模式，包括：原始视频尺寸、适应屏幕、铺满全屏、16:9、4:3 多种预览方式
  - `PLVideoTextureView` 提供 `setDisplayOrientation` 接口，用于配置画面旋转，支持的旋转角度：0度，90度，180度，270度
  - 更新 Demo 程序，演示所有新增的接口类

### 播放器 Demo

- 优化 Demo 程序的框架
- 新增 `PLMediaPlayerActivity`，用于演示 `PLMediaPlayer` 接口
- 新增 `PLVideoViewActivity`，用于演示 `PLVideoView` 接口
- 新增 `PLVideoTextureViewActivity`，用于演示 `PLVideoTextureView` 接口
- 新增 `PLAudioPlayerActivity`，用于演示使用 `PLMediaPlayer` 接口完成纯音频播放

