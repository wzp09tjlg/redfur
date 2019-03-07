package com.wuzp.corelib.core;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import com.wuzp.corelib.core.internal.ScopeContextActivityImpl;

public abstract class SkeletonActivity extends AppCompatActivity implements ILive {

    private ScopeContextBase mScopeContext;
    private ComponentGroup mComponentGroup = new ComponentGroup();

    private boolean active;

    @Override
    protected final void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mScopeContext = onCreateScopeContext();
        mScopeContext.addObserver(mComponentGroup);
        active = true;

        onAfterCreate(savedInstanceState);

        setupComponents();
        mScopeContext.onCreate(this);
    }

    @NonNull
    protected ScopeContextBase onCreateScopeContext() {
        return new ScopeContextActivityImpl(this);
    }

    protected abstract void onAfterCreate(@Nullable Bundle savedInstanceState);

    @Override
    protected void onStart() {
        super.onStart();
        active = true;
        mScopeContext.onStart(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        active = true;
        mScopeContext.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        active = false;
        mScopeContext.onPause(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        active = false;
        mScopeContext.onStop(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        active = false;
        mScopeContext.onDestroy(this);
        mScopeContext.detachAll();
        mScopeContext.removeObserver(mComponentGroup);
        mScopeContext = null;
    }

    @Override
    public boolean isActive() {
        return active;
    }

    protected void setupComponents() {
    }

    protected final boolean addComponent(Component component) {
        component.attachScopeContext(getScopeContext());
        if (!mComponentGroup.addComponent(component)) {
            component.attachScopeContext(null);
            return false;
        }
        return true;
    }

    protected final boolean removeComponent(Component component) {
        if (mComponentGroup.removeComponent(component)) {
            component.attachScopeContext(null);
            return true;
        }
        return false;
    }

    @Nullable
    public final Component getComponent(Class<? extends Component> clazz) {
        return mComponentGroup.getComponent(clazz);
    }

    @Nullable
    public ScopeContext getScopeContext() {
        return mScopeContext;
    }

    @Nullable
    public String alias() {
        return getClass().getName();
    }

}
