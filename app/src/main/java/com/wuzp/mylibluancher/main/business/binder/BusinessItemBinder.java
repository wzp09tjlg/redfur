package com.wuzp.mylibluancher.main.business.binder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
public class BusinessItemBinder extends ItemBinder<BusinessModel, BusinessItemBinder.ViewHolder> {

    @Override
    public ViewHolder create(LayoutInflater inflater, ViewGroup parent) {
        View view = inflater.inflate(R.layout.item_business, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void bind(ViewHolder holder, BusinessModel item) {
        //holder.mBusinessPic.
        holder.mBusinessItemName.setText(item.name);
    }

    @Override
    public Class<BusinessModel> bindDataType() {
        return BusinessModel.class;
    }

    static class ViewHolder extends ItemViewHolder<BusinessModel> {

        @BindView(R2.id.iv_business_pic)
        ImageView mBusinessPic;
        @BindView(R2.id.tv_business_item_name)
        TextView mBusinessItemName;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
