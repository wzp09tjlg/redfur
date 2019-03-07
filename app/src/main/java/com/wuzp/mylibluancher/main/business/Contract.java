package com.wuzp.mylibluancher.main.business;

import com.wuzp.libmvp.mvp.recycler.BaseRecyclerPresenter;
import com.wuzp.libmvp.mvp.recycler.BaseRecyclerView;

/**
 * @author wuzhenpeng03
 */
public interface Contract {

    abstract class AbsBusinessPresenter extends BaseRecyclerPresenter<AbsBusinessView> {

    }

    abstract class AbsBusinessView extends BaseRecyclerView<AbsBusinessPresenter> {

    }

}
