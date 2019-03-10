package com.wuzp.mylibluancher.main.menu;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wuzp.mylibluancher.R;
import com.wuzp.mylibluancher.main.menu.binder.MenuBinder;
import com.wuzp.rvlib.recyclerview.view.INovaRecyclerView;
import com.wuzp.rvlib.recyclerview.view.NovaRecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author wuzhenpeng03
 */
public class MenuView extends Contract.AbsMenuView {

    @BindView(R.id.rv_side_menu)
    NovaRecyclerView mRecyclerView;

    @NonNull
    @Override
    protected View inflateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        View rootView = inflater.inflate(R.layout.component_side_menu, container, true);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    protected INovaRecyclerView generateNovaRecyclerView() {
        return mRecyclerView;
    }

    @Override
    protected void initItemBinders() {
        registerBinder(new MenuBinder());
    }
}
