package com.rapidminer.execution;

import com.rapidminer.util.CSVUtil;
import javafx.scene.chart.XYChart;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

import static com.rapidminer.util.CSVUtil.DEFAULT_SEPARATOR;

public class ChartSeriesExecution implements Function<File, List<XYChart.Series<String, Number>>> {

    @Override
    public List<XYChart.Series<String, Number>> apply(File file) {

        List<XYChart.Series<String, Number>> seriesList = new LinkedList<>();

        List<String[]> fileContent = getFileContent(file);

        // skip last column (label)
        for (int j = 0; j < fileContent.get(0).length - 1; j++) {

            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName(fileContent.get(0)[j]);

            // skip first row
            for (int i = 1; i < fileContent.size(); i++) {
                String[] currentRow = fileContent.get(i);
                series.getData().add(new XYChart.Data<>(currentRow[currentRow.length - 1], Double.parseDouble(currentRow[j])));
            }

            seriesList.add(series);
        }

        return seriesList;
    }

    private List<String[]> getFileContent(File file) {
        List<String[]> fileContent = new LinkedList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;

            while ((line = br.readLine()) != null) {

                String[] row = CSVUtil.readCSVLine(line, DEFAULT_SEPARATOR);
                fileContent.add(row);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return fileContent;
    }
}
