package com.gty.testthreadpool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PoolTest {
    public static void main(String[] args) throws InterruptedException {
        /*ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(5, 10, 100, TimeUnit.MINUTES, new ArrayBlockingQueue<>(5));
        for (int i=0;i<13;i++) {
            MyTask task = new MyTask(i);
            poolExecutor.execute(task);
            System.out.println("线程池中的线程数量="+poolExecutor.getPoolSize());
            System.out.println("缓存队列等待数量="+poolExecutor.getQueue().size());
            System.out.println("已经执行完成的线程数量="+poolExecutor.getCompletedTaskCount());
        }
        System.out.println("是否全部完成---"+poolExecutor.isTerminated());
        //关闭线程
        poolExecutor.shutdown();*/

        ExecutorService service = Executors.newWorkStealingPool();
        for (int i = 0; i < 20; i++) {

            service.submit(()->{
                System.out.println(Thread.currentThread().getName());
            });
        }
        Thread.sleep(800);
        service.shutdown();
    }

}

class MyTask implements Runnable {
    private int anInt;
    public MyTask(int i) {
        this.anInt = i;
    }

    @Override
    public void run() {
        System.out.println("开始执行任务======="+anInt);
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("结束执行任务======="+anInt);
    }
}
