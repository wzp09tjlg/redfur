package com.wuzp.corelib.core;

import android.support.annotation.RestrictTo;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

@RestrictTo(RestrictTo.Scope.LIBRARY)
public final class ComponentGroup implements Iterable<Component>, IScopeLifecycle {

    private List<Component> mComponents;

    private IScopeLifecycle.PageStatus mPageEvent = null;

    public ComponentGroup() {
        mComponents = new LinkedList<>();
    }

    public final Component getComponent(Class<? extends Component> clazz) {
        for (Component c : mComponents) {
            if (c.getClass() == clazz) {
                return c;
            }
        }
        return null;
    }

    public final boolean addComponent(Component component) {
        ThreadUtils.ensureMainThread();
        //initial it here
        if (component == null) {
            return false;
        }
        component.init();

        mComponents.add(component);
        if (mPageEvent == null) {
            return true;
        }
        switch (mPageEvent) {
            case Create:
                component.onCreate();
                break;
            case Start:
                component.onCreate();
                component.onStart();
                break;
            case Resume:
                component.onCreate();
                component.onStart();
                component.onResume();
                break;
            case Pause:
                component.onCreate();
                component.onStart();
                break;
            case Stop:
                component.onCreate();
                break;
            case Destroy:
                break;
        }
        return true;
    }


    public boolean removeComponent(Component component) {
        ThreadUtils.ensureMainThread();
        if (component == null) {
            return false;
        }
        boolean removed = mComponents.remove(component);

        if (!removed) {
            return false;
        }

        if (mPageEvent == null) {
            return true;
        }
        switch (mPageEvent) {
            case Create: {
                component.onDestroy();
                break;
            }
            case Start: {
                component.onStop();
                component.onDestroy();
                break;
            }
            case Resume: {
                component.onPause();
                component.onStop();
                component.onDestroy();
                break;
            }
            case Pause: {
                component.onStop();
                component.onDestroy();
                break;
            }
            case Stop: {
                component.onDestroy();
                break;
            }
            case Destroy:
            default:
                break;
        }
        component.detachView();
        return true;
    }

    public final void clear() {
        mComponents.clear();
        mPageEvent = null;
    }

    @Override
    public Iterator<Component> iterator() {
        return Collections.unmodifiableCollection(mComponents).iterator();
    }


    @Override
    public void onCreate(ILive live) {
        mPageEvent = IScopeLifecycle.PageStatus.Create;
        for (Component c : mComponents) {
            c.onCreate();
        }
    }

    @Override
    public void onStart(ILive live) {
        mPageEvent = PageStatus.Start;
        for (Component c : mComponents) {
            c.onStart();
        }
    }

    @Override
    public void onResume(ILive live) {
        mPageEvent = PageStatus.Resume;
        for (Component c : mComponents) {
            c.onResume();
        }
    }

    @Override
    public void onPause(ILive live) {
        mPageEvent = PageStatus.Pause;
        for (Component c : mComponents) {
            c.onPause();
        }
    }

    @Override
    public void onStop(ILive live) {
        mPageEvent = PageStatus.Stop;
        for (Component c : mComponents) {
            c.onStop();
        }
    }

    @Override
    public void onDestroy(ILive live) {
        mPageEvent = PageStatus.Destroy;
        for (Component c : mComponents) {
            c.onDestroy();
        }
    }
}
