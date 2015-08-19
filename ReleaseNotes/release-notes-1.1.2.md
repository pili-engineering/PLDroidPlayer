# PLDroidPlayer Release Notes for 1.1.2

## 简介
PLDroidPlayer 为 Android 开发者提供直播播放 SDK

## 记录

### 播放器

- 发布 pldroid-player-1.1.2.jar
- 更新 arm64-v8a/libpldroidplayer.so，armeabi-v7a/libpldroidplayer.so
- 修复推流端断流后，Player 概率性地无 onCompletion 回调通知
- 修复 `AVOptions` 的 key 没有设置 value 时候的 Crash 问题
