package com.gty.testlock;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 测试ReentrantLock和Condition
 * 作用:按照条件解锁某一个线程,更加方便控制线程之间的协同
 */
public class TestCondition {

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        //true表示这是一个公平锁
        ReentrantLock lock = new ReentrantLock();
        //设置锁为可中断锁
        try {
            lock.lockInterruptibly();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //创建解锁的条件
        Condition condition1 = lock.newCondition();
        Condition condition2 = lock.newCondition();

        executorService.execute(() -> {
            try {
                lock.lock();
                //监听c1,当有线程唤醒c1就开始往下走
                condition1.await();
                System.out.println("b");
                //唤醒c2,当有线程监听c2就会继续执行
                condition2.signal();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        });

        executorService.execute(() -> {
            try {
                lock.lock();
                condition2.await();
                System.out.println("c");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        });

        //作为起始入口的第一个执行的线程要放在最后,保证其他已经await后,在进行signal
        executorService.execute(() -> {
            try {
                lock.lock();
                System.out.println("a");
                condition1.signal();//唤醒c1
            }finally {
                lock.unlock();
            }
        });

        executorService.shutdown();
    }


}
