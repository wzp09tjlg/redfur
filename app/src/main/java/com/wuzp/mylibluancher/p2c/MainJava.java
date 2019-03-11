package com.wuzp.mylibluancher.p2c;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 这个类是java类，是尝试生产者和消费者之间的关系的测试列子
 *
 * @author wuzhenpeng03
 */
public class MainJava {

    private ReentrantLock mLock = new ReentrantLock();
    private Condition mCCondition = mLock.newCondition();
    private Condition mPCondition = mLock.newCondition();

    Consumer consumer = new Consumer();
    Product product = new Product();

    public void testP2C() {
        product.start();
        consumer.start();
    }

    //消费者
    class Consumer extends Thread {

        @Override
        public void run() {

            while (true) {

                mLock.lock();
                try {
                    show("consumer");
                    mCCondition.await();
                    mPCondition.signalAll();
                } catch (Exception e) {

                }
                {
                    mLock.unlock();
                }
            }
        }
    }

    //生产者
    class Product extends Thread {

        @Override
        public void run() {
            while (true) {
                mLock.lock();

                try {
                    show("product");
                    mCCondition.signalAll();
                    mPCondition.await();
                } catch (Exception e) {

                }
                {
                    mLock.unlock();
                }
            }
        }
    }

    private static void show(String msg) {
        System.out.println("msg:" + msg);
    }

}
