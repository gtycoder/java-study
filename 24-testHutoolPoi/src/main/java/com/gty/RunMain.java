package com.gty;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.junit.Test;

import java.util.List;

public class RunMain {
    public static void main(String[] args) {
        JSONObject obj = JSONUtil.createObj();
        System.out.println(obj);
    }


    @Test
    public void test01() {
        List<String> row1 = CollUtil.newArrayList("aa", "bb", "cc", "dd");
        List<String> row2 = CollUtil.newArrayList("aa1", "bb1", "cc1", "dd1");
        List<String> row3 = CollUtil.newArrayList("aa2", "bb2", "cc2", "dd2");
        List<String> row4 = CollUtil.newArrayList("aa3", "bb3", "cc3", "dd3");
        List<String> row5 = CollUtil.newArrayList("aa4", "bb4", "cc4", "dd4");

        List<List<String>> rows = CollUtil.newArrayList(row1, row2, row3, row4, row5);

        ExcelWriter writer = ExcelUtil.getWriter("E:\\53file_download\\testWrite"+System.currentTimeMillis()+".xlsx");
        writer.renameSheet("one");

        //跳过当前行，既第一行，非必须，在此演示用
        writer.passCurrentRow();

        //合并单元格后的标题行，使用默认标题样式
        writer.merge(row1.size() - 1, "测试标题");
        CellStyle headCellStyle = writer.getHeadCellStyle();
        headCellStyle.setFillForegroundColor(IndexedColors.YELLOW1.index);

        //一次性写出内容，强制输出标题
        writer.write(rows, true);

        //写第二个
        writer.setSheet("two");
        writer.passCurrentRow();

        List<String> row9 = CollUtil.newArrayList("aa4", "bb4");
        List<String> row10 = CollUtil.newArrayList("aa4", "bb4");

        List<List<String>> rows99 = CollUtil.newArrayList(row9,row10);
        writer.merge(row9.size() - 1, "测试标题55555555555");
        writer.write(rows99, true);

        //写第三个
        writer.passCurrentRow();

        //关闭writer，释放内存
        writer.close();

    }
}
