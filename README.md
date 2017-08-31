# PLDroidPlayer

PLDroidPlayer 是一个适用于 Android 平台的音视频播放器 SDK，可高度定制化和二次开发，为 Android 开发者提供了简单、快捷的接口，帮助开发者在 Android 平台上快速开发播放器应用。

## 特性		 

- [x] Android Min API 9
- [x] 支持 RTMP 和 HLS 协议的直播流媒体播放
- [x] 支持常见的音视频文件播放（MP4、mp3、flv 等）
- [x] 支持 MediaCodec 硬件解码
- [x] 提供播放器核心类 `PLMediaPlayer`
- [x] 提供 `PLVideoView` 控件
- [x] 提供 `PLVideoTextureView` 控件
- [x] 支持多种画面预览模式
- [x] 支持画面旋转（0度，90度，180度，270度）
- [x] 支持画面镜像变换
- [x] 支持播放器音量设置，可实现静音功能
- [x] 支持纯音频播放
- [x] 支持后台播放
- [x] 支持首屏秒开
- [x] 支持直播累积延时优化
- [x] 支持带 IP 地址的播放 URL
- [x] 支持设置封面
- [x] 支持软硬解自动切换
- [x] 支持 HTTPS 协议
- [x] 支持自动重连
- [x] 支持 H.265 播放
- [x] 支持七牛私有 DRM
- [x] 支持边下边播
- [x] 支持 mp4 本地缓存功能
- [x] 支持音视频数据回调
- [x] 支持自定义音视频渲染
- [x] 可高度定制化的 `MediaController`
- [x] 支持 ARM, ARMv7a, ARM64v8a, X86 主流芯片体系架构

## 说明

从 **v2.0.0** 开始，SDK 全面升级为七牛完全自研的播放器内核，拥有更加优异的性能，升级内容如下：

- [x] 新增倍数播放功能（0.5x，1x，2x，4x 等）
- [x] 新增 mp4 本地缓存功能
- [x] 新增音视频解码数据回调
- [x] 新增自定义音视频播放和渲染
- [x] 新增 HLS 七牛私有 DRM 的支持 
- [x] 新增 H.265 格式播放的支持
- [x] 优化 CPU、内存和功耗
- [x] 优化首开效果，首开速度有大幅提升
- [x] 优化包体大小，jar 包和 so 体积均有显著减小
- [x] 优化重连逻辑，不用销毁播放器，网络断开后内部自动重连
- [x] 优化 mp4 点播，使用双 IO 技术更高效地播放 moov 在尾部的 mp4 文件
- [x] 支持播放过程中变速不变调，可实现更平滑的追帧效果，更少的卡顿率

如果从旧版本升级，建议参考 v2.0.0 版本及后续的 [ReleaseNote](https://github.com/pili-engineering/PLDroidPlayer/blob/master/ReleaseNotes/release-notes-2.0.0.md) ，查看注意事项

## 使用方法
请参考开发者中心文档：[PLDroidPlayer 开发指南](https://developer.qiniu.com/pili/sdk/1210/the-android-client-sdk)

## 反馈及意见

当你遇到任何问题时，可以通过在 GitHub 的 repo 提交 issues 来反馈问题，请尽可能的描述清楚遇到的问题，如果有错误信息也一同附带，并且在 Labels 中指明类型为 bug 或者其他。

[通过这里查看已有的 issues 和提交 Bug](https://github.com/pili-engineering/PLDroidPlayer/issues)
