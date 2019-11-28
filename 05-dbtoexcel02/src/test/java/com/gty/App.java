package com.gty;

import com.gty.util.DBUtil;
import com.gty.util.POIUtil;
import org.junit.Test;

import java.util.List;

public class App {
    @Test
    public void test04() throws Exception {
        //获取表头
        List<String> titleList = DBUtil.getColumnComments();
        //获取数据
        String sql = "select * from " + DBUtil.tableName + " limit 10";
        List<List<String>> lists = DBUtil.selectRowToList(sql);
        //写入表格
        POIUtil.writeExcel(titleList,lists,"e:\\");
    }

}
