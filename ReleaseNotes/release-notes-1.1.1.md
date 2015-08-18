# PLDroidPlayer Release Notes for 1.1.1

## 简介
PLDroidPlayer 为 Android 开发者提供直播播放 SDK

## 记录

### 播放器

- 发布 pldroid-player-1.1.1.jar 
- 发布 arm64-v8a/libpldroidplayer.so，增加 arm64 v8a 支持
- 更新 arm-v7a 版本的 libpldroidplayer.so
- 增加 `AVOptions` 类，可设置如下属性：
  * `AVOptions.KEY_GET_AV_FRAME_TIMEOUT`  // ms
  * `AVOptions.KEY_MEDIACODEC`            // 1 means enable, 0 means disable
  * `AVOptions.KEY_FFLAGS`                // "nobuffer"
  * `AVOptions.KEY_BUFFER_TIME`           // ms
- 修复部分音视频流无法播放的问题
- 修复仅含视频流无法播放的问题
- 优化连接时间
- 去除 `setBufferTime(float ms)` 接口，使用 `AVOptions` 代替


### Demo
- 增加 `AVOptions` 的演示代码
