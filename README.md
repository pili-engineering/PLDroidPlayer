# PLDroidPlayer
PLDroidPlayer 是为 pili 流媒体云服务提供的一套播放直播流的 SDK，旨在解决快速、轻松实现 Android 设备播放直播流，便于 pili 流媒体云服务的开发者专注于产品业务本身，而不必在技术细节上花费不必要的时间。

# 内容摘要
- [功能特性](#功能特性)
- [播放器对比](#播放器对比)
- [使用方法](#使用方法)
    - [配置工程](#项目配置)
    - [示例代码](#示例代码)
- [依赖库](#依赖库)
- [版本历史](#版本历史)

## 功能特性
1. 基于 [ijkplayer](https://github.com/Bilibili/ijkplayer) ( based on [ffplay](http://ffmpeg.org/) )
2. Android Min API 9
3. 支持 RTMP, HLS 协议
4. 支持 ARMv7a
5. 支持 MediaCodec 硬解码
6. 提供 `VideoView` 控件
7. 可定制化的 `MediaController`
8. 支持 `seekTo()`
9. 支持获取当前播放时长 `getDuration()`
10. 支持获取当前播放的位置 `getCurrentPosition()`
11. 支持音量控制 `setVolume()`
12. 提供如下接口：
  - `OnPreparedListener`
  - `OnCompletionListener`
  - `OnErrorListener`
  - `OnInfoListener`

## 播放器对比
| -  | ijkplayer | PLDroidPlayer |
|---|---|---|
|Shared Library Size|较大|较小|
|Shared Library Count|5个|1个|
|Widget|有商业授权风险|无商业授权风险|
|MediaController|难以定制化|容易定制化|

## 使用方法
### 项目配置
从 `releases/` 目录获取：

- pldroid-player-xxx.jar 
- ijkmediaplayer-xxx.jar
- armeabi-v7a/libpldroidplayer.so

并在项目中加入对应的 jar/so 文件的依赖关系。可参考 [PLDroidPlayerDemo][1] 中的做法。

### 示例代码

1 初始化 VideoView 及其布局
```XML
<com.pili.pldroid.player.widget.VideoView
    android:id="@+id/video_view"
    android:layout_width="fill_parent"
    android:layout_height="fill_parent"
    android:layout_gravity="center" />
```
```JAVA
  mVideoView = (VideoView) findViewById(R.id.video_view);
```

2 VideoView 与 MediaController 建立联系
```JAVA
  mMediaController.setMediaPlayer(mVideoView);
  mVideoView.setMediaController(mMediaController);
```

3 传入播放地址，可以是 `/path/to/local.mp4` 本地文件绝对路径，或 HLS URL，或 RTMP URL
```JAVA
  mVideoView.setVideoPath(mVideoPath);
```

4 设置 Listener
```JAVA
public class VideoPlayerActivity extends Activity implements
        OnCompletionListener,
        OnInfoListener,
        OnErrorListener,
        OnPreparedListener {
...
  mVideoView.setOnErrorListener(this);
  mVideoView.setOnCompletionListener(this);
  mVideoView.setOnInfoListener(this);
  mVideoView.setOnPreparedListener(this);
...

@Override
public void onCompletion(IMediaPlayer mp) {
  ...
}

@Override
public boolean onError(IMediaPlayer mp, int what, int extra) {
  ...
}

@Override
public boolean onInfo(IMediaPlayer mp, int what, int extra) {
  ...
}

@Override
public void onPrepared(IMediaPlayer mp) {
  ...
}
```

## 依赖库
* ffmpeg
* libyuv
* sdl
* libVLC

## 版本历史
* 1.0.0 ([Release Notes][2])

发布 PLDroidPlayer v1.0.0

[1]: /PLDroidPlayerDemo
[2]: /ReleaseNotes/release-notes-1.0.0.md
