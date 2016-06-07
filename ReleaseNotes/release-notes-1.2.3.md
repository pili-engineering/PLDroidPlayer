# PLDroidPlayer Release Notes for 1.2.3

## 简介
PLDroidPlayer 为 Android 开发者提供直播播放 SDK

## 记录

### 播放器
- 发布 pldroid-player-1.2.3.jar
- 更新 libpldroidplayer.so
- 支持后台播放
- 优化点播的首开时间
- 优化直播过程中的累积延时
- 新增 `setWakeMode` 和 `setScreenOnWhilePlaying` 接口
- 新增 `setLooping` 和 `isLooping` 接口
- `AVOption` 添加 `prepare timeout` 超时的配置
- 修改 `seekTo` 的参数，类型改为`long` 型
- 解决播放部分纯音频流的时候，获取不到时长的问题
- 解决从后台切换回来后，播放从头开始加载的问题
- 修复`AudioManager`可能导致的内存泄漏问题

### 播放器 Demo

- 更新 demo ，演示后台播放和`prepare timeout` 超时的配置