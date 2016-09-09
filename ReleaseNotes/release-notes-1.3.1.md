# PLDroidPlayer Release Notes for 1.3.1

本次更新:

## 版本

1. 发布 pldroid-player-1.3.1.jar
2. 更新 libpldroidplayer.so

## 优化

1. 优化了纯音频播放的累积延时

## 功能

1. 添加了 QoS 功能

## 缺陷 

1. 修复了在部分场景下频繁重连导致的崩溃问题
2. 修复了 DNS 解析优化在某些机型上出现崩溃的问题
3. 修复了当服务端主动断开 TCP 连接，客户端没重连的问题 
4. 修复了 x86_64 架构下找不到动态库导致的崩溃问题

## 其他

1. 更新了 demo 代码，演示了如何进行重连


## 更新注意事项

**1. 需要在 build.gradle 中添加如下配置**

```
compile 'com.qiniu.pili:pili-android-qos:0.8.+'
```

**2. `PLMediaPlayer` 的构造函数接口更新**

如果直接使用 `PLMediaPlayer` 进行播放的话，需要多添加一个 `Context` 参数
```
public PLMediaPlayer(Context context);
public PLMediaPlayer(Context context, AVOptions options);
```

