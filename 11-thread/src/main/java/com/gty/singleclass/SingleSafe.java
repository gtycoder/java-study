package com.gty.singleclass;

/**
 * 实现一个线程安全的单例
 */
public class SingleSafe {
    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                SingleClass instance = SingleClass.getInstance();
                System.out.println(instance);
            }).start();
        }
    }
}

/**
 * 使用sync锁创建一个线程安全的单例对象
 */
class SingleClass {
    //无参构造私有化
    private SingleClass() {
    }

    //对象属内存可见,禁止指令重排
    private volatile static SingleClass singleClass = null;

    public static SingleClass getInstance() {
        //第一次判断是否为空,避免每次,每一个线程都进入同步锁.提高效率.
        if (null == singleClass) {
            //添加同步锁
            synchronized (SingleClass.class) {
                //第二次检测,避免其他线程创建,保证了同时只有一个线程在这里创建对象
                if (null == singleClass) {
                    //创建对象
                    singleClass = new SingleClass();
                }
            }
        }
        return singleClass;
    }
}
