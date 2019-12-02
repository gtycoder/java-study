package com.gty;

import org.junit.Test;

import java.nio.IntBuffer;

public class TestApi {

    @Test
    public void test01() {
        //基本操作
        //1.创建指定长度的缓冲区(创建一个可以读写的HeapIntBuffer)[allocate分配]
        IntBuffer intBuffer = IntBuffer.allocate(10);
        //每put一个intBuffer中的pos(position)就增加一个
        intBuffer.put(11);
        intBuffer.put(22);
        intBuffer.put(33);
        //java.nio.HeapIntBuffer[pos=3 lim=10 cap=10]
        //[位置,限制,容量]
        System.out.println(intBuffer);

        //2.将pos复位,由2变为0 ,,这个方法很重要
        intBuffer.flip();
        //flip()方法实际就是   limit = position;  position = 0;  mark = -1;
        //java.nio.HeapIntBuffer[pos=0 lim=3 cap=10]
        System.out.println(intBuffer);

        //3.获取元素,  复位之后原来的的put的依然在   并且get(index)不会改变intbuffer
        System.out.println(intBuffer.get(1));
        //java.nio.HeapIntBuffer[pos=0 lim=3 cap=10]
        System.out.println(intBuffer);

        //4.替换元素
        //将位置为1的元素替换为2222
        intBuffer.put(1, 2222);
        //获取位置为1的元素是2222
        System.out.println(intBuffer.get(1));
        //java.nio.HeapIntBuffer[pos=0 lim=3 cap=10]
        System.out.println(intBuffer);


        //5.遍历
        for (int i = 0; i < intBuffer.limit(); i++) {
            System.out.println(intBuffer.get()+"\n");
        }
        //使用get()方法,会使pos每次加一 (position++)
        //java.nio.HeapIntBuffer[pos=3 lim=3 cap=10]
        System.out.println(intBuffer);
    }

    @Test
    public void test02() {
        //wrap的使用
        IntBuffer intbuffer = IntBuffer.wrap(new int[]{11, 22, 33});
        //java.nio.HeapIntBuffer[pos=0 lim=3 cap=3]  数组的长度就是容量
        System.out.println(intbuffer);

    }

    @Test
    public void test03() {
        //其他的基本操作
        IntBuffer intBuffer = IntBuffer.allocate(10);
        intBuffer.put(new int[]{11, 22, 33});
        //java.nio.HeapIntBuffer[pos=3 lim=10 cap=10]
        System.out.println(intBuffer);
        //1.复制一个一模一样的intBuffer
        IntBuffer intBuffer1 = intBuffer.duplicate();
        //java.nio.HeapIntBuffer[pos=3 lim=10 cap=10]
        System.out.println(intBuffer1);

        //2.将intbuffer中的数据放在一个int数组中
        int remaining = intBuffer.remaining();
        //remaining = limit - position,还剩多少地方
        System.out.println(remaining);
        int[] arr1 = new int[intBuffer.remaining()];
        //将缓冲区的数据放入arr1中
        intBuffer.get(arr1);
        for(Integer i : arr1) {
            System.out.print(Integer.toString(i) + ",");
        }

    }

    @Test
    public void test04() {
        System.out.println(1<<4);
    }

}
