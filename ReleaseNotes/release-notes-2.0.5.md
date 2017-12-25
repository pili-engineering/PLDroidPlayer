# PLDroidPlayer Release Notes for 2.0.5

本次更新:

## 说明

- 从 v2.0.0 版本开始，全面升级为七牛自研的播放器内核，拥有更优异的性能

## 版本

- 发布 pldroid-player-2.0.5.jar
- 更新 libQPlayer.so 

## 新增

- 新增 `AVOptions.KEY_DNS_SERVER` 字段
- 新增 `AVOptions.KEY_DOMAIN_LIST` 字段
- 新增视频 header 中 rotate 字段的支持，SDK 会自动旋转画面

## 缺陷

- 修复了部分场景下视频 open failed 的问题
- 修复了部分视频音画不同步的问题
- 修复了部分视频开播时闪屏的问题
- 修复了部分视频循环播放时进度异常的问题
- 修复了部分 mp3 流打开失败的问题
- 修复了部分场合下 `OnCompletionListener` 不触发的问题
- 修复了部分场合下销毁播放器时 crash 的问题
- 修复了部分场合下快速切换播放地址时 crash 的问题
- 修复了连续调用 `stopPlayback` 时可能 crash 的问题
- 修复了动态添加的 `PLVideoTextureView` 的 `setDisplayOrientation` 不生效的问题


## 更新注意事项

从 2.0.5 开始，`PLNetworkManager` 类被标记为 deprecated，无需引入 happydns 依赖，也无须调用 `PLNetworkManager.startDnsCacheService`，SDK 会根据 `AVOptions` 中的设置自动处理 DNS 缓存。

由于 Android 7.0 使用 BoringSSL 替换了 OpenSSL，一些依赖系统内建 OpenSSL 的程序在一些 7.0+ 的 ROM 里可能会崩溃。如果您的应用 targetSdkVersion >= 24，那么强烈推荐将 libqcOpenSSL.so 加入至 jniLibs 目录。

如果从旧版本升级，建议参考 v2.0.0 版本及后续的 [ReleaseNote](https://github.com/pili-engineering/PLDroidPlayer/blob/master/ReleaseNotes/release-notes-2.0.0.md) ，查看注意事项
