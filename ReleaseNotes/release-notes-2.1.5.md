# PLDroidPlayer Release Notes for 2.1.5

本次更新:

## 说明

- 从 v2.0.0 版本开始，全面升级为七牛自研的播放器内核，拥有更优异的性能

## 版本

- 发布 pldroid-player-2.1.5.jar
- 合并 libQPlayer.so、libqcCodec.so 为 libQPlayer.so

## 优化

- 优化了视频首帧的打开速度
- 优化了音频开始时间不为 0 的播放逻辑
- 取消了直播流时的变速播放


## 新增

- 新增 KEY_MP4_PRELOAD 的 AVOptions 设置
- 新增了 getResponseInfo 方法

## 缺陷

- 修复弱网时重复打开的崩溃
- 修复 m4a 不能播放
- 修复 mp4 缓存失败
- 修复了多个播放实例场景下的偶现崩溃
- 修复了 dns 解析造成的崩溃
- 修复了播放时释放时偶现 ANR
- 修复音画不同步问题
- 修复纯音频循环播放问题
- 修复 HLS 文件解析异常

