# PLDroidPlayer Release Notes for 2.0.1

本次更新:

## 说明

- 从 v2.0.0 版本开始，全面升级为七牛自研的播放器内核，拥有更优异的性能

## 版本

- 发布 pldroid-player-2.0.1.jar
- 更新 libQPlayer.so

## 功能

- 新增 SEI 数据帧回调
- `AVOptions` 中新增 `PREFER_FORMAT` 字段

## 缺陷

- 修复 `pause` 后 `getPlayState` 状态不对的问题
- 修复 `setVolume` 在部分场合下不生效的问题
- 修复 `setLooping` 不生效的问题
- 修复 `OnInfoListener` 视频旋转角度缺失的问题
- 修复设置 HTTP 头无效的问题
- 修复部分视频流无法播放的问题
- 修复部分视频 seek 出错的问题
- 修复网络变化时可能 crash 的问题
- 修复倍数播放没有声音的问题
- 修复了部分由断网重连引发的问题


## 更新注意事项

如果从旧版本升级，建议参考 v2.0.0 版本及后续的 [ReleaseNote](https://github.com/pili-engineering/PLDroidPlayer/blob/master/ReleaseNotes/release-notes-2.0.0.md) ，查看注意事项
