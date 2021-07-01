# PLDroidPlayer Release Notes for 2.2.2

本次更新:


## 版本

- 发布 pldroid-player-2.2.2.jar
- 更新 libQPlayer.so

- 新增缓存文件名加密开关，使用缓存功能时，存本地的文件名进行加密处理。
- 新增H265编码的视频支持SEI数据回调。 
- 新增短视频demo： NiuDroidPlayer

## 缺陷

- 修复SEI数据回调时，数组越界导致崩溃。
- 修复视频流format改变时，且打开了视频数据回调，则有机率崩溃。
- 修复视频流的NAL头格式不一致时，SEI数据没有回调。