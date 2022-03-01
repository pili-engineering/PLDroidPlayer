# PLDroidPlayer Release Notes for 2.4.0

本次更新:


## 版本

- 发布 pldroid-player-2.4.0.jar。
- 更新 libQPlayer.so。


## 缺陷

- 修复硬解情况下 切换URL ，OnVideoSizeChangedListener不回调 。
- 修复硬解情况下 切换URL ，PLOnInfoListener.MEDIA_INFO_VIDEO_RENDERING_START事件不回调 。
- 修复SEI消息不回调。
- 修复直播时，推的视频流分辨率改变导致崩溃/花屏。
- 修复seek失败后，PLOnErrorListener.ERROR_CODE_SEEK_FAILED不回调。
- 修复mp4有旋转的metadata 某些情况下会崩溃。
