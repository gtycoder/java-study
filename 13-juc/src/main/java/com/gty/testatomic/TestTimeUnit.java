package com.gty.testatomic;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class TestTimeUnit {
    @Test
    public void test01() throws InterruptedException {
        //休眠一秒
        TimeUnit.SECONDS.sleep(1);

        //单位转换
        long convert = TimeUnit.SECONDS.convert(1, TimeUnit.HOURS);
        System.out.println("将一小时转换为秒"+convert);
    }

    @Test
    public void test02() {
        //求3天后的日期
        long convert01 = TimeUnit.MILLISECONDS.convert(3, TimeUnit.DAYS);
        Date followThreeDays = new Date(System.currentTimeMillis() + convert01);
        String format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(followThreeDays);
        System.out.println(format);

    }

}
