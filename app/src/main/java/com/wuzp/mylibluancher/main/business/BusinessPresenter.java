package com.wuzp.mylibluancher.main.business;

import com.wuzp.mylibluancher.main.business.binder.EmptyModel;
import com.wuzp.rvlib.recyclerview.data.BaseDataManager;
import com.wuzp.rvlib.recyclerview.data.ChildDataItemManager;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wuzhenpeng03
 */
public class BusinessPresenter extends Contract.AbsBusinessPresenter {

    List<BaseDataManager> mDataManagerList = new ArrayList<>();
    ChildDataItemManager<EmptyModel> mEmptyDataManager;


    @Override
    protected void initDataManagers() {
        mEmptyDataManager = createChildDataItemManager();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        getData();
    }

    private void getData() {
        mEmptyDataManager.setItem(new EmptyModel());
    }
}
