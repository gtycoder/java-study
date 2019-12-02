package com.gty.testjucutil;

import java.util.concurrent.locks.LockSupport;

/**
 * 使用condition模拟生产者和消费者
 * 测试LockSupper阻塞原语
 */
public class TestLockSupper {
/*    //这是库存
    static int num = 0;
    //private AtomicInteger atomicInteger = new AtomicInteger(0);
    static ReentrantLock reentrantLock = new ReentrantLock();

    static Condition productCondition = reentrantLock.newCondition();
    static Condition consumerCondition = reentrantLock.newCondition();

    static class Product implements Runnable {
        @Override
        public void run() {
            reentrantLock.lock();
            try {
                while (num < 10) {
                    num++;
                    try {
                        TimeUnit.MILLISECONDS.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("生产了一个"+num);
                }

                *//*try {
                    productCondition.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }*//*

                System.out.println("productCondition.await()之后");

                consumerCondition.signal();

            } finally {
                reentrantLock.unlock();
            }
        }
    }

    static class Consumer implements Runnable {
        @Override
        public void run() {
            reentrantLock.lock();
            try {

                try {
                    consumerCondition.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                while (num > 1) {
                    num--;
                    try {
                        TimeUnit.MILLISECONDS.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("消费了一个"+num);
                }

                System.out.println("consumerCondition.await()之后");

                //productCondition.signal();
            } finally {
                reentrantLock.unlock();
            }
        }
    }*/
    static String msg = null ;  // 设置一个字符串
    public static void main(String[] args) {
        // 获得当前的线程操作类
        Thread mainThread = Thread.currentThread();
        new Thread(()->{
            try {
                msg = "www.baidu.com" ;
            } finally {  // 解锁关起状态
                LockSupport.unpark(mainThread);
            }
        }) .start();
        //这里的park和unpark就像是一个Semaphore,但是仅有一个许可证,当不进行unpark,是无法使用park的.
        //so,这也是一种线程的等待和唤醒
        LockSupport.park(mainThread);
        System.out.println("********** 主线程执行完毕,msg="+msg);
    }
}


