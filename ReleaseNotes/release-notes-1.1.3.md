# PLDroidPlayer Release Notes for 1.1.3

## 简介
PLDroidPlayer 为 Android 开发者提供直播播放 SDK

## 记录

### 播放器

- 发布 pldroid-player-1.1.3.jar
- 更新 libpldroidplayer.so
- 新增 armeabi, x86 支持
- 新增 `KEY_LIVE_STREAMING` option
- 修复 `getCurrentPosition` 和 `getDuration` 返回值异常问题
- 修复播放过程中，概率性不间断地回调 onCompletion 问题

### 播放器 Demo

- 更新不同播放方式（直播或点播）设置 option 的展示代码
