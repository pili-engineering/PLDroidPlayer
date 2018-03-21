# PLDroidPlayer

PLDroidPlayer 是一个适用于 Android 平台的音视频播放器 SDK，可高度定制化和二次开发，为 Android 开发者提供了简单、快捷的接口，帮助开发者在 Android 平台上快速开发播放器应用。

## 特性    

| 功能               | 描述                                                   | 版本   |
|--------------------|--------------------------------------------------------|--------|
| PLMediaPlayer      | 类似 Android MediaPlayer，提供了播放器的核心功能       | 1.2.0+ |
| PLVideoView        | 类似 Android VideoView，基于 SurfaceView 的播放控件    | 1.2.0+ |
| PLVideoTextureView | 类似 Android VideoView，基于 TextureView 的播放控件    | 1.2.0+ |
| 全架构支持         | 包括 arm64-v8a, armeabi-v7a, armeabi 与 x86            | 1.2.0+ |
| 画面镜像与旋转     | 由 PLVideoTextureView 提供，支持播放画面镜像与旋转     | 1.2.2+ |
| 后台播放           | 退到后台只播放音频                                     | 1.2.3+ |
| IP 地址播放        | URL 格式：protocol://ip/path?domain=xxxx.com           | 1.3.0+ |
| 设置播放封面       | 在播放开始前显示封面 view                              | 1.4.0+ |
| 软硬解自动切换     | 优先硬解，硬解失败自动切换到软解                       | 1.4.1+ |
| 自动直播延迟优化   | 播放直播流时可以通过自动变速播放来优化延迟             | 2.0.0+ |
| H.265 软解         | 软解播放 H.265 视频流                                  | 2.0.0+ |
| 变速播放           | 支持设置播放速度                                       | 2.0.0+ |
| MP4 离线缓存       | 支持播放过程中缓存 MP4 文件到本地                      | 2.0.0+ |
| 解码数据回调       | 回调解码后的音视频数据，可以外部渲染                   | 2.0.0+ |
| 七牛私有加密播放   | 支持播放七牛私有加密视频                               | 2.0.0+ |
| 分屏播放           | 由 PLVideoTextureView 提供，支持两个 view 并排分屏播放 | 2.0.3+ |
| 自定义 DNS 服务器  | 支持自定义 DNS 服务器与设置预解析域名                  | 2.0.5+ |
| 视频截图           | 支持视频截图                                           | 2.1.0+ |
| 区域播放           | 支持播放视频画面的部分区域                             | 2.1.0+ |
| 音量增强           | 支持将播放音量增强到大于原始音量                       | 2.1.0+ |
| 快开模式           | 极大加快相同协议与格式的视频流的打开速度               | 2.1.0+ |

## 使用方法
请参考开发者中心文档：[PLDroidPlayer 开发指南](https://developer.qiniu.com/pili/sdk/1210/the-android-client-sdk)

## 升级指南

从 2.0.0 开始，SDK 全面升级为七牛完全自研的播放器内核，拥有更加优异的性能与更小的包体。推荐所有老版本用户参考文档进行升级：[升级指南](https://github.com/pili-engineering/PLDroidPlayer/blob/master/v2_upgrade_manual.md)

## 反馈及意见

当你遇到任何问题时，可以通过在 GitHub 提交 issue 来反馈问题，请尽可能的描述清楚遇到的问题以及相应的流地址（若为本地视频，推荐上传至云存储并提供链接），推荐提供相应的日志。