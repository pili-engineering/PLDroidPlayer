# PLDroidPlayer

PLDroidPlayer 是一个适用于 Android 平台的音视频播放器 SDK，可高度定制化和二次开发，为 Android 开发者提供了简单、快捷的接口，帮助开发者在 Android 平台上快速开发播放器应用。

## 特性		 

- [x] 基于 [ijkplayer](https://github.com/Bilibili/ijkplayer) ( based on [ffplay](http://ffmpeg.org/) )		
- [x] Android Min API 9		
- [x] 支持 RTMP 和 HLS 协议的直播流媒体播放
- [x] 支持常见的音视频文件播放（MP4、M4A、flv 等）
- [x] 支持 MediaCodec 硬件解码
- [x] 提供播放器核心类 `PLMediaPlayer`
- [x] 提供 `PLVideoView` 控件
- [x] 提供 `PLVideoTextureView` 控件
- [x] 支持多种画面预览模式
- [x] 支持画面旋转（0度，90度，180度，270度）
- [x] 支持纯音频播放，并支持后台运行
- [x] 可高度定制化的 `MediaController`		
- [x] 支持 ARM, ARMv7a, ARM64v8a, X86 主流芯片体系架构
- [x] 支持后台播放

## 播放器对比
| -  | ijkplayer | PLDroidPlayer |
|---|---|---|
|Shared Library Size|较大|较小|
|Shared Library Count|5个|1个|
|Widget|有商业授权风险|无商业授权风险|
|MediaController|难以定制化|容易定制化|

## SDK 最低要求

Android 2.3 (API 9) 及其以上

## 使用方法
请参考 wiki 文档：[PLDroidPlayer 开发指南](https://github.com/pili-engineering/PLDroidPlayer/wiki)

## 依赖库
* ffmpeg
* libyuv
* sdl
* libVLC

## 说明
PLDroidPlayer 目前基于 [ijkplayer](https://github.com/Bilibili/ijkplayer) , 感谢 [ijkplayer](https://github.com/Bilibili/ijkplayer) ，相应的修改详见：https://github.com/pili-engineering/ijkplayer

## 反馈及意见

当你遇到任何问题时，可以通过在 GitHub 的 repo 提交 issues 来反馈问题，请尽可能的描述清楚遇到的问题，如果有错误信息也一同附带，并且在 Labels 中指明类型为 bug 或者其他。

[通过这里查看已有的 issues 和提交 Bug](https://github.com/pili-engineering/PLDroidPlayer/issues)。
