package com.finago.interview.task;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.finago.interview.task.processor.FileProcessor;
import com.finago.interview.task.watcher.DirectoryWatcher;

/**
 * A simple main method as an example.
 */
public class BatchProcessor {

    private static final Logger logger = LoggerFactory.getLogger(BatchProcessor.class);

    public static void main(final String[] args) {
        logger.info("Batch processor starting up…");
        Path inputDir = Paths.get("data", "in");
        FileProcessor processor = new FileProcessor();
        DirectoryWatcher watcher = new DirectoryWatcher(inputDir, processor);
        watcher.start();
    }
}
