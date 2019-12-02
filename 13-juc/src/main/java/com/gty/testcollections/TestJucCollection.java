package com.gty.testcollections;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.*;

/**
 * 测试使用juc包中的线程安全的List,Set,以及Map的实现
 * ➣ List和Set集合：
 *         ➣ CopyOnWriteArrayList相当于线程安全的ArrayList，实现了List接口。
 *             CopyOnWriteArrayList是支持高并发的；
 *         ➣ CopyOnWriteArraySet相当于线程安全的HashSet，它继承了AbstractSet类，
 *             CopyOnWriteArraySet内部包含一个CopyOnWriteArrayList对象，
 *             它是通过CopyOnWriteArrayList实现的。
 * ➣ Map集合：
 *         ➣ ConcurrentHashMap是线程安全的哈希表（相当于线程安全的HashMap）；
 *             它继承于AbstractMap类，并且实现ConcurrentMap接口。
 *             ConcurrentHashMap是通过“锁分段”来实现的，它支持并发；
 *         ➣ ConcurrentSkipListMap是线程安全的有序的哈希表（相当于线程安全的TreeMap）；
 *             它继承于AbstactMap类，并且实现ConcurrentNavigableMap接口。
 *             ConcurrentSkipListMap是通过“跳表”来实现的，它支持并发；
 *         ➣ ConcurrentSkipListSet是线程安全的有序的集合（相当于线程安全的TreeSet）;
 *             它继承于AbstractSet，并实现了NavigableSet接口。
 *             ConcurrentSkipListSet是通过ConcurrentSkipListMap实现的，它也支持并发；
 */
public class TestJucCollection {

    /**
     * 会抛出异常java.util.ConcurrentModificationException
     * ArrayList不支持多线程并发的操作.该异常是由于数据顺序编号不符合导致的
     */
    @Test
    public void testArrayList() {
        List<Integer> sList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            int index = i;
            new Thread(() -> {
                sList.add(index);
            }).start();
        }
        for (Integer integer : sList) {
            System.out.println(integer + "\t");
        }
    }

    /**
     * 添加内容可以重复
     */
    @Test
    public void testCopyOnWriteArrayList() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(2);
        List<String> iList = new CopyOnWriteArrayList<>();
        for (int i = 0; i < 2; i++) {
            int index = i;
            new Thread(() -> {
                for (int j = 0; j < 10; j++) {
                    //iList.add("线程:"+Thread.currentThread().getName() + "-" + index + "-添加的内容" + j);
                    iList.add("线程:"/*+Thread.currentThread().getName() + "-" + index */ + "-添加的内容" + j);
                }
                countDownLatch.countDown();
            }).start();
        }

        countDownLatch.await();

        for (String s : iList) {
            System.out.println(s + "\t");
        }

    }

    /**
     * 添加内容不可重复
     */
    @Test
    public void testCopyOnWriteSet() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(2);

        Set<String> sSet = new CopyOnWriteArraySet<>();
        for (int i = 0; i < 2; i++) {
            int index = i;
            new Thread(() -> {
                for (int j = 0; j < 10; j++) {
                    //sSet.add("线程:"+Thread.currentThread().getName() + "-" + index + "-添加的内容" + j);
                    //当添加的内容相同时和CopyOnWriteArrayList不同
                    sSet.add("线程:"/*+Thread.currentThread().getName() + "-" + index */ + "-添加的内容" + j);
                }
                countDownLatch.countDown();
            }).start();
        }

        countDownLatch.await();

        for (String s : sSet) {
            System.out.println(s + "\t");
        }

    }

    /**
     * 整体特征：写的时候同步写入，使用独占锁，读的时候为了保证性能使用了共享锁。
     */
    @Test
    public void testConcurrentHashMap() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(2);

        Map<String, Object> map = new ConcurrentHashMap<>();
        for (int i = 0; i < 2; i++) {
            int x = i;
            new Thread(() -> {
                for (int j = 0; j < 10; j++) {
                    //虽然有两个线程,每个put10个.当时当key相同时,value就被替换了.so,只会有10个,value要么全是0,要么全是1.
                    map.put(String.valueOf(j), x);
                }
                countDownLatch.countDown();
            }).start();
        }

        countDownLatch.await();

        for (Map.Entry<String, Object> entry : map.entrySet()) {
            System.out.println(entry.getKey() + "==" + entry.getValue());
        }
    }

    /**
     * skip意为跳表
     * 跳表集合本质上的功能是一种快速查询功能，也就是说它会在一个有序的链表里面选择一些数据作为检索的种子数。
     * 用这些种子数方便进行数据的查找，非常类似于二分法。
     *
     * 使用时要注意与ConcurrentHashMap的区别.
     */
    @Test
    public void testConcurrentSkipListMap() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(2);

        Map<String, Object> map = new ConcurrentSkipListMap<>();
        for (int i = 0; i < 2; i++) {
            int x = i;
            new Thread(() -> {
                for (int j = 0; j < 10; j++) {
                    //同样的代码逻辑,这里使用ConcurrentSkipListMap与CurrentHashMap结果是不相同的.
                    //虽然都有10个值,但是value值不同.
                    map.put(String.valueOf(j), x);
                }
                countDownLatch.countDown();
            }).start();
        }

        countDownLatch.await();

        for (Map.Entry<String, Object> entry : map.entrySet()) {
            System.out.println(entry.getKey() + "==" + entry.getValue());
        }
    }

    /**
     * 测试skipListSet
     */
    @Test
    public void testConcurrentSkipListSet() {
        CountDownLatch countDownLatch = new CountDownLatch(2);
        Set<String> set = new ConcurrentSkipListSet<>();
        for (int i = 0; i < 2; i++) {
            new Thread(() -> {
                for (int j = 0; j < 10; j++) {
                    //添加的内容相同会覆盖set不可重复
                    //set.add("线程:"+Thread.currentThread().getName()+"-添加的内容" + j);
                    set.add("线程:" + "-添加的内容" + j);
                    countDownLatch.countDown();
                }
            }).start();
        }

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (String s : set) {
            System.out.println(s);
        }

    }

}
