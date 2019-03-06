package com.wuzp.rvlib.recyclerview.binder.mvp;

/**
 * RecyclerView 生命周期观察者
 *
 * @author zhengtongyu (zhengtongyu@didichuxing.com)
 * @date 2018/9/12
 */
public interface RvLifecycleObserver {

    /**
     * RecyclerView 可见
     */
    void onAttach();

    /**
     * RecyclerView 不可见
     */
    void onDetach();

    /**
     * RecyclerView 被销毁
     */
    void onDestroy();
}
