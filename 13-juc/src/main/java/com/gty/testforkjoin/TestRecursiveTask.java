package com.gty.testforkjoin;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

/**
 * 测试有返回值的RecursiveTask有返回值的情况.
 * 求2000000000以内的加法
 */
public class TestRecursiveTask {

    //继承RecursiveTask任务,定义有返回值的可分解任务
    static class MyRecursiveTask extends RecursiveTask<Long> {
        private static final int THRESHOLD = 5000;
        private int start;
        private int end;

        public MyRecursiveTask(int start, int end) {
            this.start = start;
            this.end = end;
        }

        public MyRecursiveTask() {
        }

        @Override
        protected Long compute() {
            if ((end - start) < THRESHOLD) {
                return sumAdd(start,end);
            } else {
                int middle = (start + end) / 2;
                //创建两个子任务
                MyRecursiveTask left = new MyRecursiveTask(start, middle);
                MyRecursiveTask right = new MyRecursiveTask(middle, end);
                left.fork();
                right.fork();
                return left.join() + right.join();
            }
        }

        public Long sumAdd(int s,int e) {
            long sum = 0L;
            for (int i = s; i < e; i++) {
                sum = sum + i;
            }

            return sum;
        }
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        //使用单线程计算
        long pre = System.currentTimeMillis();
        MyRecursiveTask single = new MyRecursiveTask();
        Long aLong = single.sumAdd(0, 2000000001);
        System.out.println(aLong);
        System.out.println("========单线程花费"+(System.currentTimeMillis()-pre));

        long pre1 = System.currentTimeMillis();
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        ForkJoinTask<Long> submit = forkJoinPool.submit(new MyRecursiveTask(0, 2000000001));
        Long join = submit.join();
        //Long join = forkJoinPool.invoke(new MyRecursiveTask(0, 2000000001));
        System.out.println(join);
        System.out.println("========多线程花费"+(System.currentTimeMillis()-pre1));
        forkJoinPool.shutdown();
    }

}
