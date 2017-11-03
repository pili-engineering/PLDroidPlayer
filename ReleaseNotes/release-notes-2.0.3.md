# PLDroidPlayer Release Notes for 2.0.3

本次更新:

## 说明

- 从 v2.0.0 版本开始，全面升级为七牛自研的播放器内核，拥有更优异的性能

## 版本

- 发布 pldroid-player-2.0.3.jar
- 更新 libopenssl.so，更名为 libqcopenssl.so
- 更新 libQPlayer.so 

## 功能

- 新增 `PLVideoTextureView.setSplitMode` 方法
- 新增 `AVOptions.KEY_LIVE_STREAMING` 字段
- 新增 `PlayerState.PLAYING_CACHE` 字段
- 新增 `ERROR_CODE_PLAYER_DESTROYED` 错误码
- 新增 `ERROR_CODE_PLAYER_VERSION_NOT_MATCH` 错误码
- 新增 `PLNetworkManager.setDnsManager` 方法

## 缺陷

- 彻底修复一些场景下切换播放地址时的 crash 问题
- 彻底修复一些场景下销毁播放器时的 crash 问题
- 修复了 libopenssl 版本过低引发的问题
- 修复了 `PLVideoTextureView` 播放直播流时暂停后可能不会追帧的问题
- 修复了一些场景下画面不刷新的问题
- 修复了部分纯视频 mp4 无法循环播放的问题
- 修复了切换播放地址后，之前的 `setVolume` 没有继续生效的问题
- 修复了部分 hls 流拖动时卡住的问题
- 修复了部分 mp4 流循环播放时可能出现多次 COMPLETE 消息的问题
- 修复了 `OnBufferingUpdate` 回调在一些情况下不会出现 100% 或大于 100% 的问题
- 修复了断网后部分视频进度异常的问题
- 修复了部分视频流与音频流的兼容性问题
- 修复了 `PLVideoView` 与 `PLVideoTextureView` 被 `remove` 然后重新 `add` 后黑屏的问题

## 更新注意事项

由于 Android 7.0 使用 BoringSSL 替换了 OpenSSL，一些依赖系统内建 OpenSSL 的程序在一些 7.0+ 的 ROM 里可能会崩溃。如果您的应用 targetSdkVersion >= 24，那么强烈推荐将 libopenssl.so 加入至 jniLibs 目录。

如果从旧版本升级，建议参考 v2.0.0 版本及后续的 [ReleaseNote](https://github.com/pili-engineering/PLDroidPlayer/blob/master/ReleaseNotes/release-notes-2.0.0.md) ，查看注意事项
