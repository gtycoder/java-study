package com.gty.util;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 写入一个已经存在的excel,并覆盖其中的内容
 */
public class WriteExcel {
    public static void writeExcel(List<Map<String,Object>> dataList, File file) throws Exception {
        //判断文件类型获取文件
        Workbook workbook = getWorkbook(file);

        //获取第一个工作簿
        Sheet sheet = workbook.getSheetAt(0);

        //清除表内的原有的数据
        for (int i = 0; i < sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (null!=row) {
                sheet.removeRow(row);
            }
        }
        //将数据库字段（map的key）写入第一行
        Map<String, Object> firstRowMap = dataList.get(0);
        Set<String> keySet = firstRowMap.keySet();
        Object[] keyArr = keySet.toArray();
        Row row0 = sheet.createRow(0);
        for (int i = 0; i < keyArr.length; i++) {
            row0.createCell(i).setCellValue(keyArr[i].toString());
        }
        //将数据写入其他行
        for (int i = 0; i < dataList.size(); i++) {
            //控制有多少行
            Map<String, Object> rowMap = dataList.get(i);
            Row row = sheet.createRow(i + 1);
            for (int j = 0; j < rowMap.size(); j++) {
                Cell cell = row.createCell(j);
                cell.setCellValue(null==(rowMap.get((String)keyArr[j]))?"":rowMap.get(keyArr[j]).toString());
            }
        }
        //输出文件
        FileOutputStream fos = new FileOutputStream(file);
        workbook.write(fos);
    }

    private static Workbook getWorkbook(File file) throws Exception {
        Workbook workbook = null;
        FileInputStream fis = new FileInputStream(file);
        if (file.getName().endsWith("xls")) {
            workbook = new HSSFWorkbook(fis);
        } else if (file.getName().endsWith("xlsx")) {
            workbook = new XSSFWorkbook(fis);
        } else {
            throw new Exception("格式错误");
        }
        return workbook;
    }

}
