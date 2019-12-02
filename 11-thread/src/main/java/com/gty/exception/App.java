package com.gty.exception;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class App {
    public static void main(String[] args) {

        try {
            //使用自定义的线程工厂创建线程
            ExecutorService executor = Executors.newFixedThreadPool(10, new MyThreadFactory());
            executor.execute(new ExceptionThread());
            executor.shutdown();
        } catch (Exception e) {
            System.out.println("主线程捕获了其他线程的异常");
            //e.printStackTrace();
        }

    }
}

/**
 * 创建一个会出现异常的线程,在main线程中调用该线程
 */
class ExceptionThread implements Runnable {
    @Override
    public void run() {
        int i = 10 / 0;
    }
}

/**
 * 自定义线程工厂
 */
class MyThreadFactory implements ThreadFactory {
    @Override
    public Thread newThread(Runnable r) {
        Thread thread = new Thread(r);
        thread.setUncaughtExceptionHandler(new MyUncaughtExceptionHandler());
        return thread;
    }
}

/**
 * 自定义出现异常的处理方式
 */
class MyUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {
    @Override
    public void uncaughtException(Thread t, Throwable e) {
        System.out.println(Thread.currentThread().getName() + "发生异常" + e.getMessage());
        //这样写是不行的,还是在原来线程的控制台打印,必须要添加上上边的打印
        //e.printStackTrace();
    }
}



