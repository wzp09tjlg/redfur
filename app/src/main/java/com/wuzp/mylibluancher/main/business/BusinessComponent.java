package com.wuzp.mylibluancher.main.business;

import android.view.ViewGroup;
import com.wuzp.libmvp.mvp.MvpComponent;

/**
 * @author wuzhenpeng03
 */
public class BusinessComponent extends MvpComponent<Contract.AbsBusinessView, Contract.AbsBusinessPresenter> {

    Contract.AbsBusinessView mView;
    Contract.AbsBusinessPresenter mPresenter;

    public BusinessComponent(ViewGroup viewGroup) {
        super(viewGroup);
    }

    @Override
    protected Contract.AbsBusinessView onCreateView() {
        mView = new BusinessView();
        return mView;
    }

    @Override
    protected Contract.AbsBusinessPresenter onCreatePresenter() {
        mPresenter = new BusinessPresenter();
        return mPresenter;
    }
}
