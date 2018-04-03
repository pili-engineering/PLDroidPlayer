# PLDroidPlayer Release Notes for 2.1.2

本次更新:

## 说明

- 从 v2.0.0 版本开始，全面升级为七牛自研的播放器内核，拥有更优异的性能

## 版本

- 发布 pldroid-player-2.1.2.jar
- 更新 libQPlayer.so
- 更新 libqcCodec.so

## 优化

- 极大优化了 mp4 长视频的打开速度

## 新增

- 新增 `setBufferingEnabled` 方法
- 新增 `getHttpBufferSize` 方法

## 缺陷

- 修复了特殊情况下的 ANR 问题
- 修复了一些情况下的空指针问题
- 修复了在 `OnCompletionListener` 中调用 `start` 方法不生效的问题
- 修复了一些情况下视频封面不刷新的问题
- 修复了音频采样率较低时音画不同步的问题

## 移除

- 移除 `PLMediaPlayer.getMediaCodec` 方法，可以在业务层记录设置的是硬解还是软解
- 移除 `PLMediaPlayer.reset` 方法，请用 `release` 方法代替


## 更新注意事项

从 2.1.0 开始：
- SDK 所有公开方法的参数名不再混淆，可以直接通过 IDE 查看
- 所有 Listener 类与相关常量从 `PLMediaPlayer` 下调整为单独的接口，并添加 PL 前缀
- 所有 Listener 类的回调方法中的 `PLMediaPlayer` 参数均被移除
- `PLOnInfoListener.onInfo` 方法返回类型由 `boolean` 修改为 `void`
- 在一次 `seekTo` 未完成时进行下一次 `seekTo`，SDK 的回调由 `PLOnErrorListener.ERROR_SEEK_FAILED` 更改为 `PLOnInfoListener.MEDIA_INFO_IS_SEEKING`，不会修改 `PlayerState` 的状态
- `PLNetworkManager` 类被移除，请删除应用层的相关调用，并移除 happydns 的依赖。如需管理 DNS 缓存或自定义 DNS 服务器，请通过 `AVOptions.KEY_DOMAIN_LIST` 与 `AVOptions.KEY_DNS_SERVER` 进行设置
- 移除 `setDebugLoggingEnabled` 方法，日志选项统一通过 `AVOptions.KEY_LOG_LEVEL` 控制
