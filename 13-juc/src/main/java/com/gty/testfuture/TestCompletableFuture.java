package com.gty.testfuture;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Supplier;

/**
 * 测试使用CompletableFuture
 * 1.使用异步方法会提高效率
 * 2.使用CompletableFuture.supplyAsync(()->{})可以使get()发生异常直接停止,不阻塞,.
 */
public class TestCompletableFuture {

    public static void main(String[] args) {
        Shop shop = new Shop();
        long p = System.currentTimeMillis();

        //这是同步方法
        //int price = shop.getPrice();

        //使用异步方法
        CompletableFuture<Integer> priceAsync = shop.getPriceAsync2();


        Integer price = null;
        try {
            //设置等待时间,超过了时间就停止,不会阻塞
            price = priceAsync.get(4,TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }

        //模拟其他的工作
        //使用异步的话相当于这里的doSome()和Shop中的getPriceAsync()在同时跑.
        doSome();

        System.out.println("price"+price);
        System.out.println("耗时"+(System.currentTimeMillis()-p));
    }

    public static void doSome() {
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}

class Shop {
    private int price=100;

    //使用异步,自己创建CompletableFuture,发生异常后会get()方法会一直阻塞,直到timeout.
    public CompletableFuture<Integer> getPriceAsync1() {
        CompletableFuture<Integer> completableFuture = new CompletableFuture<>();
        new Thread(()->{
            int price = this.computePrice();
            completableFuture.complete(price);
        }).start();
        return completableFuture;
    }

    //使用异步线程,使用静态工厂创建,当当前线程发生异常后,会直接反馈到get()方法,使线程不会阻塞或是等待时间.
    public CompletableFuture<Integer> getPriceAsync2() {
        //也能使用lambod表达式
        /*CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            return computePrice();
        });*/

        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(new Supplier<Integer>() {
            @Override
            public Integer get() {
                return computePrice();
            }
        });
        return future;
    }

    //使用同步的方式
    public int getPrice() {
        //模拟延时1s
        delay();
        return this.computePrice();
    }

    public int computePrice() {
        //设置错误,使线程抛出异常
        //int a = 10 / 0;
        return 10 * this.price;
    }

    public void delay() {
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
