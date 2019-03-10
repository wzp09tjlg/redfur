package com.wuzp.mylibluancher.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.wuzp.mylibluancher.R;
import com.wuzp.mylibluancher.base.BaseActivity;
import com.wuzp.mylibluancher.main.menu.MenuComponent;
import com.wuzp.mylibluancher.main.title.TitleComponent;

/**
 * 真正业务层的activity
 * <p>
 * 可以承载众多业务模块的activity
 *
 * @author wuzhenpeng03
 */
public class BusinessActivity extends BaseActivity {

    @BindView(R.id.fl_title_bar_contain)
    FrameLayout mTitleComponent;
    @BindView(R.id.fl_menu_contain)
    FrameLayout mMenuComponent;
    @BindView(R.id.fl_business_contain)
    FrameLayout mBusinessComponent;

    @Override
    protected void onAfterCreate(@Nullable Bundle savedInstanceState) {
        super.onAfterCreate(savedInstanceState);
        LayoutInflater inflater = LayoutInflater.from(this);
        View rootView = inflater.inflate(R.layout.activity_business, null, false);
        ButterKnife.bind(this, rootView);
        setContentView(rootView);
    }

    @Override
    protected void setupComponents() {
        super.setupComponents();
        TitleComponent titleComponent = new TitleComponent(mTitleComponent);
        MenuComponent menuComponent = new MenuComponent(mMenuComponent);

        addComponent(titleComponent);
        addComponent(menuComponent);
    }

}
