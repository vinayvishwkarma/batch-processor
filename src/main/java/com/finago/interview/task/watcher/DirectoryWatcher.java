package com.finago.interview.task.watcher;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.finago.interview.task.processor.FileProcessor;

/**
 * Watches a directory for newly created XML files and triggers processing.
 */
public class DirectoryWatcher {

    private static final Logger logger = LoggerFactory.getLogger(DirectoryWatcher.class);

    private final Path inputDir;

    private final FileProcessor fileProcessor;

    private volatile boolean running = true;

    public DirectoryWatcher(final Path inputDir, final FileProcessor fileProcessor) {
        this.inputDir = inputDir;
        this.fileProcessor = fileProcessor;
    }

    /**
     * Starts watching the directory in the current thread. Blocks until stopped.
     */
    public void start() {
        logger.info("Watching directory: {}", inputDir.toAbsolutePath());
        try (WatchService watchService = FileSystems.getDefault().newWatchService()) {
            inputDir.register(watchService, ENTRY_CREATE);
            while (running) {
                WatchKey key;
                try {
                    key = watchService.take();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    logger.warn("Directory watcher interrupted, stopping.", e);
                    break;
                }
                for (WatchEvent<?> event : key.pollEvents()) {
                    if (event.kind() == ENTRY_CREATE) {
                        Path created = inputDir.resolve((Path) event.context());
                        logger.info("Detected new file: {}", created.getFileName());
                        if (created.toString().toLowerCase().endsWith(".xml")) {
                            logger.info("Processing XML: {}", created.getFileName());
                            try {
                                fileProcessor.processXml(created);
                            } catch (Exception ex) {
                                logger.error("Error processing XML: {}", created.getFileName(), ex);
                            }
                        }
                    }
                }
                boolean valid = key.reset();
                if (!valid) {
                    logger.warn("Watch key no longer valid; stopping watcher.");
                    break;
                }
            }
        } catch (final IOException e) {
            logger.error("IO error in directory watcher", e);
        }
    }

    /**
     * Stops the watcher loop; thread will exit after current cycle.
     */
    public void stop() {
        running = false;
    }
}
