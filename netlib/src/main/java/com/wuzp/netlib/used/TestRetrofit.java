package com.wuzp.netlib.used;

import com.wuzp.netlib.rpc.BaseService;
import com.wuzp.netlib.rpc.NetFactory;

/**
 * @author wuzhenpeng03
 */
public class TestRetrofit {


    public void doSome(){
        NetFactory.getRpcService(BaseService.class,"");//.postUser("");
    }
}
