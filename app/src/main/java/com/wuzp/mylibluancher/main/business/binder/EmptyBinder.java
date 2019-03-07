package com.wuzp.mylibluancher.main.business.binder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.wuzp.mylibluancher.R;
import com.wuzp.mylibluancher.R2;
import com.wuzp.rvlib.recyclerview.binder.ItemBinder;
import com.wuzp.rvlib.recyclerview.binder.ItemViewHolder;

/**
 * @author wuzhenpeng03
 */
public class EmptyBinder extends ItemBinder<EmptyModel, EmptyBinder.ViewHolder> {

    @Override
    public ViewHolder create(LayoutInflater inflater, ViewGroup parent) {
        View view = inflater.inflate(R.layout.item_business_empty, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void bind(ViewHolder holder, EmptyModel item) {

    }

    @Override
    public Class<EmptyModel> bindDataType() {
        return EmptyModel.class;
    }

    static class ViewHolder extends ItemViewHolder<EmptyModel> {

        @BindView(R2.id.tv_empty_name)
        TextView mEmptyName;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this,view);
        }
    }
}
