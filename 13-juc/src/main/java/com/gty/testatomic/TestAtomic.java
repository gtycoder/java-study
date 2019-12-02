package com.gty.testatomic;

import com.gty.domain.User;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicLongArray;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 测试原子类基本的操作
 */
public class TestAtomic {
    /**
     * AtomicLong 的基本用法
     */
    @Test
    public void test01() {
        AtomicLong atomicLong = new AtomicLong(10000l);
        long andAdd = atomicLong.getAndAdd(10);
        long l = atomicLong.addAndGet(10);
        long l1 = atomicLong.decrementAndGet();
    }

    /**
     * AtomicLongArray 基本使用
     */
    @Test
    public void test02() {
        long[] arr = new long[]{12,23,34,53,64,75,86};
        AtomicLongArray atomicLongArray = new AtomicLongArray(arr);
        atomicLongArray.set(0,99999);
        boolean b = atomicLongArray.compareAndSet(0, 999, 10000000001l);
        for (int i = 0; i < atomicLongArray.length(); i++) {
            long l = atomicLongArray.get(i);
            System.out.println(l);
        }
        System.out.println(b);
    }

    /**
     * AtomicReference<User> 测试对象类型原子类的基本使用
     */
    @Test
    public void test03() {
        AtomicReference<User> userAtomic = new AtomicReference<>();
        User user1 = new User(100);
        User user2 = new User(110);
        userAtomic.set(user1);
        boolean b = userAtomic.compareAndSet(user1, user2);

        User user3 = userAtomic.get();
        System.out.println(user3);
        System.out.println(user3.equals(user2));
        System.out.println(user3.equals(user1));
    }

    /**
     *  AtomicIntegerFieldUpdater<User>
     *  更新字段的使用方法和要求
     */
    @Test
    public void test04() {
        //字段要使用 public volatile int id;
        AtomicIntegerFieldUpdater<User> updater = AtomicIntegerFieldUpdater.newUpdater(User.class, "id");
        User user1 = new User(1000);
        //将user1的id字段的值加2000并且返回新值
        int i = updater.addAndGet(user1, 2000);
        System.out.println(i);
        System.out.println(user1);
    }

    //获取随机数的方式
    @Test
    public void test05() {
        Random random = new Random();
        for (int j = 0; j < 100; j++) {
            //10万-11万
            //(0.8)*(11万-10万)+10万   乘以差值 加上 下限
            int i =(int) (random.nextDouble()*10000)+100000;
            System.out.println(i);
        }
    }

}
