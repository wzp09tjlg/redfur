package com.wuzp.mylibluancher.main.menu;

import com.wuzp.mylibluancher.main.menu.binder.MenuModel;
import com.wuzp.rvlib.recyclerview.data.ChildDataListManager;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wuzhenpeng03
 */
public class MenuPresenter extends Contract.AbsMenuPresenter {

    ChildDataListManager mDataListManager;
    List<MenuModel> mMenuData;


    @Override
    protected void initDataManagers() {
        mDataListManager = createChildDataListManager();
        addDataManager(mDataListManager);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        getData();
    }

    private void getData() {
        mMenuData = new ArrayList<>();
        int testSize = 20;
        for (int i = 0; i < testSize; i++) {
            MenuModel menuModel = new MenuModel();
            menuModel.menuName = "testName" + i;
            mMenuData.add(menuModel);
        }
        mDataListManager.set(mMenuData);
    }
}
