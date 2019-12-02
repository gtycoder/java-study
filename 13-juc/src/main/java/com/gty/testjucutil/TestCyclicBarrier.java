package com.gty.testjucutil;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *测试CyclicBarrier,循环屏障
 * 作用:等待一组线程达到一定的状态.然后在做某件事情.并且该屏障可重用
 *
 * 与CountDownLatch区别:
 * CountDownLatch的下一步的动作实施者是主线程，具有不可重复性；
 * 而CyclicBarrier的下一步动作实施者还是“其他线程”本身，具有往复多次实施动作的特点。
 *
 * 面试题：CountDownLatch与CyclicBarrier区别？
 * CountDownLatch：最大特征是进行一个数据减法的操作等待，所有的统计操作一旦开始之中就必须一直执行countDown()方法，
 * 如果等待个数不是0将被一直等待，并且无法重置；
 * CyclicBarrier：设置一个等待的临界点，并且可以有多个等待线程出现，只要满足了临界点触发了线程的执行代码后将重新开始进行计数处理操作，也可以直接利用reset()方法执行重置操作。
 */
public class TestCyclicBarrier {

    static ExecutorService executorService = Executors.newFixedThreadPool(10);
    static CyclicBarrier cyclicBarrier = new CyclicBarrier(5, new Runnable() {
        @Override
        public void run() {
            //达到某个屏障点后一起做什么事
            System.out.println("人数达到了5人,开始活动");
            //这里一般都是最后一个线程来执行结束后的任务
            System.out.println(Thread.currentThread().getName());
            //是不是可以用来关闭资源
            //executorService.shutdown();
        }
    });

    public static void main(String[] args) {

        for (int i = 0; i < 20; i++) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            executorService.execute(()->{
                play();
            });
        }
        executorService.shutdown();
    }

    private static void play() {
        System.out.println(Thread.currentThread().getName()+"准备就绪");
        try {
            //当数量不够时会阻塞,程序不会停止
            cyclicBarrier.await();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
        //System.out.println(Thread.currentThread().getName()+"开始运行");
    }
}
