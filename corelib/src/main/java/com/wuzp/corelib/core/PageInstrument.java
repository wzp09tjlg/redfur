package com.wuzp.corelib.core;

import java.util.List;

/**
 * Instrument 用于处理 page 跳转工具类
 * <p>
 * Instrument 维护一个 stack，管理一系列的 Page
 *
 */
public interface PageInstrument {

//    /**
//     * Instrument 下所有的 Page 将共享一个 titleBar.
//     *
//     * @param titleBar title bar for page.
//     */
//    void attachTitleBar(TitleBar titleBar);

    /**
     * 指定所有 Page 共享的类 Dialog 弹层显示区域
     * @param frame viewGroup
     */
//    void attachDialogFrame(@NonNull DialogFrameLayout frame);

//    DialogInstrument getDialogInstrument();

    /**
     * @return true Instrument 已包含指定的 root Page
     */
    boolean hasRootPage();

    /**
     * Root Page 会将已存在于 stack 中的 Page 先清除
     *
     * @param
     */
    void setRootPage();

    /**
     * @return 当前 Instrument 关联的 RootPage
     */
    void getRootPage();

    /**
     * @param
     */
    void pushPage();

    /**
     * 当前 page 出栈
     */
    void pop();

    /**
     * 出栈并清空栈
     */
    void popToRoot();

    /**
     * 出栈到指定页面
     * @param page 栈中必须要包含这个页面, 如果栈中没有这个页面, 不做任何操作
     */
    void popToPage(Class<?> page);

    /**
     * 判断回退栈中是否有当前页面
     * @param page
     * @return true 有, false 无
     */
    boolean containsPageInBackStack(Class<?> page);

    /**
     * @return true 回退
     */
    boolean handleBack();

    /**
     * @return size 回退栈中 Page 的总数
     */
    int getBackstackSize();

    /**
     * 注册全局 Page 生命周期监听回调
     *
     * @param pageLifecycleCallback
     */
    void registerPageLifecycleCallback(IScopeLifecycle pageLifecycleCallback);

    /**
     * 移除已注册全局 Page 生命周期监听回调
     *
     * @param pageLifecycleCallback
     */
    void unregisterPageLifecycleCallback(IScopeLifecycle pageLifecycleCallback);

    /**
     * @return 已注册的 Page 生命周期回调接口
     */
    List<IScopeLifecycle> getRegisteredPageLifecycleCallbacks();
}
