package com.finago.interview.task;

import com.finago.interview.task.util.AppUtil;
import java.io.IOException;
import java.nio.file.*;

/**
 * A simple main method as an example.
 */

public class BatchProcessor {

    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println("*beep boop* ...processing data... *beep boop*");

        WatchService watchService = FileSystems.getDefault().newWatchService();
        Path inputDirectory = Path.of(AppUtil.getPath("in"));

        inputDirectory.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);

        WatchKey key;

        while ((key = watchService.take()) != null) {
            for (WatchEvent<?> event : key.pollEvents()) {
                System.out.println("Event kind " + event.kind() + ". File affected: " + event.context());
                AppUtil.isXMLFileValid((String) event.context());
            }
            key.reset();
        }
    }

}
