# PLDroidPlayer
PLDroidPlayer 是一个适用于 Android 的音视频播放器 SDK，可高度定制化和二次开发，特色是支持 RTMP 和 HLS 直播流媒体、以及常见音视频文件（如 MP4、M4A ）播放。

# 功能特性
  - [x] 基于 [ijkplayer](https://github.com/Bilibili/ijkplayer) ( based on [ffplay](http://ffmpeg.org/) )
  - [x]  Android Min API 9
  - [x] 支持 RTMP, HLS 协议
  - [x] 支持 ARM v7a, ARM64 v8a
  - [x] 支持 MediaCodec 硬解码
  - [x] 支持纯音频播放，并支持后台运行
  - [x] 提供 `VideoView` 控件
  - [x] 可定制化的 `MediaController`
  - [x] 支持 `seekTo()`
  - [x] 支持获取当前播放时长 `getDuration()`
  - [x] 支持获取当前播放的位置 `getCurrentPosition()`
  - [x] 支持音量控制 `setVolume()`
  - [x] 提供如下接口：
    - `OnPreparedListener`
    - `OnCompletionListener`
    - `OnErrorListener`
    - `OnInfoListener`

# 内容摘要
- [播放器对比](#播放器对比)
- [使用方法](#使用方法)
    - [配置工程](#项目配置)
    - [示例代码](#示例代码)
- [依赖库](#依赖库)
- [版本历史](#版本历史)



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
#### Video 播放

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

#### 纯音频播放
1 实例化 `AudioPlayer`
```JAVA
mAudioPlayer = new AudioPlayer(this);
```

2 `AudioPlayer` 与 `MediaController` 建立联系
```JAVA
mMediaController.setMediaPlayer(mAudioPlayer);
mAudioPlayer.setMediaController(mMediaController);
```

3 传入播放地址，可以是 `/path/to/local.mp3` 本地音频文件绝对路径，或 HLS URL，或 RTMP URL
```JAVA
mAudioPlayer.setAudioPath(mAudioPath);
```

4 设置 Listener
```JAVA
mAudioPlayer.setOnErrorListener(this);
mAudioPlayer.setOnCompletionListener(this);
mAudioPlayer.setOnInfoListener(this);
mAudioPlayer.setOnPreparedListener(this);
```

## 依赖库
* ffmpeg
* libyuv
* sdl
* libVLC

## 版本历史
* 1.1.0 ([Release Notes][4])
  - 发布 pldroid-player-1.1.1.jar 
  - 发布 arm64-v8a/libpldroidplayer.so，增加 arm64 v8a 支持
  - 更新 arm-v7a 版本的 libpldroidplayer.so
  - 增加 `AVOptions` 类，可设置如下属性：
    * `AVOptions.KEY_GET_AV_FRAME_TIMEOUT`  // ms
    * `AVOptions.KEY_MEDIACODEC`            // 1 means enable, 0 means disable
    * `AVOptions.KEY_FFLAGS`                // "nobuffer"
    * `AVOptions.KEY_BUFFER_TIME`           // ms
  - 修复部分音视频流无法播放的问题
  - 修复仅含视频流无法播放的问题
  - 优化连接时间
  - 废除 `setBufferTime(float ms)` 接口，使用 `AVOptions` 代替
  - 增加 `AVOptions` 的演示代码

* 1.1.0 ([Release Notes][3])
  - 发布 pldroid-player-1.1.0.jar
  - 更新 ijkmediaplayer.jar
  - 更新 libpldroidplayer.so
  - 添加纯音频播放接口，支持后台运行
  - 添加 bufferTime 设置接口：`setBufferTime(float ms)`
  - 添加状态码：`EXTRA_CODE_CONNECTION_REFUSED` 和 `EXTRA_CODE_EOF`
  - 优化播放延时
  - 优化播放过程中因断流导致的等待时间
  - 修复部分机型硬解码异常问题
  - 添加纯音频播放展示界面

* 1.0.0 ([Release Notes][2])
  - 发布 PLDroidPlayer v1.0.0

[1]: /PLDroidPlayerDemo
[2]: /ReleaseNotes/release-notes-1.0.0.md
[3]: /ReleaseNotes/release-notes-1.1.0.md
[4]: /ReleaseNotes/release-notes-1.1.1.md
