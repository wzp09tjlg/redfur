package com.wuzp.mylibluancher.review;


/**
 * 单列的两种表现形式
 *
 * @author wuzp
 */
public class SingleClass {

    private SingleClass() {
    }

    private static volatile SingleClass mSingleClass;

    //第一种表现形式是双重检查：原因是多线程在使用时，因为存在Java编译器的指令重排，导致没有初始化的对象直接被赋值。
    // 使用的地方可能存在使用没有被初始化的对象
    public static SingleClass getInstance() {
        if (mSingleClass == null) {
            synchronized (SingleClass.class) {
                if (mSingleClass == null) {
                    mSingleClass = new SingleClass();
                }
            }
        }
        return mSingleClass;
    }


    //第二种表现形式是静态内部类，静态内部类能够保证单列的唯一性 根本性原因时Java的双亲委派加载机制决定。类在加载过程中 是通过classloader 来加载，
    //classloader 加载能够确保只有一个线程进行。其他线程会被阻塞。
    public static class InnerHonder {
      private static final SingleClass HONDER = new SingleClass();
    }

    public static SingleClass getmSingleClass(){
        return InnerHonder.HONDER;
    }

}
