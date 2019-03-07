package com.wuzp.mylibluancher.main.title;

import android.view.ViewGroup;
import com.wuzp.libmvp.mvp.MvpComponent;

/**
 * @author wuzhenpeng03
 */
public class TitleComponent extends MvpComponent<Contract.AbsTitleView, Contract.AbsTitlePresenter> {

    Contract.AbsTitlePresenter mPresenter;
    Contract.AbsTitleView mView;

    public TitleComponent(ViewGroup viewGroup) {
        super(viewGroup);
    }

    @Override
    protected Contract.AbsTitleView onCreateView() {
        mView = new TitleView();
        return mView;
    }

    @Override
    protected Contract.AbsTitlePresenter onCreatePresenter() {
        mPresenter = new TitlePresenter();
        return mPresenter;
    }
}
