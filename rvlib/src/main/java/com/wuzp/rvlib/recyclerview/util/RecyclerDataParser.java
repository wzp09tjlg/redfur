package com.wuzp.rvlib.recyclerview.util;

import com.wuzp.rvlib.recyclerview.binder.ItemBinder;
import com.wuzp.rvlib.recyclerview.data.BaseDataManager;

import java.util.List;

/**
 * //////////////////////////////////Internal Class//////////////////////////////////
 * <p>
 * 提供解析 RecyclerView 相关数据的方法
 */

public class RecyclerDataParser {

    /**
     * 传入元素在列表中的位置, 获得元素所属的 DataManager
     *
     * @param dataManagers
     * @param positionInRV
     * @return
     */
    public static <T extends BaseDataManager> T getDataManagerForPosition(List<BaseDataManager> dataManagers,
        int positionInRV) {
        int itemCount;
        int positionInDataManager = positionInRV;
        for (BaseDataManager dataManager : dataManagers) {
            itemCount = dataManager.getCount();
            if (positionInDataManager < itemCount) {
                return (T) dataManager;
            }
            positionInDataManager -= itemCount;
        }
        throw new IllegalStateException("Invalid position for DataManager!");
    }

    /**
     * 传入元素在列表中的位置, 获得该元素
     *
     * @param dataManagers
     * @param positionInRV
     * @return
     */
    public static <T> T getItemForPosition(List<BaseDataManager> dataManagers, int positionInRV) {
        int itemCount;
        int positionInDataManager = positionInRV;
        for (BaseDataManager dataManager : dataManagers) {
            itemCount = dataManager.getCount();
            if (positionInDataManager < itemCount) {
                return (T) dataManager.get(positionInDataManager);
            }
            positionInDataManager -= itemCount;
        }
        throw new IllegalStateException("Item not found for position. position = " + positionInRV);
    }

    /**
     * 传入元素在列表中的位置, 获得元素在所属 DataManager 中的位置
     *
     * @param dataManagers
     * @param positionInRV
     * @return
     */
    public static int getPositionInDataManager(List<BaseDataManager> dataManagers, int positionInRV) {
        int itemCount;
        int positionInDataManager = positionInRV;
        for (BaseDataManager dataManager : dataManagers) {
            itemCount = dataManager.getCount();
            if (positionInDataManager < itemCount) {
                break;
            }
            positionInDataManager -= itemCount;
        }
        return positionInDataManager;
    }

    /**
     * 传入元素在所属 DataManager 中的位置, 获得元素在列表中的位置
     *
     * @param dataManagers
     * @param dataManager
     * @param positionInDataManager
     * @return
     */
    public static int getItemPositionInRV(List<BaseDataManager> dataManagers, BaseDataManager dataManager,
        int positionInDataManager) {
        int dataManagerIndex = dataManagers.indexOf(dataManager);
        if (dataManagerIndex < 0) {
            throw new IllegalStateException("BaseDataManager does not exist in adapter");
        }
        int positionInRV = positionInDataManager;
        for (int i = 0; i < dataManagerIndex; i++) {
            positionInRV += dataManagers.get(i).getCount();
        }
        return positionInRV;
    }

    /**
     * 传入元素在列表中的位置, 获得可以与元素绑定的 ItemBinder
     * <p>
     * 遍历 {@link ItemBinder} 列表，通过 {@link ItemBinder#canBindData} 方法找出可以与元素匹配的 {@link ItemBinder}
     *
     * @param dataManagers
     * @param binders
     * @param positionInRV
     * @return
     */
    public static ItemBinder getBinderForPosition(List<BaseDataManager> dataManagers, List<ItemBinder> binders,
        int positionInRV) {
        Object item = getItemForPosition(dataManagers, positionInRV);
        for (ItemBinder binder : binders) {
            if (binder.canBindData(item)) {
                return binder;
            }
        }
        throw new IllegalStateException("Binder not found for position. object = " + item);
    }

    /**
     * 传入指定元素，获取元素在列表中的位置
     *
     * @param dataManagers
     * @param item
     * @return
     */
    public static int getPositionForItem(List<BaseDataManager> dataManagers, Object item) {
        int positionInRV = 0;
        for (BaseDataManager dataManager : dataManagers) {
            int count = dataManager.getCount();
            for (int i = 0; i < count; i++) {
                if (dataManager.get(i).equals(item)) {
                    return positionInRV;
                }
                positionInRV++;
            }
        }

        return -1;
    }
}
