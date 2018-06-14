package com.csvparser.execution;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import static com.csvparser.util.CSVUtil.DEFAULT_SEPARATOR;
import static com.csvparser.util.CSVUtil.readCSVLine;

public class CSVParserExecution implements Supplier<Map<String, List<String>>> {


    private final File file;
    private final Map<String, List<String>> dataCollector;
    private String[] header;

    public CSVParserExecution(File file) {
        this.file = file;
        dataCollector = new LinkedHashMap<>();
    }

    @Override
    public Map<String, List<String>> get() {

        Boolean firstRow = true;


        // parse all rows from the file
        // put each value according to map key (column)
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {

            String line;

            while ((line = br.readLine()) != null) {

                String[] row = readCSVLine(line, DEFAULT_SEPARATOR);

                if (firstRow) {
                    initCollection(row);
                    firstRow = false;
                    continue;
                }

                // TODO: check of number of tokens is equal with number of columns
                for (int i = 0; i < row.length; i++) {
                    dataCollector.get(header[i]).add(row[i]);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


        return dataCollector;
    }

    private void initCollection(String[] header) {

        // set columns names to an array
        this.header = header;

        // init all keys (columns) with an empty list
        // this is used later to add values for each column
        for (int i = 0; i < this.header.length; i++) {
            dataCollector.put(this.header[i], new ArrayList<>());
        }

    }


}
