package com.finago.interview.task;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import javax.xml.bind.JAXBException;

import com.finago.interview.task.batch.BatchProcessorHelper;

public class BatchProcessor {

	public static void main(String[] args) throws NoSuchAlgorithmException, IOException, InterruptedException, JAXBException {
		System.out.println("*beep boop* ...processing data... *beep boop*");

		new BatchProcessorHelper().doWork();
	}

	

}
