# PLDroidPlayer Release Notes for 2.1.4

本次更新:

## 说明

- 从 v2.0.0 版本开始，全面升级为七牛自研的播放器内核，拥有更优异的性能

## 版本

- 发布 pldroid-player-2.1.4.jar
- 更新 libQPlayer.so

## 优化

- 优化了 mp4 长视频的打开速度

## 新增

- 新增 KEY_START_POSITION 的 AVOptions 设置
- 添加了对 ffconcat 文件格式的支持

## 缺陷

- 修复了设置 `AVOptions.KEY_LIVE_STREAMING` 后，暂停状态下切换播放地址画面不刷新的问题
- 修复了部分停止时造成的 ANR 问题
- 修复了 PLOnInfoListener 的 MEDIA_INFO_LOOP_DONE 回调不生效的问题
- 修复了部分 seek 无效的问题
- 修复了部分 HLS 文件音画不同步的问题
- 修复了偶现网络状态改变造成的 ANR 问题
- 修复个别视频播放到一半时提前结束的问题
- 修复播放直播流偶现电流音的问题
- 修复退出时 listener 泄漏的问题