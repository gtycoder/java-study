package com.gty.util;

import com.gty.domain.XCell;
import com.gty.domain.XRow;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

public class ReadExcel {

    /**
     * 获取第一行的数据,作为数据库字段的注释
     */
    public static XRow getFirstRowData(File file) throws Exception {

        if (!file.exists()) {
            return null;
        }

        //获取 work
        FileInputStream fis = new FileInputStream(file);
        Workbook workbook = new HSSFWorkbook(fis);
        //只取第一个sheet
        Sheet sheet = workbook.getSheetAt(0);
        //第一行的数据封装
        XRow firstRow = new XRow();
        //取第一行
        Row row = sheet.getRow(sheet.getFirstRowNum());
        firstRow.setRowIndex(sheet.getFirstRowNum()+1);
        //取出头和尾
        short firstCellNum = row.getFirstCellNum();
        short lastCellNum = row.getLastCellNum();
        List<XCell> cells = new ArrayList<>();
        //取出所有的cell
        for (int i = firstCellNum; i < lastCellNum; i++) {
            Cell cell = row.getCell(i);
            //单元格的数据封装
            XCell xCell = new XCell();
            xCell.setCellIndex(i+1);
            //暂时使用string类型测试,可以详细的进行value分类
            xCell.setValue(cell.getStringCellValue());
            cells.add(xCell);
        }
        firstRow.setRowValue(cells);

        //遍历查看
        System.out.println("=====rowIndex===="+firstRow.getRowIndex());
        List<XCell> rowValue = firstRow.getRowValue();
        for (XCell xCell : rowValue) {
            System.out.println(xCell);
        }

        return firstRow;
    }

    /**
     * 以第一行为宽度获取其余的数据
     */
    public static List<XRow> getOtherData(File file,XRow firstRow) throws Exception {
        //所有的行数据
        List<XRow> rows = new ArrayList<>();

        //获取 work
        FileInputStream fis = new FileInputStream(file);
        Workbook workbook = new HSSFWorkbook(fis);
        Sheet sheet = workbook.getSheetAt(0);
        int firstRowNum = sheet.getFirstRowNum();
        int lastRowNum = sheet.getLastRowNum();
        //第一行的列数
        int columnSize = firstRow.getRowValue().size();
        int firstCellIndex = firstRow.getRowValue().get(0).getCellIndex();
        int lastCellIndex = firstRow.getRowValue().get(columnSize-1).getCellIndex();
        //去掉第一行
        for (int i = firstRowNum+1; i < lastRowNum+1; i++) {
            //遍历所有的行
            XRow xRow = new XRow();
            xRow.setRowIndex(i+1);
            Row row = sheet.getRow(i);
            List<XCell> cells = new ArrayList<>();
            //以第一行为宽度,遍历所有的cell
            for (int j = firstCellIndex-1; j < lastCellIndex; j++) {
                Cell cell = row.getCell(j);
                XCell xCell = new XCell();
                xCell.setCellIndex(j+1);
                //暂时使用string类型测试,可以详细的进行value分类
                xCell.setValue(cell.getStringCellValue());
                cells.add(xCell);
            }
            xRow.setRowValue(cells);
            rows.add(xRow);
        }

        //遍历查看
        for (XRow row : rows) {
            int rowIndex = row.getRowIndex();
            List<XCell> rowValue = row.getRowValue();
            System.out.println("======rowIndex==="+rowIndex);
            for (XCell xCell : rowValue) {
                System.out.println(xCell);
            }
        }
        return rows;
    }
}

