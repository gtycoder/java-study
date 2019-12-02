package com.gty.threadcommunication;

/**
 * 实现线程的协同,这个是网上的,后边那个是自己仿着写的
 */
public class NotifyAndWait {
    //创建共享对象
    private static Object object = new Object();
    //创建轮询标志位,false为没有改变.true为变化了
    private static boolean flag = false;

    //创建一个等待线程
    static class WaitThread implements Runnable {
        @Override
        public void run() {
            //加锁
            synchronized (object) {
                //轮询当没有改变(false)等待
                while (!flag) {
                    try {
                        object.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                //当改变时执行任务
                System.out.println(Thread.currentThread().getName() + "flag是" + flag + "等待被唤醒了,开始执行任务");
                //执行完成后将标志位回归原始
                flag = false;
            }
            //休眠一下
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //转入通知线程
            synchronized (object) {
                //当标志已经改变了就不在重复唤醒
                while (flag) {
                    try {
                        object.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                //当没有改变的时候要改变
                flag = true;
                //同时唤醒其他线程
                object.notifyAll();
                //额外输出
                System.out.println(Thread.currentThread().getName() + "flag是" + flag + "等待线程转入通知线程了");
            }
        }
    }

    //创建一个通知线程
    static class NotifyThread implements Runnable {
        @Override
        public void run() {
            //加个锁
            synchronized (object) {
                //当有其他线程改变了标志就不在改变
                while (flag) {
                    try {
                        object.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                //当状态没有发生该改变,执行任务
                System.out.println(Thread.currentThread().getName() + "flag是" + flag + "通知线程执行任务");
                //改变状态
                flag = true;
                object.notifyAll();
            }
            //休眠一下
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //转入等待线程
            synchronized (object) {
                while (!flag) {
                    try {
                        object.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                //额外输出
                System.out.println(Thread.currentThread().getName() + "flag是" + flag + "通知线程开始等待了");
            }
        }
    }

    public static void main(String[] args) {
        Thread waitThread = new Thread(new WaitThread());
        Thread notifyThread = new Thread(new NotifyThread());
        waitThread.start();
        notifyThread.start();

    }
}



