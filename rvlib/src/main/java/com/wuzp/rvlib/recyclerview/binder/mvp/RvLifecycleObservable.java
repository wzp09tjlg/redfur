package com.wuzp.rvlib.recyclerview.binder.mvp;

/**
 * 被观测的 RecyclerView 的生命周期
 *
 * @author zhengtongyu (zhengtongyu@didichuxing.com)
 * @date 2018/9/12
 */
public interface RvLifecycleObservable {

    /**
     * 订阅 RecyclerView 生命周期观察者，当声明周期变化时调用 {@link RvLifecycleObserver} 相应方法
     *
     * @param observer RecyclerView 生命周期观察者
     */
    void subscribe(RvLifecycleObserver observer);
}
