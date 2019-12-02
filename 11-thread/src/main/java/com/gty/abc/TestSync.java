package com.gty.abc;

/**
 * 使用synchronized多线程打印abc
 */
public class TestSync implements Runnable {

    private String printStr;
    private Object suf;
    private Object self;

    public TestSync(String printStr, Object suf, Object self) {
        this.printStr = printStr;
        this.suf = suf;
        this.self = self;
    }

    @Override
    public void run() {
        print1();
    }

    //设置同步锁
    public void print1() {
        int i = 10;
        //给前对象加锁
        synchronized (suf) {
            //给自己加锁
            synchronized (self) {
                            /*  Object suf, Object self
                                ("A",c,a);
                                ("B",a,b);
                                ("C",b,c);*/
                System.out.print(printStr);
                //唤醒self上的线程
                i--;
                self.notify();
            }
            //suf对象上的等待,目的是释放suf上的锁,wait方法可以释放掉锁.
            try {
                suf.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    //测试
    public static void main(String[] args) throws InterruptedException {
        //创建锁对象
        Object a = new Object();
        Object b = new Object();
        Object c = new Object();
        //创建测试用例
        new Thread(new TestSync("A", c, a)).start();
        //sleep是为了保证线程按顺序启动
        Thread.sleep(100);
        new Thread(new TestSync("B", a, b)).start();
        Thread.sleep(100);
        new Thread(new TestSync("C", b, c)).start();
        Thread.sleep(100);
        //B等待A执行完成,C等待B完成,A等待C完成
    }


}
