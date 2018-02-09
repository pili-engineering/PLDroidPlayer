# PLDroidPlayer Release Notes for 2.1.0

本次更新:

## 说明

- 从 v2.0.0 版本开始，全面升级为七牛自研的播放器内核，拥有更优异的性能

## 版本

- 发布 pldroid-player-2.1.0.jar
- 更新 libQPlayer.so
- 更新 libqcCodec.so
- 更新 libqcOpenSSL.so

## 新增

- 新增 `captureImage` 方法
- 新增 `setVideoArea` 方法
- 新增 `PLOnImageCapturedListener` 接口
- 新增 `setVolume` 参数大于 1.0 时增大音量的功能
- 新增 `setPlaySpeed(float)` 方法
- 新增 `AVOptions.KEY_FAST_OPEN` 字段
- 新增 `AVOptions.KEY_SEEK_MODE` 字段
- 新增 `AVOptions.KEY_CACHE_EXT` 字段
- 新增 `AVOptions.KEY_OPEN_RETRY_TIMES` 字段
- 新增 `PLOnInfoListener.MEDIA_INFO_CACHED_COMPLETE` 字段
- 新增 `PLOnInfoListener.MEDIA_INFO_IS_SEEKING` 字段
- 新增 `PLMediaPlayer.ERROR_CODE_PLAYER_CREATE_AUDIO_FAILED` 字段

## 缺陷

- 修复 `PLVideoTextureView` 复用时可能 crash 的问题
- 修复 `PLVideoTextureView` 部分场景下画面不刷新的问题
- 修复部分场合下自动断网重连失效的问题
- 修复开启离线缓存后断网时无法继续播放的问题
- 修复部分设备上后台播放音频卡顿的问题
- 修复循环播放时 buffer 时间较长的问题
- 修复播放部分 http-flv 格式时 crash 的问题
- 修复播放部分带有跳转的 URL 时crash 的问题
- 修复倍速播放时音调不正常的问题
- 修复播放本地文件时调用 `seekTo` 仍会出现 buffering 回调的问题
- 修复部分场景下 `seekTo` 不生效的问题
- 修复部分流中 SAR 与 DAR 字段不生效的问题
- 修复 SEI 数据回调为 0 的问题

## 更新注意事项

从 2.1.0 开始：
- SDK 所有公开方法的参数名不再混淆，可以直接通过 IDE 查看
- 所有 Listener 类与相关常量从 `PLMediaPlayer` 下调整为单独的接口，并添加 PL 前缀
- 所有 Listener 类的回调方法中的 `PLMediaPlayer` 参数均被移除
- `PLOnInfoListener.onInfo` 方法返回类型由 `boolean` 修改为 `void`
- 在一次 `seekTo` 未完成时进行下一次 `seekTo`，SDK 的回调由 `PLOnErrorListener.ERROR_SEEK_FAILED` 更改为 `PLOnInfoListener.MEDIA_INFO_IS_SEEKING`，不会修改 `PlayerState` 的状态
- `PLNetworkManager` 类被移除，请删除应用层的相关调用，并移除 happydns 的依赖。如需管理 DNS 缓存或自定义 DNS 服务器，请通过 `AVOptions.KEY_DOMAIN_LIST` 与 `AVOptions.KEY_DNS_SERVER` 进行设置
- 移除 `setDebugLoggingEnabled` 方法，日志选项统一通过 `AVOptions.KEY_LOG_LEVEL` 控制
