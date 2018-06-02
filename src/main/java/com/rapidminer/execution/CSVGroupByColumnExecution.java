package com.rapidminer.execution;

import com.rapidminer.util.MultiMapData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class CSVGroupByColumnExecution implements Supplier<Map<String, Map<String, List<Double>>>> {

    private static final String COLUMN_NAME = "label";

    private final Map<String, List<String>> parsedCSVData;

    public CSVGroupByColumnExecution(Map<String, List<String>> parsedCSVData) {
        this.parsedCSVData = parsedCSVData;
    }

    @Override
    public Map<String, Map<String, List<Double>>> get() {

        MultiMapData<String, String, Double> result = new MultiMapData<>();

        List<String> numericalColumns = findNumericalColumns();
        List<String> labelColumn = parsedCSVData.get(COLUMN_NAME);

        if (numericalColumns.size() == 0 || labelColumn == null) {
            return result.getMap();
        }

        for (int i = 0; i < labelColumn.size(); i++) {
            String currentLabel = labelColumn.get(i);

            for (String column : numericalColumns) {
                result.put(currentLabel, column, Double.parseDouble(parsedCSVData.get(column).get(i)));
            }
        }

        return result.getMap();
    }

    private List<String> findNumericalColumns() {
        List<String> columns = new ArrayList<>();

        for (String key : parsedCSVData.keySet()) {
            String firstElement = parsedCSVData.get(key).get(0);

            try {
                Double.parseDouble(firstElement);
                columns.add(key);
            } catch (NumberFormatException e) {
            }
        }

        return columns;
    }
}
