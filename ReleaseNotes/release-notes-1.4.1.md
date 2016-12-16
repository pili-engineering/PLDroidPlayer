# PLDroidPlayer Release Notes for 1.4.1

本次更新:

## 版本

  - 发布 pldroid-player-1.4.1.jar
  - 更新 libpldroidplayer.so

## 功能

  - 新增硬解失败的回调

```
    public static final int ERROR_CODE_HW_DECODE_FAILURE = -2003;
```

  - 新增软硬解自动切换功能

```
    AVOptions 类:
    public final static int MEDIA_CODEC_SW_DECODE = 0;
    public final static int MEDIA_CODEC_HW_DECODE = 1;
    public final static int MEDIA_CODEC_AUTO = 2;

    AVOptions mAVOptions;
    mAVOptions.setInteger(AVOptions.KEY_MEDIACODEC, AVOptions.MEDIA_CODEC_AUTO);
```

  - 新增软硬解自动切换的消息回调

```
    public static final int MEDIA_INFO_SWITCHING_SW_DECODE = 802;
```

  - 新增直播卡顿的消息回调

```
    public static final int MEDIA_INFO_BUFFERING_START = 701;
    public static final int MEDIA_INFO_BUFFERING_END = 702;
```

  - 新增获取 metadata 的接口

```
    public HashMap<String, String> getMetadata();
```

  - AVOptions 新增 “probesize” 配置选项

```
    mAVOptions.setInteger(AVOptions.KEY_PROBESIZE, 128 * 1024);
```

  - 新增 HTTPS 协议、speex 解码、mp4v 解码支持

## 更新注意事项

  - 需要在 build.gradle 中添加如下配置：

```
    compile 'com.qiniu.pili:pili-android-qos:0.8.16'
```
