package com.wuzp.corelib.core;

import android.support.v4.util.ArrayMap;
import com.wuzp.corelib.core.internal.LiveHandlerImpl;

import java.util.Map;

public abstract class ScopeContextBase implements ScopeContext, IScopeLifecycle {

    private LiveHandlerImpl mLiveHandler;

    private Map<String, Object> mAttached = new ArrayMap<>();

    @Override
    public void onCreate(ILive live) {
        liveHandler().onCreate(live);
    }

    @Override
    public void onStart(ILive live) {
        liveHandler().onStart(live);
    }

    @Override
    public void onResume(ILive live) {
        liveHandler().onResume(live);
    }

    @Override
    public void onPause(ILive live) {
        liveHandler().onPause(live);
    }

    @Override
    public void onStop(ILive live) {
        liveHandler().onStop(live);
    }

    @Override
    public void onDestroy(ILive live) {
        liveHandler().onDestroy(live);
    }

    @Override
    public LiveHandler getLiveHandler() {
        return liveHandler();
    }

    @Override
    public boolean addObserver(IScopeLifecycle observer) {
        return liveHandler().addObserver(observer);
    }

    @Override
    public boolean removeObserver(IScopeLifecycle observer) {
        return liveHandler().removeObserver(observer);
    }

    @Override
    public ScopeContext getParent() {
        return null;
    }

    private LiveHandlerImpl liveHandler() {
        if (mLiveHandler == null) {
            mLiveHandler = new LiveHandlerImpl();
        }
        return mLiveHandler;
    }

    @Override
    public Object attach(String key, Object object) {
        return mAttached.put(key, object);
    }

    @Override
    public Object detach(String key) {
        return mAttached.remove(key);
    }

    @Override
    public Object getObject(String key) {
        return mAttached.get(key);
    }

    @Override
    public void detachAll() {
        mAttached.clear();
    }
}
