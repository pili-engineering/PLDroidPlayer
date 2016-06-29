# PLDroidPlayer Release Notes for 1.3.0

本次更新:

## 版本

1. 发布 pldroid-player-1.3.0.jar
2. 更新 libpldroidplayer.so

## 优化

1. 优化直播累积延时
2. 优化首开时间
3. 优化播放器退出耗时较长

## 功能

1. 支持带 IP 地址的播放 URL，支持的格式为：protocol://ip/path?domain=xxxx.com
   目前支持的协议为：RTMP、HDL，暂时不支持 HLS 协议
2. 新增 PLNetworkManager 类，提供 DNS 缓存管理服务
3. 新增 AVOptions 累积延时相关的配置参数
4. 新增多种新的回调信息，方便更准确地感知播放过程中的状态变化

## 缺陷 

1. 修复动态构建 PLVideoTextureView 导致崩溃的问题
2. 修复 onError 回调 extra 为 0 的情况
3. 修复了部分码流的时长解析不准确的问题
4. 修复点播缓冲过程中，断网操作导致长时间无法恢复的问题
5. 修复特殊网络情况下，退出播放器时的 ANR 问题

## 更新注意事项

1. 如果需要使用 SDK 提供的 DNS 缓存管理服务，则需要添加如下配置

```
// 添加 happydns 的依赖
dependencies {
    compile 'com.qiniu:happy-dns:0.2.+'
}

// 添加网络状态监测的权限
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>

```





