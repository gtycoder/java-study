package com.gty.testjucutil;

import java.util.concurrent.Exchanger;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * 测试exchanger
 * java.util.concurrent包中的Exchanger类可用于两个线程之间交换信息。
 * 可简单地将Exchanger对象理解为一个包含两个格子的容器，通过exchanger方法可以向两个格子中填充信息。
 * 当两个格子中的均被填充时，该对象会自动将两个格子的信息交换，然后返回给线程，从而实现两个线程的信息交换。
 */
public class TestExchanger {
    public static void main(String[] args) {
        Exchanger<String> exchanger = new Exchanger<>();
        //传入时test1对应test1,test2对应test2
        Thread test1 = new Thread(new ExchangerTest("test1",exchanger));
        test1.setName("test1");
        //当只有一个时,线程会阻塞
        Thread test2 = new Thread(new ExchangerTest("test2",exchanger));
        test2.setName("test2");
        test1.start();
        test2.start();
    }
}

class ExchangerTest implements Runnable {
    private String name;
    //公用同一个交换空间
    private Exchanger<String> exchanger;

    public ExchangerTest(String name,Exchanger<String> exchanger) {
        this.name = name;
        this.exchanger = exchanger;
    }

    @Override
    public void run() {
        System.out.println("交换之前"+Thread.currentThread().getName()+name);
        try {
            //等待5秒,凑不够2个线程交换就结束
            //传入自己的内容,交换另一个线程的内容,也可以传递null,实现一个读一个写
            String after = exchanger.exchange(name,5,TimeUnit.SECONDS);
            System.out.println("交换之后"+Thread.currentThread().getName()+after);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }
}
