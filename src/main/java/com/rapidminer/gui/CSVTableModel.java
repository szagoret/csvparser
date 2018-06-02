package com.rapidminer.gui;

import javax.swing.table.AbstractTableModel;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CSVTableModel extends AbstractTableModel {

    private final String[] columnNames;

    private final Map<String, List<String>> csvFileData;

    public CSVTableModel(Map<String, List<String>> csvFileData) {
        Objects.requireNonNull(csvFileData);
        this.columnNames = csvFileData.keySet().toArray(new String[0]);
        this.csvFileData = csvFileData;
    }

    @Override
    public int getRowCount() {
        return csvFileData.get(columnNames[0]).size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return csvFileData.get(columnNames[columnIndex]).get(rowIndex);
    }
}
