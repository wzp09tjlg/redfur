package com.wuzp.corelib.core;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Each page will have a unique PageContext.
 * PageContext is created and added to page during the attach stage.
 * <p>
 * <p>
 * The pageId variable is the only identification of page, currently using the full name of the class
 * <p>
 * Ability: provide context, action page jump push, pop operation, page lifecycle
 * Lifecycle: same as page lifecycle
 * PageContext can be obtained in page, presenter, and component
 *
 */
public interface ScopeContext {

    @NonNull
    String alias();

    @NonNull
    Bundle getBundle();

    @NonNull INavigator getNavigator();

    @NonNull LiveHandler getLiveHandler();

    boolean addObserver(@NonNull IScopeLifecycle observer);

    boolean removeObserver(@NonNull IScopeLifecycle observer);

    @Nullable
    ScopeContext getParent();

    Object attach(String key, Object object);

    Object detach(String key);

    Object getObject(String key);

    void detachAll();
}
