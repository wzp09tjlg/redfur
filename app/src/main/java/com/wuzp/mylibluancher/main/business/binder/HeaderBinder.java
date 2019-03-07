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
public class HeaderBinder extends ItemBinder<HeaderModel, HeaderBinder.ViewHolder> {

    @Override
    public ViewHolder create(LayoutInflater inflater, ViewGroup parent) {
        View itemView = inflater.inflate(R.layout.item_header, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void bind(ViewHolder holder, HeaderModel item) {
        holder.mHeaderName.setText(item.headerName);
    }

    @Override
    public Class<HeaderModel> bindDataType() {
        return HeaderModel.class;
    }

    static class ViewHolder extends ItemViewHolder<HeaderModel> {

        @BindView(R2.id.tv_header_name)
        TextView mHeaderName;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
