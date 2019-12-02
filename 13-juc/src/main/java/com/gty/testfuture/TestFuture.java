package com.gty.testfuture;

import java.util.concurrent.*;

/**
 * 测试Future接口,使用线程池计算100以内的质数
 */
public class TestFuture {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        //对0和1求没有意义
        for (int i = 2; i < 100; i++) {
            ComputePrimeNum primeNum = new ComputePrimeNum();
            primeNum.setNum(i);
            Future<Boolean> submit = executorService.submit(primeNum);
            try {
                if (submit.get()) {
                    System.out.println(i);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

        }
        executorService.shutdown();
    }
}

class ComputePrimeNum implements Callable<Boolean> {
    private int num;

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    @Override
    public Boolean call() throws Exception {
        boolean isPrime = true;
        //计算是否是质数要从2开始,并且出去自身
        for (int i = 2; i < num; i++) {
            if (num%i==0) {
                //说明有除了1和自身的还能整除的数,不是质数
                isPrime = false;
                //找到一个就可以说明了,直接跳出循环
                break;
            }
        }
        return isPrime;
    }
}
