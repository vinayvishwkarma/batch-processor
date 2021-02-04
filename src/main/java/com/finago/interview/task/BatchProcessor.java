package com.finago.interview.task;

import com.finago.interview.task.batch.BatchServiceImpl;

public class BatchProcessor {

	public static void main(String[] args) {
		System.out.println("*beep boop* ...processing data... *beep boop*");

		new BatchServiceImpl().process();

	}

}
