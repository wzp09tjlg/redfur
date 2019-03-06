package com.wuzp.rvlib.recyclerview.data;

import com.wuzp.rvlib.recyclerview.adapter.NovaRecyclerAdapter;

/**
 * @author wuzhenpeng03
 */
public final class ChildDataItemManager<T> extends BaseDataManager<T> {

    public ChildDataItemManager(NovaRecyclerAdapter adapter) {
        super(adapter);
    }

    public ChildDataItemManager(NovaRecyclerAdapter adapter, T item) {
        super(adapter);
        if (item == null) {
            throw new IllegalStateException("item should not be null ");
        }
        mDataList.add(item);
    }

    public void setItem(T item) {
        if (item == null) {
            throw new IllegalStateException("item should not be null ");
        }
        if (mDataList.size() == 0) {
            mDataList.add(item);
            onInserted(0, 1);
        } else {
            mDataList.set(0, item);
            onChanged(0, 1, null);
        }
    }

    public void removeItem() {
        if (mDataList.size() > 0) {
            mDataList.clear();
            onRemoved(0, 1);
        }
    }
}
