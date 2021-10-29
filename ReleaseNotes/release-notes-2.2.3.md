# PLDroidPlayer Release Notes for 2.2.3

本次更新:


## 版本

- 发布 pldroid-player-2.2.3.jar。
- 更新 libQPlayer.so。
- 更新openssl lib 至 1.1.1d。
- 更新ffmpeg至4.4。
- 对`PLOnInfoListener` `PLOnErrorListener`的接口增加参数，传递更多信息。
- 更新埋点上报逻辑。


## 缺陷

- 修复Open Failed error 上抛2次。
- 修复当前渲染帧和当前上抛的SEI数据时间戳不同步。
- 修复渲染时的颜色color range 和color space 适配。
- 修复硬解时 first frame回调时机过早。
- 修复H265视频花屏问题。
- 修复android 硬解seek后音画不同步问题。
- rtmp协议播放无效地址 返回错误时间由20s缩短到5s。
- 修复seek后进度条横跳的问题。修复边播边下出错时，删除对应的本地文件。
- 修复音画不同步问题。
