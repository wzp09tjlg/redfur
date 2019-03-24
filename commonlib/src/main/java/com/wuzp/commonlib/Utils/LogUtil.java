package com.wuzp.commonlib.Utils;

import android.content.Context;
import android.util.Log;

/**
 * 常用的日志util类
 * 需要写哪些方法
 * d e i w
 *
 * @author wuzp
 */
public class LogUtil {

    private static final boolean DEBUG = true;
    private static final String TAG = "COMMON";

    public static void d(String msg){
        if(DEBUG){
            Log.d(TAG,msg);
        }
    }

    public static void d(String tag,String msg){
        if(DEBUG){
            Log.d(tag, msg);
        }
    }

    public static void e(String msg){
        if(DEBUG){
            Log.e(TAG,msg);
        }
    }

    public static void e(String tag,String msg){
        if(DEBUG){
            Log.e(tag, msg);
        }
    }

    public static void i(String msg){
        if(DEBUG){
            Log.i(TAG,msg);
        }
    }

    public static void i(String tag,String msg){
        if(DEBUG){
            Log.i(tag, msg);
        }
    }

    public static void w(String msg){
        if(DEBUG){
            Log.w(TAG,msg);
        }
    }

    public static void w(String tag,String msg){
        if(DEBUG){
            Log.w(tag, msg);
        }
    }
}
