package com.finago.interview.task.batch;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileProcessorBatchServiceImpl implements FileProcessorBatchService {

	@Override
	public boolean copyFileFromSourceToTarget(String source, String target) {
		Path result = null;
		try {
			result = Files.copy(Paths.get(source), Paths.get(target));
		} catch (IOException e) {
			System.out.println("Exception while moving file: " + e.getMessage());
		}
		if (result != null) {
			return true;
		}
		return false;
	}
}