# PLDroidPlayer Release Notes for 2.0.4

本次更新:

## 说明

- 从 v2.0.0 版本开始，全面升级为七牛自研的播放器内核，拥有更优异的性能

## 版本

- 发布 pldroid-player-2.0.4.jar
- 更新 libQPlayer.so 

## 功能

- 新增 `AVOptions.KEY_LOG_LEVEL` 字段
- 新增 `PLMediaPlayer.MEDIA_INFO_LOOP_DONE` 字段
- 新增 `PLVideoTextureView.disableSplitMode` 方法
- 新增 `getRtmpVideoTimestamp` 与 `getRtmpAudioTimestamp` 方法

## 缺陷

- 修复了一些情况下开启 `PLNetowrkManager.startDnsCacheService` 后无法播放的问题
- 修复了一些设备上无法加载 `libqcCodec.so` 的问题
- 修复了播放一些 flv 直播流时卡顿的问题
- 修复了一些情况下循环播放时卡顿的问题
- 修复了一些情况下 `PLVideoTextureView` 画面不刷新的问题
- 修复了一些情况下不会收到 `OnSeekComplete` 的问题
- 修复了一些情况下出现 `ERROR_CODE_SEEK_FAILED` 的问题
- 修复了一些情况下开启 `PLVideoTextureView.setSplitMode` 后 view 尺寸错误的问题
- 修复了一些情况下频繁 `seekTo` 可能 crash 的问题
- 修复了播放地址过长时离线缓存失败的问题
- 修复了播放完成后时间可能继续增长的问题
- 修复了 `setDebuggingLogEnabled` 为 `false` 时仍会产生大量 log 的问题

## 更新注意事项

由于 Android 7.0 使用 BoringSSL 替换了 OpenSSL，一些依赖系统内建 OpenSSL 的程序在一些 7.0+ 的 ROM 里可能会崩溃。如果您的应用 targetSdkVersion >= 24，那么强烈推荐将 libqcOpenSSL.so 加入至 jniLibs 目录。

如果从旧版本升级，建议参考 v2.0.0 版本及后续的 [ReleaseNote](https://github.com/pili-engineering/PLDroidPlayer/blob/master/ReleaseNotes/release-notes-2.0.0.md) ，查看注意事项
