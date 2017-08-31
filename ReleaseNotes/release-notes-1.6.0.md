# PLDroidPlayer Release Notes for 1.6.0

本次更新:

## 版本

- 发布 pldroid-player-1.6.0.jar
- 更新 libpldroidplayer.so

## 功能

- 新增音视频播放时间戳回调

```
    /**
     * The rendering video frame.
     *
     * pts per frame: unit: ms
     *
     * @see OnInfoListener
     */
    public static final int MEDIA_INFO_VIDEO_FRAME_RENDERING = 10004;

    /**
     * The rendering audio frame.
     *
     * pts per frame: unit: ms
     *
     * @see OnInfoListener
     */
    public static final int MEDIA_INFO_AUDIO_FRAME_RENDERING = 10005;

    // enable audio/video rendering msg callback
    int isEnablRenderingMsg = 1;
    mAVOptions.setInteger(AVOptions.KEY_AUDIO_RENDER_MSG, isEnablRenderingMsg);
    mAVOptions.setInteger(AVOptions.KEY_VIDEO_RENDER_MSG, isEnablRenderingMsg);
```

- `MEDIA_INFO_VIDEO_RENDERING_START` 消息的 `extra` 参数附带首帧时间
- `MEDIA_INFO_AUDIO_RENDERING_START` 消息的 `extra` 参数附带首帧时间
- `OnPreparedListener` 消息的 `onPrepared` 接口增加 `preparedTime` 时间
- 新增 setVideoUri(Uri uri, Map<String, String> headers) 接口，支持自定义消息头

## 缺陷 

- 修复部分场景下多线程同步导致的 QOS 模块 NPE 异常
- 兼容不支持 rtmp_live 参数的直播流播放

## 更新注意事项

- **不再需要在** build.gradle 中添加如下配置了:

```
    compile 'com.qiniu.pili:pili-android-qos:0.8.+'
```

