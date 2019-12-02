package com.gty.testjucutil;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 测试CountDownLatch,计数器
 * 作用:一个线程等待其他线程全部完成后,程序才会继续运行,否则一直处于等待状态
 */
public class TestCountDownLatch {

    static int count = 0;

    public static void main(String[] args) {
        ExecutorService threadPool = Executors.newFixedThreadPool(10);
        //创建一个count数量为100的计数器
        CountDownLatch countDownLatch = new CountDownLatch(100);
        for (int i = 1; i <= 100; i++) {
            final int index = i;
            //每循环一次就创建一个线程,并且放到线程池中
            threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    synchronized (TestCountDownLatch.class) {
                        count = count +index;
                        //每调用一次count数量就减一
                        countDownLatch.countDown();
                        System.out.println(count+"=====");
                    }
                }
            });
        }

        //计数锁await()结束(只有当count=0才能继续往下执行)
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(count);
        //关闭线程池
        threadPool.shutdown();
    }
}
