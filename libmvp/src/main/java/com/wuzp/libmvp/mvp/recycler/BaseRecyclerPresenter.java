package com.wuzp.libmvp.mvp.recycler;

import android.support.annotation.CallSuper;
import android.support.annotation.MainThread;
import com.wuzp.libmvp.mvp.IPresenter;
import com.wuzp.rvlib.recyclerview.adapter.NovaRecyclerAdapter;
import com.wuzp.rvlib.recyclerview.data.BaseDataManager;
import com.wuzp.rvlib.recyclerview.data.ChildDataItemManager;
import com.wuzp.rvlib.recyclerview.data.ChildDataListManager;

import java.util.List;

/**
 * 封装对 NovaRecyclerView 显示的数据的控制操作，以及生成指定类型 DataManager 的方法
 *
 * @param <V> BaseRecyclerView 实现类
 */

public abstract class BaseRecyclerPresenter<V extends BaseRecyclerView> extends IPresenter<V> {

    private NovaRecyclerAdapter mNovaRecyclerAdapter;

    @Override
    @CallSuper
    public void onCreate() {
        super.onCreate();
        mNovaRecyclerAdapter = getLogicView().getNovaRecyclerAdapter();
        initDataManagers();
    }

    /**
     * 生成空数据的 {@link ChildDataItemManager}
     *
     * @param <T> 数据类型
     * @return 空数据的 {@link ChildDataItemManager}
     */
    protected final <T> ChildDataItemManager<T> createChildDataItemManager() {
        return new ChildDataItemManager<>(mNovaRecyclerAdapter);
    }

    /**
     * 根据传入的数据生成 {@link ChildDataItemManager}
     *
     * @param item 数据
     * @param <T>  数据类型
     * @return 根据传入的数据生成的 {@link ChildDataItemManager}
     */
    protected final <T> ChildDataItemManager<T> createChildDataItemManager(T item) {
        return new ChildDataItemManager<>(mNovaRecyclerAdapter, item);
    }

    /**
     * 生成空数据的 {@link ChildDataListManager}
     *
     * @param <T> 数据类型
     * @return 空数据的 {@link ChildDataListManager}
     */
    protected final <T> ChildDataListManager<T> createChildDataListManager() {
        return new ChildDataListManager<>(mNovaRecyclerAdapter);
    }

    /**
     * 根据传入的数据生成 {@link ChildDataListManager}
     *
     * @param data 数据
     * @param <T>  数据类型
     * @return 根据传入的数据生成的 {@link ChildDataListManager}
     */
    protected final <T> ChildDataListManager<T> createChildDataListManager(List<T> data) {
        return new ChildDataListManager<>(mNovaRecyclerAdapter, data);
    }

    /**
     * 业务层实现此方法，在此方法中尽可能将当前页面所有 DataManager 添加到 RecyclerAdapter 中，接着关注于数据的变化而非
     * 关注 DataManager
     * <p>
     * 对于特殊的界面完全动态的页面如搜索结果页，可以调用下面的 {@link #addDataManager} 等方法
     */
    protected abstract void initDataManagers();

    /**
     * 为 NovaRecyclerView 增加一种显示的数据，增加多条时建议使用 {@link #addDataManagers}
     * <p>
     * 如果不是完全动态的页面，尽量在 {@link #initDataManagers} 方法中完成 DataManager 的添加
     *
     * @param dataManager
     */
    @MainThread
    public final void addDataManager(BaseDataManager dataManager) {
        mNovaRecyclerAdapter.addDataManager(dataManager);
    }

    /**
     * 为 NovaRecyclerView 增加多种显示的数据
     * <p>
     * 如果不是完全动态的页面，尽量在 {@link #initDataManagers} 方法中完成 DataManager 的添加
     *
     * @param dataManagers
     */
    @MainThread
    public final void addDataManagers(BaseDataManager... dataManagers) {
        mNovaRecyclerAdapter.addDataManagers(dataManagers);
    }

    /**
     * 为 NovaRecyclerView 增加多种显示的数据
     * <p>
     * 如果不是完全动态的页面，尽量在 {@link #initDataManagers} 方法中完成 DataManager 的添加
     *
     * @param dataManagers
     */
    @MainThread
    public final void addDataManagers(List<BaseDataManager> dataManagers) {
        mNovaRecyclerAdapter.addDataManagers(dataManagers);
    }

    /**
     * 添加位置处于底部的 DataManager
     * <p>
     * 如果不是完全动态的页面，尽量在 {@link #initDataManagers} 方法中完成 DataManager 的添加
     *
     * @param dataManager
     */
    @MainThread
    public final void addFootDataManager(BaseDataManager dataManager) {
        mNovaRecyclerAdapter.addFootDataManager(dataManager);
    }

    /**
     * 清除 NovaRecyclerView 显示的数据
     * <p>
     * 开发者应该关注于 DataManager 中数据的变化而非关注 DataManager 本身
     * 对于不希望显示的数据，将对应的 DataManager 中的数据置空即可
     * <p>
     * 空数据的 DataManager 完全不会应用影响性能
     */
    @MainThread
    public final void clearDataManagers() {
        mNovaRecyclerAdapter.clearDataManagers();
    }

    public final <T> T getItem(int positionInRV) {
        return mNovaRecyclerAdapter.getItem(positionInRV);
    }
}
