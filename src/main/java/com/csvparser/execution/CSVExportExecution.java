package com.csvparser.execution;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CSVExportExecution implements Function<Map<String, Map<String, Double>>, File> {

    private static final String DEFAULT_SEPARATOR = ";";
    private static final String DEFAULT_FILE_NAME = "WSMedianResponse.csv";

    private final String defaultColumnName = "label";

    @Override
    public File apply(Map<String, Map<String, Double>> stringDoubleMap) {
        final File resultFile = Paths.get(".", DEFAULT_FILE_NAME).toAbsolutePath().normalize().toFile();
        try {
            final FileWriter writer = new FileWriter(resultFile);

            // write header
            // use first element in the map to extract the columns
            Set<String> setColumns = stringDoubleMap.entrySet().stream().findFirst().get().getValue().keySet();

            List<String> columns = new ArrayList<>(setColumns);
            columns.add(defaultColumnName);

            writeLine(writer, columns);

            // iterate through all labels (rows) and extract the rows
            for (Map.Entry<String, Map<String, Double>> columnEntry : stringDoubleMap.entrySet()) {


                // iterate through all columns of current label and extract values
                List<String> valuesRow = columnEntry.getValue().entrySet().stream()
                        .map(Map.Entry::getValue)
                        .map(String::valueOf)
                        .collect(Collectors.toList());

                // add label value
                valuesRow.add(columnEntry.getKey());

                // write row in the file
                writeLine(writer, valuesRow);

            }
            writer.flush();
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return resultFile;
    }

    private void writeLine(Writer w, List<String> values) throws IOException {
        String row = String.join(DEFAULT_SEPARATOR, values);
        w.append(row).append("\n");
    }
}
