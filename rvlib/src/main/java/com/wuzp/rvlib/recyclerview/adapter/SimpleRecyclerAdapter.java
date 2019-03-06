package com.wuzp.rvlib.recyclerview.adapter;

import com.wuzp.rvlib.recyclerview.binder.ItemBinder;
import com.wuzp.rvlib.recyclerview.data.ChildDataListManager;

import java.util.List;

/**
 * 提供一个简单的只显示一种类型数据的 {@link NovaRecyclerAdapter} 实现
 *
 * @param <T> 在 RecyclerView 上显示的数据的类型
 * @param <B> 可以显示 T 类型数据的 {@link ItemBinder} 的实例
 * @author https://github.com/DevAhamed/MultiViewAdapter
 */
public final class SimpleRecyclerAdapter<T, B extends ItemBinder> extends NovaRecyclerAdapter {

    private final ChildDataListManager<T> mDataManager = new ChildDataListManager<>(this);

    public SimpleRecyclerAdapter() {
        addDataManager(mDataManager);
    }

    public SimpleRecyclerAdapter(B binder) {
        addDataManager(mDataManager);
        registerBinder(binder);
    }

    /**
     * 获取 {@link SimpleRecyclerAdapter} 中使用的 {@link ChildDataListManager}
     *
     * @return DataListManager
     */
    public final ChildDataListManager<T> getDataManager() {
        return mDataManager;
    }

    /**
     * 设置需要在 {@link SimpleRecyclerAdapter} 对应的 RecyclerView 上显示的数据
     *
     * @param dataList 设置的数据列表
     */
    public final void setData(List<T> dataList) {
        mDataManager.set(dataList);
    }

    /**
     * 增加需要在 {@link SimpleRecyclerAdapter} 对应的 RecyclerView 上显示的数据
     *
     * @param dataList 添加的数据列表
     */
    public void addData(List<T> dataList) {
        mDataManager.addAll(dataList);
    }
}
