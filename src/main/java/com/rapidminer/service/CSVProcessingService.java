package com.rapidminer.service;

import com.rapidminer.execution.CSVExportExecution;
import com.rapidminer.execution.CSVGroupByColumnExecution;
import com.rapidminer.execution.ChartSeriesExecution;
import com.rapidminer.execution.WSCallExecution;

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
