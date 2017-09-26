# PLDroidPlayer Release Notes for 2.0.2

本次更新:

## 说明

- 从 v2.0.0 版本开始，全面升级为七牛自研的播放器内核，拥有更优异的性能

## 版本

- 发布 pldroid-player-2.0.2.jar
- 发布 libopenssl.so
- 更新 libQPlayer.so 

## 缺陷

- 修复了 `setVolume` 导致静音的问题
- 修复了频繁切换播放地址时可能 crash 的问题
- 修复了销毁播放器时可能 crash 的问题
- 修复了播放 m3u8 视频流断网时 `IO_ERROR` 回调过于频繁的问题
- 修复了 `AVOptions` 为空时可能 crash 的问题
- 修复了部分 mp3 音频播放时可能 crash 的问题
- 修复了播放过程中再次 `setSurface` 时可能 crash 的问题
- 修复了 7.0 以上系统部分机型缺失 `openssl` 的问题
- 修复了 `setDisplayAspectRatio` 参数为 `ASPECT_RATION_ORIGIN` 时画面不渲染的问题
- 修复了视频播放完毕后 `MediaController` 中时间戳继续增长的问题
- 修复了播放一些 m3u8 流时画面不连续的问题
- 修复了一些场景下设置 HTTP 头不生效的问题
- 修复了播放离线缓存内容时提前回调 `onCompletion` 的问题
- 修复了播放百度直播云视频流的兼容性问题

## 更新注意事项

由于 Android 7.0 使用 BoringSSL 替换了 OpenSSL，一些依赖系统内建 OpenSSL 的程序在一些 7.0+ 的 ROM 里可能会崩溃。如果您的应用 targetSdkVersion >= 24，那么强烈推荐将 libopenssl.so 加入至 jniLibs 目录。

如果从旧版本升级，建议参考 v2.0.0 版本及后续的 [ReleaseNote](https://github.com/pili-engineering/PLDroidPlayer/blob/master/ReleaseNotes/release-notes-2.0.0.md) ，查看注意事项
