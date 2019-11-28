package com.gty.util;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

public class POIUtil {

    public static void writeExcel(List<String> titleList,List<List<String>> dataList, String filePath) throws Exception {
        //创建文件
        File path = new File(filePath);
        if (!path.exists()) {
            path.mkdirs();
        }
        String fileName = System.currentTimeMillis() + ".xlsx";
        File file = new File(path, fileName);

        //创建一个excel对象
        Workbook workbook = new XSSFWorkbook();
        //创建第一个工作簿
        Sheet sheet = workbook.createSheet("dbName");
        //将数据库字段（map的key）写入第一行
        Row row0 = sheet.createRow(0);
        for (int i = 0; i < titleList.size(); i++) {
            //创建单元格并写入数据
            Cell cell = row0.createCell(i);
            cell.setCellValue(titleList.get(i).toString());
        }

        //将数据写入其他行
        for (int i = 0; i < dataList.size(); i++) {
            List<String> rowList = dataList.get(i);
            //控制有多少行
            Row row = sheet.createRow(i + 1);
            for (int j = 0; j < rowList.size(); j++) {
                //控制列数
                Cell cell = row.createCell(j);
                String o = rowList.get(j);
                //除掉空值,也可以写入对应类型,此处直接写入string
                cell.setCellValue(null==o?"":o);
            }
        }

        //输出文件
        FileOutputStream fos = new FileOutputStream(file);
        workbook.write(fos);
    }

}
