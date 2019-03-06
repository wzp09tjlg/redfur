package com.wuzp.corelib.core;


/**
 * Scope 生命周期回调接口
 */
public interface IScopeLifecycle {

    void onCreate(ILive live);

    void onStart(ILive live);

    void onResume(ILive live);

    void onPause(ILive live);

    void onStop(ILive live);

    void onDestroy(ILive live);

    /**
     * Page 对应的生命周期状态
     */
    enum PageStatus {
        Create,
        Start,
        Resume,
        Stop,
        Pause,
        Destroy,
    }
}
