package com.wuzp.mylibluancher.main.business;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.wuzp.mylibluancher.R;
import com.wuzp.mylibluancher.R2;
import com.wuzp.mylibluancher.main.business.binder.BusinessItemBinder;
import com.wuzp.mylibluancher.main.business.binder.EmptyBinder;
import com.wuzp.mylibluancher.main.business.binder.HeaderBinder;
import com.wuzp.rvlib.recyclerview.view.INovaRecyclerView;
import com.wuzp.rvlib.recyclerview.view.NovaRecyclerView;

/**
 * @author wuzhenpeng03
 */
public class BusinessView extends Contract.AbsBusinessView {

    @BindView(R2.id.rv_business_contain)
    NovaRecyclerView mRecycleView;

    @NonNull
    @Override
    protected View inflateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        View itemView = inflater.inflate(R.layout.component_business, container, true);
        ButterKnife.bind(this, itemView);
        return itemView;
    }

    @Override
    protected INovaRecyclerView generateNovaRecyclerView() {
        return mRecycleView;
    }

    @Override
    protected void initItemBinders() {
        registerBinder(new EmptyBinder());
        registerBinder(new HeaderBinder());
        registerBinder(new BusinessItemBinder());
    }
}
