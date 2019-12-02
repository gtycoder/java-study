package com.gty.testcollections;

import java.util.concurrent.*;

/**
  ➣ ArrayBlockingQueue是数组实现的线程安全的有界的阻塞队列；
  ➣ LinkedBlockingQueue是单向链表实现的（指定大小）阻塞队列，该队列按FIFO（先进先出）排序元素；
  ➣ LinkedBlockingDeque是双向链表实现的（指定大小）双向并发阻塞队列，
          该阻塞队列同时支持FIFO和FILO两种操作方式；
  ➣ ConcurrentLinkedQueue是单向链表实现的无界队列，该队列按FIFO（先进先出）排序元素。
  ➣ ConcurrentLinkedDeque是双向链表实现的无界队列，该队列同时支持FIFO和FILO两种操作方式。
* */
public class TestQueue {
    /*
    队列的常用的方法:
        add 增加一个元索 如果队列已满，则抛出一个IIIegaISlabEepeplian异常
        remove 移除并返回队列头部的元素 如果队列为空，则抛出一个NoSuchElementException异常
        element 返回队列头部的元素 如果队列为空，则抛出一个NoSuchElementException异常
    以上三个会抛出异常.
        offer 添加一个元素并返回true 如果队列已满，则返回false
        poll 移除并返问队列头部的元素 如果队列为空，则返回null
        peek 返回队列头部的元素 如果队列为空，则返回null
    以上三个不会抛出异常但是会出现null
        put 添加一个元素 如果队列满，则阻塞
        take 移除并返回队列头部的元素 如果队列为空，则阻塞
    以上两个会阻塞
    * */
}

/**
 * 测试ArrayBlockingQueue,能保证消费者消费一个,生产者就生产一个
 */
class TestArrayBlockingQueue {
    public static void main(String[] args) {
        //创建一个公用的阻塞队列,必须数组的实现必须要指定数量
        BlockingQueue<Integer> blockingQueue = new ArrayBlockingQueue<>(5);

        //创建生产者线程
        for (int i = 0; i < 20; i++) {
            int index = i;
            new Thread(() -> {
                try {
                    TimeUnit.SECONDS.sleep(1);
                    //当5个线程存入了值后,方法会阻塞,知道消费者移除一个.然后才会添加一个.
                    blockingQueue.put(index);
                    System.out.println(Thread.currentThread().getName() + "生产了一个" + index);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }

        //创建2个消费者线程
        for (int i = 0; i < 2; i++) {
            new Thread(() -> {
                for (; ; ) {
                    try {
                        TimeUnit.SECONDS.sleep(2);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    if (blockingQueue.isEmpty()) {
                        System.out.println("blockingQueue空了");
                        break;
                    }
                    try {
                        //先进先出(FIFO)队列
                        Integer take = blockingQueue.take();
                        System.out.println(Thread.currentThread().getName() + "{{取出了一个}}----" + take);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }
}

/**
 * 测试LinkedBlockingQueue对阻塞队列的实现
 * 对于Queue来说,Array和Link的实现使用基本一致.都是FIFO.
 */
class TestLinkedBlockingQueue {
    public static void main(String[] args) {

        //创建一个公用的阻塞队列,必须数组的实现必须要指定数量
        BlockingQueue<Integer> blockingQueue = new LinkedBlockingQueue<>(5);

        //创建生产者线程
        for (int i = 0; i < 20; i++) {
            int index = i;
            new Thread(() -> {
                try {
                    TimeUnit.SECONDS.sleep(1);
                    //当5个线程存入了值后,方法会阻塞,知道消费者移除一个.然后才会添加一个.
                    blockingQueue.put(index);
                    System.out.println(Thread.currentThread().getName() + "生产了一个" + index);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }

        //创建2个消费者线程
        for (int i = 0; i < 2; i++) {
            new Thread(() -> {
                for (; ; ) {
                    try {
                        TimeUnit.SECONDS.sleep(2);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    if (blockingQueue.isEmpty()) {
                        System.out.println("blockingQueue空了");
                        break;
                    }
                    try {
                        //先进先出(FIFO)队列
                        Integer take = blockingQueue.take();
                        System.out.println(Thread.currentThread().getName() + "{{取出了一个}}----" + take);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }
}

/*
 * 以上是使用的是单端的阻塞队列
 * 还有:PriorityBlockingQueue  可以指定优先级的
 *     SynchronousQueue       只能一进一出的阻塞队列
 * */

/**
 * 测试一下双端队列,
 * Deque是Queue的一种特殊情况.也实现了Queue接口
 */
class TestLinkedBlockingDeque {
    public static void main(String[] args) {
        LinkedBlockingDeque<Integer> deque = new LinkedBlockingDeque<>(5);

        //生产者在前后端放数据
        for (int i = 0; i < 2; i++) {
            int index = i;
            new Thread(() -> {
                for (int j = 1; j < 5; j++) {
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    if (j % 2 == 0) {
                        try {
                            deque.putFirst(index);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        System.out.println(Thread.currentThread().getName() + "向队列头添加" + index);
                    } else {
                        try {
                            deque.putLast(index);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        System.out.println(Thread.currentThread().getName() + "向队列尾添加" + index);
                    }
                }
            }).start();
        }
        
        //消费者从前取数据
        new Thread(()->{
            while (true) {
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (deque.isEmpty()) {
                    System.out.println("队列空了");
                    break;
                }

                try {
                    Integer integer = deque.takeFirst();
                    System.out.println(Thread.currentThread().getName()+"从队头取出"+integer);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        //消费者从后取数据
        new Thread(()->{
            while (true) {
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (deque.isEmpty()) {
                    System.out.println("队列空了");
                    break;
                }

                try {
                    Integer integer = deque.takeLast();
                    System.out.println(Thread.currentThread().getName()+"从队尾取出"+integer);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}



