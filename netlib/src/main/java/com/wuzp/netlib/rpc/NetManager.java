package com.wuzp.netlib.rpc;

/**
 * 正常使用retrofit的方式是
 * 定义接口 baseService
 * 创建retrofit对象，和真实完成http请求的okhttpclint,
 * 创建baseService的实例
 * 最后就可以使用了
 *
 * @author wuzhenpeng03
 */
public class NetManager<T> {

    private NetManager manager;

    private NetManager() {
    }

    public T createService(Class<T> clazz) {
        return RetrofitHelper.getRetrofit().create(clazz);
    }

}
