package com.gty.testfuture;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

/**
 * 测试并行流
 */
public class TestStream {
    public static void main(String[] args) {
        Shops shops = new Shops();
        long pre = System.currentTimeMillis();
        //使用顺序流
        //List<String> price = shops.getPrice();

        //使用并行流
        //List<String> price = shops.getPrice2();

        //使用顺序流,使用异步CompletableFuture
        //List<String> price = shops.getPrice3();

        //使用顺序流,使用异步,指定线程池,这种方式使用起来更灵活,易于业务要求
        List<String> price = shops.getPrice4();

        for (String s : price) {
            System.out.println(s);
        }
        System.out.println("-----------------分割线---------------");
        System.out.println("用时====="+(System.currentTimeMillis()-pre));
    }
}

class Shops {
    //有5个商店要比价格
    private List<String> shopNames = Arrays.asList("1", "2", "3", "4","5");

    //使用顺序流
    public List<String> getPrice() {
        Stream<String> stream = shopNames.stream();
        List<String> collect = stream.map(s -> s + "价格是" + computePrice(s)).collect(toList());
        return collect;
    }

    //使用并行流
    public List<String> getPrice2() {
        //parallelStream()并行处理,其实底层使用的也是线程池,并且最多只有4个线程并行使用
        Stream<String> stream = shopNames.parallelStream();
        List<String> collect = stream.map(s -> s + "价格是" + computePrice(s)).collect(toList());
        return collect;
    }

    //使用顺序流,在顺序流中使用异步CompletableFuture
    public List<String> getPrice3() {
        Stream<String> stream = shopNames.stream();
        //使用异步方法,建新线程处理每一,与并行流类似
        List<CompletableFuture<String>> futures =
                stream.map(
                        s -> CompletableFuture.supplyAsync(
                                () -> s + "价格是" + computePrice(s)))
                        .collect(toList());
        //取出结果时使用join(),与get()一样,只是不用抛出异常
        List<String> collect = futures.stream().map(c -> c.join()).collect(toList());
        return collect;
    }

    //使用顺序流,使用异步,使用指定线程池
    public List<String> getPrice4() {
        //线程池中线程数量根据业务决定也可以这样写,shopNames.size();一个店铺一个
        ExecutorService executorService = Executors.newFixedThreadPool(10);

        Stream<String> stream = shopNames.stream();
        List<CompletableFuture<String>> futures =
                stream.map(
                        s -> CompletableFuture.supplyAsync(
                                //在使用SupplyAsync创建CompletableFuture时传入自定义的线程池
                                () -> s + "价格是" + computePrice(s), executorService))
                        .collect(toList());

        List<String> collect = futures.stream().map(c -> c.join()).collect(toList());
        //记得关闭线程池
        executorService.shutdown();
        return collect;
    }

    private int computePrice(String name) {
        //模拟延时
        this.delay();
        return Integer.valueOf(name) * 1000;
    }


    public void delay() {
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

