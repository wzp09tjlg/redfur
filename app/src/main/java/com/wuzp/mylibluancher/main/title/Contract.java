package com.wuzp.mylibluancher.main.title;

import com.wuzp.libmvp.mvp.IPresenter;
import com.wuzp.libmvp.mvp.IView;

/**
 * @author wuzhenpeng03
 */
public interface Contract {

    abstract class AbsTitlePresenter extends IPresenter<AbsTitleView> {

        /**
         * 页面返回键逻辑处理
         */
        public abstract void onBack();

        /**
         * 页面菜单键逻辑处理
         */
        public abstract void onMenu();

    }

    abstract class AbsTitleView extends IView<AbsTitlePresenter> {

        /**
         * 点击返回键
         */
        public abstract void onBackClicked();

        /**
         * 点击菜单键
         */
        public abstract void onMenuClicked();
    }
}
