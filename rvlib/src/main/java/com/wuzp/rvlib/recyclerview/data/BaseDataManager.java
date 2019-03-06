package com.wuzp.rvlib.recyclerview.data;

import android.support.v7.util.ListUpdateCallback;
import com.wuzp.rvlib.recyclerview.adapter.NovaRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wuzhenpeng03
 */
public class BaseDataManager<T> implements ListUpdateCallback {
    private final NovaRecyclerAdapter mAdapter;
    protected List<T> mDataList = new ArrayList<>();

    public BaseDataManager(NovaRecyclerAdapter adapter) {
        mAdapter = adapter;
    }

    /**
     * 将指定数据移动到指定位置
     *
     * @param currentPosition 指定数据当前位置
     * @param targetPosition  指定数据将要被移动到的位置
     */
    public void move(int currentPosition, int targetPosition) {
        T item = mDataList.get(currentPosition);
        mDataList.remove(currentPosition);
        mDataList.add(targetPosition, item);
        onMoved(currentPosition, targetPosition);
    }

    public final int getCount() {
        return mDataList.size();
    }

    public final T get(int index) {
        return mDataList.get(index);
    }

    public int size() {
        return mDataList.size();
    }

    ///////////////////////////////////////////
    /////////// Internal API ahead. ///////////
    ///////////////////////////////////////////

    @Override
    public void onInserted(int position, int count) {
        mAdapter.notifyBinderItemRangeInserted(this, position, count);
    }

    @Override
    public void onRemoved(int position, int count) {
        mAdapter.notifyBinderItemRangeRemoved(this, position, count);
    }

    @Override
    public void onMoved(int fromPosition, int toPosition) {
        mAdapter.notifyBinderItemMoved(this, fromPosition, toPosition);
    }

    @Override
    public void onChanged(int position, int count, Object payload) {
        mAdapter.notifyBinderItemRangeChanged(this, position, count, payload);
    }
}
