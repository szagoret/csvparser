package com.csvparser.util;


public class CSVUtil {

    public static final char DEFAULT_SEPARATOR = ';';

    // TODO: check if string is null or empty
    // TODO: find the quotes that are part of the word and escape them
    public static String[] readCSVLine(String line, char separator) {

        String[] row = line.split(String.valueOf(separator));
        for (int i = 0; i < row.length; i++) {
            row[i] = row[i].replace("\"", "");
        }
        return row;
    }
}
