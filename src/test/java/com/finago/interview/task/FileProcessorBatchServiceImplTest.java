package com.finago.interview.task;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.finago.interview.task.batch.FileProcessorBatchServiceImpl;

public class FileProcessorBatchServiceImplTest {
	
	@Test
	public void copyFromSourceToTarget() {
		
		
		boolean status = new FileProcessorBatchServiceImpl().copyFileFromSourceToTarget(source, target);
		assertEquals(true, status);
	}


}
