package com.gty.domain;

import lombok.Data;

import java.util.List;

@Data
public class XRow {
    private int rowIndex;
    private List<XCell> rowValue;
}
