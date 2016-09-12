# PLDroidPlayer Release Notes for 1.3.2 Hotfix

本次更新:

## 版本

1. 发布 pldroid-player-1.3.2.jar

## 缺陷 

1. 修复了部分场景下直接使用 `PLMediaPlayer` 播放出现的崩溃问题

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

