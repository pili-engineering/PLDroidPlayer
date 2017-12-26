# PLDroidPlayer Release Notes for 2.0.6

本次更新:

## 说明

- 从 v2.0.0 版本开始，全面升级为七牛自研的播放器内核，拥有更优异的性能

## 版本

- 发布 pldroid-player-2.0.6.jar

## 缺陷

- 修复了一些场合下 MP4 离线缓存失效的问题
- 修复了部分设备在移动 4G 网络下偶现的崩溃问题


## 更新注意事项

从 2.0.5 开始，`PLNetworkManager` 类被标记为 deprecated，无需引入 happydns 依赖，也无须调用 `PLNetworkManager.startDnsCacheService`，SDK 会根据 `AVOptions` 中的设置自动处理 DNS 缓存。

由于 Android 7.0 使用 BoringSSL 替换了 OpenSSL，一些依赖系统内建 OpenSSL 的程序在一些 7.0+ 的 ROM 里可能会崩溃。如果您的应用 targetSdkVersion >= 24，那么强烈推荐将 libqcOpenSSL.so 加入至 jniLibs 目录。

如果从旧版本升级，建议参考 v2.0.0 版本及后续的 [ReleaseNote](https://github.com/pili-engineering/PLDroidPlayer/blob/master/ReleaseNotes/release-notes-2.0.0.md) ，查看注意事项
