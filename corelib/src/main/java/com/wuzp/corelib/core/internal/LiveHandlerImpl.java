package com.wuzp.corelib.core.internal;

import android.support.annotation.RestrictTo;
import com.wuzp.corelib.core.Cancelable;
import com.wuzp.corelib.core.ILive;
import com.wuzp.corelib.core.IScopeLifecycle;
import com.wuzp.corelib.core.LiveHandler;

import java.util.ArrayList;
import java.util.List;


/**
 * Defines an object that has a  Lifecycle
 * You can use it as page's live proxy
 * <p>
 * The page's live state can be obtained through pagelive
 * and the live callback that can be registered to monitor that page
 */
@RestrictTo(RestrictTo.Scope.LIBRARY)
public final class LiveHandlerImpl implements IScopeLifecycle, LiveHandler {

    private PageStatus mPageStatus;
    private List<Cancelable> mCancelableList = new ArrayList<>();
    private List<IScopeLifecycle> mObservers = new ArrayList<>();

    @Override
    public void onCreate(ILive live) {
        mPageStatus = PageStatus.Create;
        dispatchState(live);
    }

    @Override
    public void onStart(ILive live) {
        mPageStatus = PageStatus.Start;
        dispatchState(live);
    }

    @Override
    public void onResume(ILive live) {
        mPageStatus = PageStatus.Resume;
        dispatchState(live);
    }

    @Override
    public void onPause(ILive live) {
        mPageStatus = PageStatus.Pause;
        dispatchState(live);
    }

    @Override
    public void onStop(ILive live) {
        mPageStatus = PageStatus.Stop;
        dispatchState(live);
    }

    @Override
    public void onDestroy(ILive live) {
        mPageStatus = PageStatus.Destroy;
        dispatchState(live);
    }

    public boolean addObserver(IScopeLifecycle observer) {
        return mObservers.add(observer);
    }

    public boolean removeObserver(IScopeLifecycle observer) {
        return mObservers.remove(observer);
    }

    @Override
    public void bind(Cancelable cancelable) {
        if (cancelable == null) {
            return;
        }

        if (isDestroyed()) {
            cancelable.cancel();
            return;
        }

        if (!mCancelableList.contains(cancelable)) {
            mCancelableList.add(cancelable);
        }
    }

    @Override
    public boolean isActive() {
        return (mPageStatus == PageStatus.Create
            || mPageStatus == PageStatus.Start
            || mPageStatus == PageStatus.Resume);
    }

    @Override
    public boolean isDestroyed() {
        return (mPageStatus == PageStatus.Destroy);
    }

    private void dispatchState(ILive live) {
        updateState(live);
        if (isDestroyed()) {
            dispatchDestroyed();
        }
    }

    private void updateState(ILive live) {
        List<IScopeLifecycle> observables = new ArrayList<>(mObservers);
        for (IScopeLifecycle o : observables) {
            if (mPageStatus == PageStatus.Create) {
                o.onCreate(live);
            } else if (mPageStatus == PageStatus.Start) {
                o.onStart(live);
            } else if (mPageStatus == PageStatus.Resume) {
                o.onResume(live);
            } else if (mPageStatus == PageStatus.Pause) {
                o.onPause(live);
            } else if (mPageStatus == PageStatus.Stop) {
                o.onStop(live);
            } else if (mPageStatus == PageStatus.Destroy) {
                o.onDestroy(live);
            }
        }

    }

    private void dispatchDestroyed() {
        List<Cancelable> cancelables = new ArrayList<>(mCancelableList);
        for (Cancelable cancelable : cancelables) {
            if (cancelable != null) {
                cancelable.cancel();
            }
        }
        mCancelableList.clear();
        mObservers.clear();
    }

}
