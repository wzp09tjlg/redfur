package com.wuzp.mylibluancher.main.menu;

import com.wuzp.libmvp.mvp.recycler.BaseRecyclerPresenter;
import com.wuzp.libmvp.mvp.recycler.BaseRecyclerView;

/**
 * @author wuzhenpeng03
 */
public interface Contract {

    abstract class AbsMenuPresenter extends BaseRecyclerPresenter<AbsMenuView> {

    }

    abstract class AbsMenuView extends BaseRecyclerView<AbsMenuPresenter> {

    }
}
