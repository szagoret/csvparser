package com.csvparser.service;

import com.csvparser.execution.CSVParserExecution;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CSVParserService {

    private static final Logger logger = Logger.getGlobal();

    public static void parseAndConsume(File csvFile, Consumer<Map<String, List<String>>> resultConsumer) {
        CompletableFuture.supplyAsync(new CSVParserExecution(csvFile))
                .thenAccept(resultConsumer)
                .exceptionally(e ->
                {
                    logger.log(Level.SEVERE, e.getMessage());
                    return null;
                });
    }
}