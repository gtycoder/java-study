package com.gty.newthread;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class RunMain {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        //为了获取到结果,线程需要使用FutureTask类进行一下包装
        FutureTask<String> task = new FutureTask<>(new NewCallableThread());
        //然后使用Thread类在包装一下FutureTask类
        Thread thread = new Thread(task);
        thread.start();
        //该方法会阻塞当前线程,直到Callable线程执行完毕得出结果,才会获取.
        System.out.println(task.get());
    }
}

class NewCallableThread implements Callable<String> {
    @Override
    public String call() throws Exception {
        return "这是Callable线程的返回值";
    }
}