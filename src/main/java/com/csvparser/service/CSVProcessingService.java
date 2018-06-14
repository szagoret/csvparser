package com.csvparser.service;

import com.csvparser.execution.CSVExportExecution;
import com.csvparser.execution.CSVGroupByColumnExecution;
import com.csvparser.execution.ChartSeriesExecution;
import com.csvparser.execution.WSCallExecution;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;

public class CSVProcessingService {

    public static void calculateMedian(Map<String, List<String>> parsedCSVData,
                                       String wsUrl,
                                       Consumer<List<?>> whenFinish,
                                       Function<Throwable, Void> whenError) {
        CompletableFuture
                .supplyAsync(new CSVGroupByColumnExecution(parsedCSVData))
                .thenApply(new WSCallExecution(wsUrl))
                .thenApply(new CSVExportExecution())
                .thenApply(new ChartSeriesExecution())
                .thenAccept(whenFinish)
                .exceptionally(whenError);
    }
}
