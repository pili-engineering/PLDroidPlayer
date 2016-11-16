# PLDroidPlayer Release Notes for 1.4.0

本次更新:

## 版本

1. 发布 pldroid-player-1.4.0.jar
2. 更新 libpldroidplayer.so

## 功能

1. 新增设置封面功能

```
    /**
     * Sets the cover view
     * The view will be shown when the player is preparing
     *
     * @param coverView the cover view.
     */
    public void setCoverView(View coverView);
```

2. 新增获取当前播放状态接口

```
    /**
     * Get the current play state
     * @return play state
     */
    public PlayerState getPlayerState();

    public enum PlayerState {
        IDLE,
        PREPARING,
        PREPARED,
        PLAYING,
        BUFFERING,
        PAUSED,
        COMPLETED,
        ERROR
    }
```

## 优化

1. 升级 ijk 版本 0.7.4
2. 升级 ffmpeg 版本 3.2

## 缺陷 

1. 修复了多次打开、关闭播放器出现 ANR 的问题
2. 修复了播放地址含有多个 domain 时解析异常的问题
