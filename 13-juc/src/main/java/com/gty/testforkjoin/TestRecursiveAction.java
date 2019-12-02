package com.gty.testforkjoin;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.TimeUnit;

/**
 * ForkJoinPool中添加的是可分割的任务,没有返回值的情况
 * 任务如何可分割?
 */
public class TestRecursiveAction {

    //定义一个数据接收方,无界队列
    static Queue<String> queue = new ConcurrentLinkedQueue<>();

    //=====================内部类开始=============================
    /**
     * 需要继承ForkJoinTask的无返回值的子类,定义如何分隔任务
     */
    static class MyRecursiveAction extends RecursiveAction {

        //定义一个临界值,作为区分的界限
        private static final int THRESHOLD = 500;
        //定义开始值
        private int start;
        //定义结束值
        private int end;
        //定义需要分隔的数据
        private List<String> list;

        public MyRecursiveAction(int start, int end, List<String> list) {
            this.start = start;
            this.end = end;
            this.list = list;
        }

        @Override//定义任务如何分隔
        protected void compute() {
            //在这里定义任务拆分规则
            if ((end - start) < THRESHOLD) {
                sendData(this.list);
            } else {
                //这里是每次发送的数据超过了临界值,要分成两个小任务
                int middle = (start + end) / 2;
                //使用递归的方式,创建左右两个任务
                MyRecursiveAction left = new MyRecursiveAction(start, middle, list);
                MyRecursiveAction right = new MyRecursiveAction(middle, end, list);
                //两个任务都开启
                left.fork();
                right.fork();
            }

        }

        //主要的发送数据的方法,这里会在每次符合条件的时候才会调用
        private void sendData(List<String> list) {
            for (int i = start; i < end; i++) {
                //模拟数据发到db2
                queue.add(list.get(i));
            }
            //每次发送延时50ms
            try {
                TimeUnit.MILLISECONDS.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    //=======================内部类结束=============================

    public static void main(String[] args) throws InterruptedException {
        //这里模拟查询出了10-11万条的数据
        List<String> list = getDate();
        int count = list.size();
        System.out.println("============从db1中取出的数据大小" + count);
        System.out.println("============该开始时db2中的数据大小" + queue.size());
        System.out.println("=======开始数据迁移========");
        //创建一个线程池
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        //添加一个可分解任务
        forkJoinPool.submit(new MyRecursiveAction(0, count, list));
        //阻塞,知道任务完成
        forkJoinPool.awaitTermination(10, TimeUnit.SECONDS);
        //ForkJoinPool也是一个线程池,也需要关闭
        forkJoinPool.shutdown();
        System.out.println("=======结束数据迁移========");
        System.out.println("================结束后db2中数据"+queue.size());
        //查询其中一个
        System.out.println(queue.peek());
    }

    //模拟数据查询
    static List<String> getDate() {
        //这里不能使用LinkedList,要ArrayList并且尽量指定大小,避免频繁扩容
        List<String> list = new ArrayList<>(111000);
        Random random = new Random();
        //查询一次10万--11万数据
        int count = (int) (random.nextDouble() * 10000) + 100000;
        //将数据写入list
        for (int i = 0; i < count; i++) {
            list.add("id=" + random.nextInt(200000));
        }
        //查询耗时10ms
        try {
            TimeUnit.MILLISECONDS.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return list;
    }

}



