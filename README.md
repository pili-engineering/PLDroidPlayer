# PiliPlayer
PiliPlayer 是为 pili 流媒体云服务提供的一套播放直播流的 SDK，旨在解决快速、轻松实现 Android 设备播放直播流，便于 pili 流媒体云服务的开发者专注于产品业务本身，而不必在技术细节上花费不必要的时间。

# 内容摘要
- [Feature List](#Feature-List)
- [SDK](#SDK)
- [为什么选择 PiliPlayer](#为什么选择-PiliPlayer)
- [使用方法](#使用方法)
    - [配置工程](#项目配置)
    - [示例代码](#示例代码)
- [依赖库](#依赖库)
- [版本历史](#版本历史)

## Feature List
[FeatureList.md][4]

## SDK
SDK 提供了 VideoView, IMediaController。
* VideoView 用来显示 Video 数据的[SurfaceView控件][1]
* IMediaController 定义了 MediaController 控件的相关接口，为了便于您进行定制化，您需要自己实现 MediaController 控件，可参考 PiliPlayerDemo 中的[MediaController.java][2]
```JAVA
public void setMediaPlayer(MediaPlayerControl player);
public void show();
public void show(int timeOut);
public void hide();
public boolean isShowing();
public void setEnabled(boolean enable);
public void setAnchorView(View view);
```
* VideoView 与 MediaController 通过 MediaPlayerControl 建立了调用关系。

## 为什么选择 PiliPlayer
| -  | ijkplayer | PiliPlayer |
|---|---|---|
|Shared Library Size|较大|较小|
|依赖 Shared Library 的数量|5个|1个|
|widget|有商业授权风险|无商业授权风险|
|MediaController|无法定制化|可定制化|

## 使用方法
### 项目配置
从 releases 目录下载 **piliplayer-xxx.jar**, **ijkmediaplayer-xxx.jar**,以及 **armeabi-v7a/libpiliplayer.so** 或 **x86/libpiliplayer.so**，并在项目中加入对应的 jar/so 文件的依赖关系。可参考 [PiliPlayerDemo][3] 中的做法。

### 示例代码
#### 初始化 VideoView 及其布局
```XML
<com.pili.android.piliplayer.widget.VideoView
    android:id="@+id/video_view"
    android:layout_width="fill_parent"
    android:layout_height="fill_parent"
    android:layout_gravity="center" />
```
```JAVA
  mVideoView = (VideoView) findViewById(R.id.video_view);
```

#### VideoView 与 MediaController 建立联系
```JAVA
  mMediaController.setMediaPlayer(mVideoView);
  mVideoView.setMediaController(mMediaController);
```

#### 传入文件路径或者 http/rtmp 地址
```JAVA
  mVideoView.setVideoPath(mVideoPath);
```

#### 设置 Listener
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
public void onCompletion(IMediaPlayer iMediaPlayer) {
  ...
}

@Override
public boolean onError(IMediaPlayer iMediaPlayer, int framework_err, int impl_err) {
  ...
}

@Override
public boolean onInfo(IMediaPlayer iMediaPlayer, int what, int extra) {
  ...
}

@Override
public void onPrepared(IMediaPlayer iMediaPlayer) {
  ...
}
```

## 依赖库
* ffmpeg
* libyuv
* sdl
* libVLC

## 版本历史
* 1.0.0 ([Release Notes][5])

发布PLDroidPlayer v1.0.0

[1]: http://developer.android.com/reference/android/view/SurfaceView.html
[2]: /PiliPlayerDemo/app/src/main/java/com/pili/android/playerdemo/widget/MediaController.java
[3]: /PiliPlayerDemo
[4]: /FeatureList.md
[5]: /ReleaseNotes/release-notes-1.0.0.md
