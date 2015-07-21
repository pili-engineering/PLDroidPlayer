# PLDroidPlayer Release Notes for 1.1.0

## 简介
PLDroidPlayer 为 Android 开发者提供直播播放 SDK

## 记录

### 播放器
* 发布 pldroid-player-1.1.0.jar 
* 更新 ijkmediaplayer.jar
* 更新 libpldroidplayer.so
* 添加纯音频播放接口，支持后台运行
* 添加 bufferTime 设置接口：`setBufferTime(float ms)`
* 添加状态码：`EXTRA_CODE_CONNECTION_REFUSED` 和 `EXTRA_CODE_EOF`
* 优化播放延时
* 优化播放过程中因断流导致的等待时间
* 修复部分机型硬解码异常问题

### Demo
* 添加纯音频播放展示界面
