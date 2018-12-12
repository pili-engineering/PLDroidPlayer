# PLDroidPlayer Release Notes for 2.1.6

本次更新:

## 说明

- 从 v2.0.0 版本开始，全面升级为七牛自研的播放器内核，拥有更优异的性能

## 版本

- 发布 pldroid-player-2.1.6.jar
- 更新 libQPlayer.so

## 优化

- 优化了部分 HLS 播放流畅度
- 优化了重连速度

## 新增

- 新增了 addCache、delCache 方法
- 新增了 setIOCacheSize、addIOCache、delIOCache 方法
- 新增了本地日志功能

## 缺陷

- 修复了部分场景下播放状态异常
- 修复了播放时释放时偶现 ANR
- 修复了 HLS 文件解析异常
- 修复了部分场景下空指针、空对象异常

