# PLDroidPlayer 升级指南

从 v2.0.0 版本开始，PLdroidPlayer 不再依赖 ijkmediaplayer，全面升级为七牛自研的播放器内核，拥有更优异的性能。

## 更改说明

具体接口使用方式详见[官网文档](https://developer.qiniu.com/pili/sdk/1210/the-android-client-sdk)

### 新增功能

- 减少包体大小
- 提高首开速度
- 自动重连
- 倍数播放
- 支持多码率 HLS
- 支持七牛加密 HLS
- MP4 本地缓存
- 播放加密视频
- H.265 软解
- 解码数据回调（YUV/PCM/SEI）
- 大幅减少包体体积
- 大幅优化首开速度
- 变速不变调，平滑追帧
- 视频截图
- 自定义播放区域

### 依赖库

- 原有的 pldroidplayer.so 不再需要，可以删除
- 新增 libqplayer.so，该库为必须依赖
- 新增 libqccodec.so，该库为软解的 codec，若不使用软解，可以删除
- 新增 libqcopenssl.so，若 targetSdkVersion < 24 或不需要播放 https 地址的流，可以删除

### 混淆配置

需要在 `proguard-rules` 中新增

```
  -keep class com.pili.pldroid.player.** { *; }
  -keep class com.qiniu.qplayer.mediaEngine.MediaPlayer{*;}
```

### 注意事项

- SDK 内部自动处理重连，不再需要外部进行重连
- SDK 所有公开方法的参数名不再混淆，可以直接通过 IDE 查看
- 所有 Listener 类与相关常量从 `PLMediaPlayer` 下调整为单独的接口，并添加 PL 前缀
- 所有 Listener 类的回调方法中的 `PLMediaPlayer` 参数均被移除
- `AVOptions` 与所有 Listener 类的内容均有了较多改变，详见[官网文档](https://developer.qiniu.com/pili/sdk/1210/the-android-client-sdk)

### 新增接口

#### `PLMediaPlayer` / `PLVideoView` / `PLVideoTextureView`

- `captureImage()`: 视频截图
- `setVideoArea()`: 区域播放
- `setVolume()`: 音量扩增
- `setPlaySpeed()`： 倍速播放

#### `PLVideoView`

- `setZOrderOnTop`:同系统 `SurfaceView` 的 `setZOrderOnTop`
- `setZOrderMediaOverlay`: 同系统 `SurfaceView` 的 `setZOrderMediaOverlay`

#### `PLVideoTextureView`

- `PLVideoTextureView.setSplitMode()`: 启用分屏模式
- `PLVideoTextureView.disableSplitMode()`: 禁用分屏模式

### 删除接口

- `PLNetworkManager`

