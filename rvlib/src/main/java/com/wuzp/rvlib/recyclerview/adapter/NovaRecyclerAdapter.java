package com.wuzp.rvlib.recyclerview.adapter;

import android.support.annotation.MainThread;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.wuzp.rvlib.R;
import com.wuzp.rvlib.recyclerview.binder.ItemBinder;
import com.wuzp.rvlib.recyclerview.binder.ItemViewHolder;
import com.wuzp.rvlib.recyclerview.data.BaseDataManager;
import com.wuzp.rvlib.recyclerview.util.RecyclerDataParser;

import java.util.ArrayList;
import java.util.List;

/**
 * 可以在一个 RecycleView 中显示多种视图的 RecyclerAdapter 。
 * <p>
 * Adapter 的元素数据保存在 mTotalDataManagers {@link List<BaseDataManager>}
 * 每个 {@link BaseDataManager} 都可以表示一种类型的数据，每种类型的数据都应有对应的 {@link ItemBinder} 以提供其视图。
 * mTotalDataManagers {@link List<BaseDataManager>} 和 mBinders {@link List<ItemBinder>} 配合可以支持多种视图的显示。
 * <p>
 *
 * @author https://github.com/DevAhamed/MultiViewAdapter
 */
public class NovaRecyclerAdapter extends RecyclerView.Adapter<ItemViewHolder> {

    /**
     * 所有注册的 ItemBinder 的 getColumnCount 返回值乘积
     */
    private int mBinderColumnSum = 1;

    /**
     * 全部的 DataManagers
     */
    private List<BaseDataManager> mTotalDataManagers = new ArrayList<>();
    /**
     * 底部的 DataManager，此列表中的 DataManager 相对于其他 DataManager 永远处于底部
     */
    private List<BaseDataManager> mFootDataManagers = new ArrayList<>();
    private List<ItemBinder> mBinders = new ArrayList<>();

    /**
     * 创建视图对应 {@link ItemViewHolder}
     *
     * @param parent   RecyclerView 实例
     * @param viewType 视图类型
     * @return ItemViewHolder 实例
     */
    @Override
    public final ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return mBinders.get(viewType).create(LayoutInflater.from(parent.getContext()), parent);
    }

    /**
     * 绑定 ViewHolder
     * <p>
     * 通过元素在 RecyclerView 中的位置获取对应元素, 并设置到 holder {@link ItemViewHolder} 中,
     * 接着通过 {@link ItemBinder} 实现数据绑定
     *
     * @param holder   ItemViewHolder 实例
     * @param position 元素在 RecyclerView 中的位置
     */
    @Override
    public final void onBindViewHolder(ItemViewHolder holder, int position) {
        ItemBinder binder = mBinders.get(holder.getItemViewType());
        holder.itemView.setTag(R.id.nova_support_nova_recycler_view_tag, holder);
        binder.bindViewHolder(holder, getItem(position), getItemPositionInDataManager(position));
    }

    /**
     * 获取所有元素数目
     * <p>
     * 将各个 {@link BaseDataManager} 子列表的数目相加, 得到总数
     *
     * @return Adapter 中所有元素数目
     */
    @Override
    public final int getItemCount() {
        int itemCount = 0;
        for (BaseDataManager dataManager : mTotalDataManagers) {
            itemCount += dataManager.getCount();
        }
        return itemCount;
    }

    /**
     * 获取视图类型
     * <p>
     * 通过 {@link RecyclerDataParser#getBinderForPosition(List, List, int)} 获取指定位置元素对应的 {@link ItemBinder}
     * 根据元素对应的 {@link ItemBinder} 获取视图类型
     *
     * @param position 元素在 RecyclerView 中的位置
     * @return 视图类型
     */
    @Override
    public final int getItemViewType(int position) {
        ItemBinder binder = getBinderForPosition(position);
        if (binder != null) {
            return mBinders.indexOf(binder);
        }
        return super.getItemViewType(position);
    }

    /**
     * 添加一种 {@link BaseDataManager} , 可用于增加 RecyclerView 上显示的数据
     * <p>
     * 注意, 注册是有先后顺序的, 在 RecyclerView 的展示时先注册的先展示
     * 添加多个 {@link BaseDataManager} 时建议使用 {@link #addDataManagers}
     *
     * @param dataManager
     */
    @MainThread
    public final void addDataManager(BaseDataManager dataManager) {
        mTotalDataManagers.add(normalDataManagerInsertPosition(), dataManager);
        notifyDataSetChanged();
    }

    /**
     * 添加多种 {@link BaseDataManager}
     *
     * @param dataManagers
     */
    @MainThread
    public final void addDataManagers(BaseDataManager... dataManagers) {
        for (BaseDataManager dataManager : dataManagers) {
            mTotalDataManagers.add(normalDataManagerInsertPosition(), dataManager);
        }
        notifyDataSetChanged();
    }

    @MainThread
    public final void addDataManagers(List<BaseDataManager> dataManagers) {
        mTotalDataManagers.addAll(normalDataManagerInsertPosition(), dataManagers);
        notifyDataSetChanged();
    }

    @MainThread
    public final void addDataManagersWithoutNotify(List<BaseDataManager> dataManagers) {
        mTotalDataManagers.addAll(normalDataManagerInsertPosition(), dataManagers);
    }

    /**
     * @return 正常的 DataManager （非底部 DataManager ）的插入位置
     */
    private int normalDataManagerInsertPosition() {
        return mTotalDataManagers.size() - mFootDataManagers.size();
    }

    /**
     * 添加位置处于底部的 DataManager
     *
     * @param dataManager 位置处于底部的 DataManager
     */
    @MainThread
    public final void addFootDataManager(BaseDataManager dataManager) {
        mTotalDataManagers.add(dataManager);
        mFootDataManagers.add(dataManager);
        notifyDataSetChanged();
    }

    /**
     * 清除 RecyclerView 展示的数据
     */
    @MainThread
    public final void clearDataManagers() {
        mTotalDataManagers.clear();
        mFootDataManagers.clear();
        notifyDataSetChanged();
    }

    /**
     * 注册一种 {@link ItemBinder}
     * <p>
     * 如果 RecyclerAdapter 已经与 {@link INovaLayoutManager} 建立联系，则属于逻辑异常，目的是为了保证网格布局的正常展示，并且规范
     * 代码编写
     *
     * @param itemBinder 被注册的 ItemBinder
     */
    @MainThread
    public final void registerBinder(ItemBinder itemBinder) {
        if (mBinders.contains(itemBinder)) {
            return;
        }
        mBinders.add(itemBinder);
    }

    ///////////////////////////////////////////
    /////////// Internal API ahead. ///////////
    ///////////////////////////////////////////

    /**
     * 获取注册的 Binder 列表
     *
     * @return 注册的 Binder 列表
     */
    public List<ItemBinder> getRegisteredBinderList() {
        return mBinders;
    }

    /**
     * 计算 {@link #mBinders} 中所有 ItemBinder 的 getColumnCount 返回值乘积，用于和 {@link #mSpanSizeLookup} 配合实现网格布局
     *
     * @return {@link #mBinders} 中所有 ItemBinder 的 getColumnCount 返回值乘积
     */
    private int calculateBinderColumnSum() {
        int columnSum = 1;
        for (ItemBinder itemBinder : mBinders) {
            columnSum *= itemBinder.getColumnCount();
        }
        return columnSum;
    }

    /**
     * 返回网格布局的 LayoutManger 需要的参数 SpanSizeLookup
     */
    public final GridLayoutManager.SpanSizeLookup getSpanSizeLookup() {
        return mSpanSizeLookup;
    }

    /**
     * 返回网格布局的 LayoutManger 需要的参数 SpanCount
     */
    public final int getSpanCount() {
        mBinderColumnSum = calculateBinderColumnSum();
        return mBinderColumnSum;
    }

    /**
     * 将当前位置的数据移动到指定位置
     *
     * @param currentPosition 当前位置
     * @param targetPosition  目标位置
     */
    public void moveData(int currentPosition, int targetPosition) {
        if (currentPosition < 0 || targetPosition < 0) {
            return;
        }
        BaseDataManager dataManager = getDataManager(currentPosition);
        BaseDataManager targetDataManager = getDataManager(targetPosition);
        if (dataManager != targetDataManager) {
            return;
        }
        int currentPositionInManager = getItemPositionInDataManager(currentPosition);
        int targetPositionInManager = getItemPositionInDataManager(targetPosition);
        if (dataManager.equals(targetDataManager)) {
            dataManager.move(currentPositionInManager, targetPositionInManager);
        }
    }

    public <T> T getItem(int positionInRV) {
        return RecyclerDataParser.getItemForPosition(mTotalDataManagers, positionInRV);
    }

    public <T extends BaseDataManager> T getDataManager(int positionInRV) {
        return RecyclerDataParser.getDataManagerForPosition(mTotalDataManagers, positionInRV);
    }

    public ItemBinder getBinderForPosition(int positionInRV) {
        return RecyclerDataParser.getBinderForPosition(mTotalDataManagers, mBinders, positionInRV);
    }

    public int getItemPositionInDataManager(int positionInRV) {
        return RecyclerDataParser.getPositionInDataManager(mTotalDataManagers, positionInRV);
    }

    public int getItemPositionForItem(Object item) {
        return RecyclerDataParser.getPositionForItem(mTotalDataManagers, item);
    }

    @MainThread
    public final void notifyBinderItemRangeChanged(BaseDataManager dataManager, int positionStart, int itemCount,
        Object payload) {
        notifyItemRangeChanged(RecyclerDataParser.getItemPositionInRV(mTotalDataManagers, dataManager, positionStart),
            itemCount, payload);
    }

    @MainThread
    public final void notifyBinderItemMoved(BaseDataManager dataManager, int fromPosition, int toPosition) {
        notifyItemMoved(RecyclerDataParser.getItemPositionInRV(mTotalDataManagers, dataManager, fromPosition),
            RecyclerDataParser.getItemPositionInRV(mTotalDataManagers, dataManager, toPosition));
    }

    @MainThread
    public final void notifyBinderItemRangeInserted(BaseDataManager dataManager, int positionStart, int itemCount) {
        notifyItemRangeInserted(RecyclerDataParser.getItemPositionInRV(mTotalDataManagers, dataManager, positionStart),
            itemCount);
    }

    @MainThread
    public final void notifyBinderItemRangeRemoved(BaseDataManager dataManager, int positionStart, int itemCount) {
        notifyItemRangeRemoved(RecyclerDataParser.getItemPositionInRV(mTotalDataManagers, dataManager, positionStart),
            itemCount);
    }

    private final GridLayoutManager.SpanSizeLookup mSpanSizeLookup = new GridLayoutManager.SpanSizeLookup() {
        @Override
        public int getSpanSize(int position) {
            return mBinderColumnSum / getBinderForPosition(position).getColumnCount();
        }
    };
}
