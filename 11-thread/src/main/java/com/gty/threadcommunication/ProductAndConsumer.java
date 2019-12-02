package com.gty.threadcommunication;

/**
 * 使用线程中的通信实现一个简单的生产者和消费者
 */
public class ProductAndConsumer {
    //创建商品的库存标志
    private static int num = 0;
    //共享对象
    private static Object object = new Object();

    //创建一个生产者
    static class Product implements Runnable {
        @Override
        public void run() {
            while (true) {
                //加锁
                synchronized (object) {
                    //当商品库存低于5时开始生产
                    while (num < 5) {
                        num++;
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        System.out.println("11111生产者生产一个商品" + num);
                        //唤醒消费者
                    }
                    //切记先唤醒在休眠
                    //唤醒消费者
                    object.notifyAll();
                    //超过5个
                    try {
                        object.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }

    //创建一个消费者
    static class Consumer implements Runnable {
        @Override
        public void run() {
            while (true) {
                synchronized (object) {
                    //当库存高于1开始卖
                    while (num > 0) {
                        //开始售卖
                        num--;
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        System.out.println("22222消费者消费了一个商品" + num);
                    }
                    //切记先唤醒在休眠
                    object.notifyAll();
                    try {
                        object.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        Thread product = new Thread(new Product());
        Thread consumer = new Thread(new Consumer());
        product.start();
        consumer.start();
    }

}
