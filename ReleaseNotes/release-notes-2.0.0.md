# PLDroidPlayer Release Notes for 2.0.0

本次更新:

## 说明

- 本次更新，全面升级为七牛自研的播放器内核，拥有更优异的性能

## 版本

- 发布 pldroid-player-2.2.0.jar
- 发布 libQPlayer.so
- 发布 libqcCodec.so
- 删除 pldroidplayer.so

## 功能

- 新增倍数播放功能（0.5x，1x，2x，4x 等）
- 新增 mp4 本地缓存功能
- 新增音视频解码数据回调
- 新增自定义音视频播放和渲染
- 新增 HLS 七牛私有 DRM 的支持 
- 新增 H.265 格式播放的支持

## 优化

- 优化 CPU、内存和功耗
- 优化首开效果，首开速度有大幅提升
- 优化包体大小，jar 包和 so 体积均有显著减小
- 优化重连逻辑，不用销毁播放器，网络断开后内部自动重连
- 优化 mp4 点播，使用双 IO 技术更高效地播放 moov 在尾部的 mp4 文件
- 支持播放过程中变速不变调，可实现更平滑的追帧效果，更少的卡顿率

## 特性

- 支持 RTMP，HLS，HDL（HTTP-FLV）协议
- 支持 flv，mp4，mp3 等视频格式
- 支持 H.264/H.265，AAC 软解和硬解
- 支持 HTTPS 协议

## 更新注意事项

- 需要替换新的动态库文件
  - 删除 pldroidplayer.so
  - 新增 libQPlayer.so 和 libqcCodec.so

- 需要更新混淆方式
  ```
  -keep class com.pili.pldroid.player.** { *; }
  -keep class com.qiniu.qplayer.mediaEngine.MediaPlayer{*;}
  ```

- 去掉了一些 `AVOptions` 参数，下面的参数不用再配置

  ```
  AVOptions.KEY_LIVE_STREAMING
  AVOptions.KEY_DELAY_OPTIMIZATION
  AVOptions.KEY_START_ON_PREPARED
  AVOptions.KEY_PROBESIZE
  ```

- SDK 内部自动重连，不再需要外部进行重连

  - 网络断开后，SDK 会自动重连，并触发  `ERROR_CODE_IO_ERROR` 消息
  - 重连成功后，SDK 会触发 `MEDIA_INFO_CONNECTED` 消息

- 更新了一些消息回调
  - 精简了错误码，新的错误码定义如下：

  | errorCode                    | value | 描述      |
  | ---------------------------- | ----- | ------- |
  | MEDIA_ERROR_UNKNOWN          | -1    | 未知错误    |
  | ERROR_CODE_OPEN_FAILED       | -2    | 播放器打开失败 |
  | ERROR_CODE_IO_ERROR          | -3    | 网络异常    |
  | ERROR_CODE_SEEK_FAILED       | -4    | 拖动失败    |
  | ERROR_CODE_HW_DECODE_FAILURE | -2003 | 硬解失败    |

  - 新增部分状态回调，新增的状态码定义如下：

  | what                     | value | 描述        |
  | ------------------------ | ----- | --------- |
  | MEDIA_INFO_CONNECTED     | 200   | 连接成功      |
  | MEDIA_INFO_VIDEO_BITRATE | 20001 | 视频的码率统计结果 |
  | MEDIA_INFO_VIDEO_FPS     | 20002 | 视频的帧率统计结果 |
  | MEDIA_INFO_AUDIO_BITRATE | 20003 | 音频的帧率统计结果 |
  | MEDIA_INFO_AUDIO_FPS     | 20004 | 音频的帧率统计结果 |
