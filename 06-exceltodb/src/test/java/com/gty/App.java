package com.gty;

import com.gty.domain.XRow;
import com.gty.util.DBUtil;
import com.gty.util.ReadExcel;
import org.junit.Test;

import java.io.File;
import java.util.List;

public class App {
    @Test
    public void test01() throws Exception {
        //获取第一行作为表的注释
        XRow firstRowData = ReadExcel.getFirstRowData(new File("e:\\test.xls"));

        //获取数据作为表的内容
        List<XRow> otherData = ReadExcel.getOtherData(new File("e:\\test.xls"), firstRowData);

        //创建数据库表,并返回表的字段
        List<String> columnList = DBUtil.createTable(firstRowData);

        //插入数据
        int[] ints = DBUtil.insertRowBatch(columnList, otherData);

    }
}
