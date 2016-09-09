# PLDroidPlayer Release Notes for 1.2.0

## 简介
PLDroidPlayer 为 Android 开发者提供直播播放 SDK

## 记录

### 播放器
- 发布 pldroid-player-1.2.0.jar
- 删除了 ijkmediaplayer.jar
- 实现播放器秒开优化，网络条件好的情况下，可以实现秒开
- 新增播放器核心类 `PLMediaPlayer`
- 新增` PLVideoView` 控件
- 新增`PLVideoTextureView` 控件
- 支持多种画面预览模式，包括：原始尺寸、适应屏幕、全屏铺满、16:9、4:3 等
- 支持画面旋转（0度，90度，180度，270度）
- 更新 Demo 程序，演示所有新增的接口类

### 播放器 Demo

- 优化 Demo 程序的框架
- 新增 `PLMediaPlayerActivity`，用于演示 `PLMediaPlayer` 接口
- 新增 `PLVideoViewActivity`，用于演示 `PLVideoView` 接口
- 新增 `PLVideoTextureViewActivity`，用于演示 `PLVideoTextureView` 接口
- 新增 `PLAudioPlayerActivity`，用于演示使用 `PLMediaPlayer` 接口完成纯音频播放

