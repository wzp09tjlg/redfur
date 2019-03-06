package com.wuzp.rvlib.recyclerview.data;

import android.support.v7.util.DiffUtil;
import com.wuzp.rvlib.recyclerview.adapter.NovaRecyclerAdapter;
import com.wuzp.rvlib.recyclerview.util.DiffUtilCallback;
import com.wuzp.rvlib.recyclerview.util.PayloadProvider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public final class ChildDataListManager<T> extends BaseDataManager<T> {
    final PayloadProvider<T> mPayloadProvider;

    public ChildDataListManager(NovaRecyclerAdapter adapter) {
        this(adapter, new ArrayList<T>());
    }

    public ChildDataListManager(NovaRecyclerAdapter adapter, List<T> dataList) {
        this(adapter, dataList, new PayloadProvider<T>() {
            @Override
            public boolean areContentsTheSame(T oldItem, T newItem) {
                return oldItem.equals(newItem);
            }

            @Override
            public Object getChangePayload(T oldItem, T newItem) {
                return null;
            }
        });
    }

    public ChildDataListManager(NovaRecyclerAdapter adapter, List<T> dataList,
        PayloadProvider<T> payloadProvider) {
        super(adapter);
        for (T item : dataList) {
            if (item == null) {
                throw new IllegalStateException("item should not be null ");
            }
        }
        mPayloadProvider = payloadProvider;
        mDataList.addAll(dataList);
    }

    public void add(T item) {
        if (item == null) {
            throw new IllegalStateException("item should not be null ");
        }
        mDataList.add(item);
        onInserted(mDataList.size() - 1, 1);
    }

    public void add(int index, T item) {
        if (item == null) {
            throw new IllegalStateException("item should not be null ");
        }
        mDataList.add(index, item);
        onInserted(index, 1);
    }

    public void addAll(Collection<? extends T> items) {
        addAll(mDataList.size(), items);
    }

    public void addAll(int index, Collection<? extends T> items) {
        for (T item : items) {
            if (item == null) {
                throw new IllegalStateException("item should not be null ");
            }
        }
        mDataList.addAll(index, items);
        onInserted(index, items.size());
    }

    public int indexOf(T item) {
        return mDataList.indexOf(item);
    }

    public void set(int index, T item) {
        if (item == null) {
            throw new IllegalStateException("item should not be null ");
        }
        mDataList.set(index, item);
        onChanged(index, 1, null);
    }

    public void set(List<T> dataList) {
        for (T item : dataList) {
            if (item == null) {
                throw new IllegalStateException("item should not be null ");
            }
        }
        if (mDataList.isEmpty()) {
            mDataList.addAll(dataList);
            onInserted(0, dataList.size());
            return;
        }

        DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtilCallback<T>(mDataList,
            dataList) {
            @Override
            public boolean areContentsTheSame(T oldItem, T newItem) {
                return mPayloadProvider.areContentsTheSame(oldItem, newItem);
            }

            @Override
            public Object getChangePayload(T oldItem, T newItem) {
                return mPayloadProvider.getChangePayload(oldItem, newItem);
            }
        });

        mDataList = new ArrayList<>(dataList);
        result.dispatchUpdatesTo(this);
    }

    public void remove(int index) {
        if (index >= size()) {
            throw new IndexOutOfBoundsException();
        }

        mDataList.remove(index);
        onRemoved(index, 1);
    }

    public void clear() {
        int size = size();
        if (size <= 0) {
            return;
        }

        mDataList.clear();
        onRemoved(0, size);
    }
}

