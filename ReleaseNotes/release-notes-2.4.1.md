# PLDroidPlayer Release Notes for 2.4.1

本次更新:


## 版本

- 发布 pldroid-player-2.4.1.jar。
- 更新 libQPlayer.so。
- 更新 libqcOpenSSL.so。

## 功能
- 新增https在请求时 带上请求的SNI
- 由于某些原因，暂时禁用apm上报

## 缺陷

- 修复硬解情况下用PLTextureView作为渲染view 在停止播放时崩溃。