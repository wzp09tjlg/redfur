package com.wuzp.mylibluancher.main.menu;

import android.view.ViewGroup;
import com.wuzp.libmvp.mvp.MvpComponent;

/**
 * @author wuzhenpeng03
 */
public class MenuComponent extends MvpComponent<Contract.AbsMenuView, Contract.AbsMenuPresenter> {

    Contract.AbsMenuPresenter mPresenter;
    Contract.AbsMenuView mView;

    public MenuComponent(ViewGroup viewGroup) {
        super(viewGroup);
    }

    @Override
    protected Contract.AbsMenuView onCreateView() {
        mView = new MenuView();
        return mView;
    }

    @Override
    protected Contract.AbsMenuPresenter onCreatePresenter() {
        mPresenter = new MenuPresenter();
        return mPresenter;
    }
}
