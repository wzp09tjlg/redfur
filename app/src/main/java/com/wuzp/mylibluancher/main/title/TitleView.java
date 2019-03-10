package com.wuzp.mylibluancher.main.title;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.wuzp.mylibluancher.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author wuzhenpeng03
 */
public class TitleView extends Contract.AbsTitleView {

    @BindView(R.id.fl_back_area)
    FrameLayout mBackArea;
    @BindView(R.id.tv_title_center)
    TextView mTitle;
    @BindView(R.id.fl_menu_area)
    FrameLayout mMenuArea;

    @NonNull
    @Override
    protected View inflateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        View rootView = inflater.inflate(R.layout.component_title, container, true);
        ButterKnife.bind(this, rootView);
        return rootView;
    }


    @OnClick(R.id.fl_back_area)
    @Override
    public void onBackClicked() {
        getPresenter().onBack();
    }

    @OnClick(R.id.fl_menu_area)
    @Override
    public void onMenuClicked() {
        getPresenter().onMenu();
    }
}
