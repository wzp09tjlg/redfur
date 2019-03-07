package com.wuzp.mylibluancher.main.menu.binder;

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
public class MenuBinder extends ItemBinder<MenuModel, MenuBinder.ViewHolder> {

    @Override
    public ViewHolder create(LayoutInflater inflater, ViewGroup parent) {
        View itemView = inflater.inflate(R.layout.item_menu, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void bind(ViewHolder holder, MenuModel item) {
        holder.mMenuName.setText(item.menuName);
    }

    @Override
    public Class<MenuModel> bindDataType() {
        return MenuModel.class;
    }

    static class ViewHolder extends ItemViewHolder<MenuModel> {

        @BindView(R2.id.tv_menu_name)
        TextView mMenuName;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

}
