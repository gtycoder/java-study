package com.gty.testthreadpool;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.*;

public class TestThreadPool {

    //延迟1s,测试线程池的容量
    private static void delay() {
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    static int doSome() {
        delay();
        System.out.println(Thread.currentThread().getName() + "执行了doSome,在" + new SimpleDateFormat("HH:mm:ss").format(new Date()));
        return 22;
    }
}

class TestCacheThreadPool {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        CountDownLatch countDownLatch = new CountDownLatch(6);
        //最多有5个线程同时执行,超过了就进入等待队列
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        long pre = System.currentTimeMillis();
        //创建6个线程,执行会延长至下一轮
        for (int i = 0; i < 6; i++) {
            Future<Integer> future = executorService.submit(() -> {
                return TestThreadPool.doSome();
            });
            countDownLatch.countDown();
            System.out.println(future.get());
        }

        countDownLatch.await();
        System.out.println("用时" + (System.currentTimeMillis() - pre));
        //关闭线程池,不在接收新的任务
        executorService.shutdown();
    }
}
