# PLDroidPlayer Release Notes for 2.3.0

本次更新:


## 版本

- 发布 pldroid-player-2.3.0.jar。
- 更新 libQPlayer.so。
- 更改版本号机制，末尾为0为正式版本，末尾不为0则为灰度版本。


## 缺陷

- 修复SurfaceView 配合硬解切后台回前台黑屏。
- 修复某些情况下，硬解时，数据没有完全给到解码器。
- 修复某些JNI对象没有释放。
- 修复jar和so版本不匹配时，sdk崩溃。
- 修复buffer end事件一直发送的问题。
- 修复java层空指针问题。

## 功能

- 新增option KEY_VIDEO_DISABLE 用于纯音频播放，因为某些mp3文件中带有video stream，避免进度不动。

- 增加stop方法，用于停止当前正在播放的视频。


