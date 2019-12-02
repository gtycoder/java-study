package com.gty.threadcommunication;

/**
 * 使用wait和notify进行线程之间的通信,自己实现的
 */
public class WaitAndNotify {
    //轮询标志位,未变更是false.变更了是true
    private static boolean stop = false;
    //监视器对应的对象
    private static Object monitor = new Object();

    //等待线程
    static class WaitThread implements Runnable{
        @Override
        public void run() {
            synchronized(monitor){
                //循环检测标志位是否变更
                while(!stop){
                    try {
                        //标志位未变更，进行等待
                        monitor.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                //标志位变更了就开始执行任务
                //被唤醒后获取到对象的监视器之后执行的代码
                System.out.println("Thread "+Thread.currentThread().getName()+" 被===唤醒了");
                //任务执行完成之后重置标志位,并进入休眠,同时唤醒其他线程
                stop = false;
            }
            //休眠1秒之后，线程角色转换为唤醒线程
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //与上述代码相反的逻辑
            synchronized(monitor){
                //当其已经被更改了就不在进行修改
                while(stop){
                    try {
                        monitor.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                //当线程重置为false后会唤醒其他的线程
                stop = true;
                monitor.notify();
                System.out.println("Thread "+ Thread.currentThread().getName()+"唤醒其他线程11111");
            }
        }
    }

    /*Thread 通知线程唤醒其他线程222
    Thread 等待线程 被===唤醒了
    Thread 等待线程唤醒其他线程11111
    Thread 通知线程被唤醒了----
    * */

    //通知线程
    static class NotifyThread implements Runnable{
        @Override
        public void run() {
            synchronized (monitor){
                while(stop){
                    //当标志位已经改变时休眠
                    try {
                        monitor.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                //标志位没有改变就改变
                stop = true;
                monitor.notify();
                System.out.println("Thread "+ Thread.currentThread().getName()+"唤醒其他线程222");
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (monitor){
                while(!stop){
                    try {
                        monitor.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("Thread "+Thread.currentThread().getName()+"被唤醒了----");
            }
        }
    }
    public static void main(String[] args){
        Thread waitThread = new Thread(new WaitThread());
        waitThread.setName("等待线程");
        Thread notifyThread = new Thread(new NotifyThread());
        notifyThread.setName("通知线程");
        waitThread.start();
        notifyThread.start();
    }
}

