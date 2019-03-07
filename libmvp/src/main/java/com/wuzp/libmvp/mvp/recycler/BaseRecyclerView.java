package com.wuzp.libmvp.mvp.recycler;

import android.support.annotation.CallSuper;
import com.wuzp.libmvp.mvp.IView;
import com.wuzp.rvlib.recyclerview.adapter.NovaRecyclerAdapter;
import com.wuzp.rvlib.recyclerview.binder.ItemBinder;
import com.wuzp.rvlib.recyclerview.mark.ItemDraggable;
import com.wuzp.rvlib.recyclerview.view.INovaRecyclerView;
import com.wuzp.rvlib.recyclerview.view.NovaRecyclerView;
import com.wuzp.rvlib.recyclerview.view.layoutmanager.INovaLayoutManager;
import com.wuzp.rvlib.recyclerview.view.layoutmanager.NovaGridLayoutManager;
import com.wuzp.rvlib.recyclerview.view.layoutmanager.NovaLinearLayoutManager;


/**
 * 封装 NovaRecyclerView 的设置过程，可用于承担 MVP 架构中 V 模块的职责，
 * 如果需要使用 {@link INovaRecyclerView} 来显示页面，建议直接使用 BaseRecyclerView 的子类，
 * 而非单独对 {@link INovaRecyclerView} 进行封装
 *
 */
public abstract class BaseRecyclerView<P extends BaseRecyclerPresenter> extends IView<P> {

    private boolean mInitialized = false;

    private NovaRecyclerView mNovaRecyclerView;
    private NovaRecyclerAdapter mRecyclerAdapter;
    private INovaLayoutManager mNovaLayoutManager;

    @Override
    @CallSuper
    public void onCreate() {
        super.onCreate();
        initNovaRecyclerView();
    }

    @Override
    @CallSuper
    public void onPause() {
        super.onPause();
        // 布局管理器的吸顶效果向视图层级中注入了视图, 需要在切出页面时释放一下
        mNovaLayoutManager.release();
    }

    /**
     * 初始化 {@link #mNovaRecyclerView} 并为其设置 RecyclerAdapter 和 LayoutManager
     */
    private void initNovaRecyclerView() {
        INovaRecyclerView novaRecyclerView = generateNovaRecyclerView();
        if (!(novaRecyclerView instanceof NovaRecyclerView)) {
            throw new IllegalStateException("Result of generateNovaRecyclerView must be instanceof NovaRecyclerView");
        }
        mNovaRecyclerView = (NovaRecyclerView) novaRecyclerView;
        mRecyclerAdapter = new NovaRecyclerAdapter();
        initItemBinders();
        mNovaLayoutManager = generateNovaLayoutManager();
        setupNovaLayoutManager(mNovaLayoutManager);
        mNovaRecyclerView.setAdapter(mRecyclerAdapter);
        mNovaRecyclerView.setNovaLayoutManager(mNovaLayoutManager);
        setupNovaRecyclerView(mNovaRecyclerView);
        mInitialized = true;
    }

    /**
     * 业务层实现此方法以在生成 {@link INovaRecyclerView} 实例，从而 BaseRecyclerView 可以在 {@link #onCreate} 将通过此方法生成的
     * {@link INovaRecyclerView} 实例赋值给 {@link #mNovaRecyclerView} ，并完成 {@link INovaRecyclerView}
     * 的核心设置
     *
     * @return 业务层使用的 {@link INovaRecyclerView} 实例
     */
    protected abstract INovaRecyclerView generateNovaRecyclerView();

    /**
     * 业务层实现此方法以完成对 {@link ItemBinder} 的注册
     */
    protected abstract void initItemBinders();

    /**
     * @return 是否启用线性布局以优化性能
     */
    protected boolean useLinearLayout() {
        return false;
    }

    /**
     * 生成{@link #mNovaRecyclerView} 使用的 {@link INovaLayoutManager}
     * 如果需要基于 {@link INovaLayoutManager} 进行扩展可以通过覆写此方法来替换默认实现
     *
     * @return {@link #mNovaRecyclerView} 使用的 {@link INovaLayoutManager}
     */
    protected INovaLayoutManager generateNovaLayoutManager() {
        if (useLinearLayout()) {
            return new NovaLinearLayoutManager(getContext());
        }
        return new NovaGridLayoutManager(getContext());
    }

    /**
     * 覆写此方法以为 NovaLayoutManager 设置 SmoothScroller
     *
     * @param novaLayoutManager 当前使用的 RecyclerView 的 INovaLayoutManager
     */
    protected void setupNovaLayoutManager(INovaLayoutManager novaLayoutManager) {
    }

    /**
     * 覆写此方法以开启分割线，侧滑，上拉加载，ItemBinder Mvp 化这些配置选项
     *
     * @param novaRecyclerView {@link #mNovaRecyclerView}
     */
    protected void setupNovaRecyclerView(INovaRecyclerView novaRecyclerView) {
    }

    /**
     * 为 {@link #mRecyclerAdapter} 注册 ItemBinder
     * 为规范代码编写， 禁止 setAdapter 后 registerBinder。
     *
     * @param itemBinder 注册的 ItemBinder
     */
    protected final void registerBinder(ItemBinder itemBinder) {
        if (mInitialized) {
            throw new IllegalStateException("RecyclerAdapter has Attached To WrapperAdapter, disable for registerBinder"
                + ". itemBinder = " + itemBinder);
        }
        if (itemBinder instanceof ItemDraggable) {
            itemBinder.setItemDragListener(mNovaRecyclerView);
        }
        mRecyclerAdapter.registerBinder(itemBinder);
    }

    /**
     * 返回当前使用的 INovaRecyclerView 实例
     *
     * @return {@link #mNovaRecyclerView}
     */
    public final NovaRecyclerView getNovaRecyclerView() {
        return mNovaRecyclerView;
    }

    /**
     * 返回当前使用的 RecyclerAdapter
     *
     * @return {@link #mRecyclerAdapter}
     */
    final NovaRecyclerAdapter getNovaRecyclerAdapter() {
        return mRecyclerAdapter;
    }

    public final INovaLayoutManager getNovaLayoutManager() {
        return mNovaLayoutManager;
    }
}

