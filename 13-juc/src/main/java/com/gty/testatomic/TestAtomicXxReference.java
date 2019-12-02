package com.gty.testatomic;

import org.junit.Test;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicStampedReference;
import java.util.concurrent.atomic.LongAdder;

public class TestAtomicXxReference {
    //测试ABA问题
    public static void main(String[] args) {
        //AtomicMarkableReference也可以实现
        AtomicStampedReference<String> reference = new AtomicStampedReference<>("A",1);
        //该线程负责打印和交换
        new Thread(()->{
            //获取期望值
            String expect = reference.getReference();
            //获得版本戳
            Integer stamp =reference.getStamp();
            System.out.println(Thread.currentThread().getName()+"期望值是------"+expect);
            //等待
            try {
                TimeUnit.SECONDS.sleep(8);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //打印现在准备进行cas操作前的值
            System.out.println(Thread.currentThread().getName()+"交换前的值----"+reference.getReference());
            //进行cas操作
            boolean b = reference.compareAndSet("A", "CAS操作成功了",stamp,stamp+1);
            System.out.println(Thread.currentThread().getName()+"结果--"+b+"交换后的值---"+reference.getReference());
        }).start();

        //该线程负责模拟ABA问题
        new Thread(()->{
            //先让上边初始化
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //进行ABA
            reference.compareAndSet("A", "B",reference.getStamp(),reference.getStamp()+1);
            //这里经过了线程操作reference但是值并没有变
            reference.compareAndSet("B", "A",reference.getStamp(),reference.getStamp()+1);
            System.out.println(Thread.currentThread().getName()+"交换了两次后----"+reference.getReference()+"版本戳"+reference.getStamp());
        }).start();

    }


    @Test
    public void test01() {
        LongAdder adder = new LongAdder();
        adder.increment();
        System.out.println(adder);
    }
}
