package com.wuzp.netlib.rpc;

import android.util.ArrayMap;

/**
 * @author wuzhenpeng03
 */
public class NetFactory {

    private static NetFactory netFactory;

    @SuppressWarnings("all")
    private ArrayMap<Class<?>, BaseService> mSodaServices = new ArrayMap<>();


    private NetFactory() {

    }

    private static NetFactory getInstance() {
        if (netFactory == null) {
            netFactory = new NetFactory();
        }
        return netFactory;
    }

    public static <T extends BaseService> T getRpcService(Class<T> clazz, String url) {
        T service = (T) getInstance().mSodaServices.get(clazz);
        if (service == null) {
            // TODO: 2019/3/12 待构造
            //创建一个可设置baseUrl的service
            //service = getInstance().mSodaRpcServiceFactory.newRpcService(clazz, url);
            getInstance().mSodaServices.put(clazz, service);
        }
        return service;
    }
}
