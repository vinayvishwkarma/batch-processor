package com.finago.interview.task.batch;

import com.finago.interview.task.model.Receiver;
import com.finago.interview.task.model.Receivers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import javax.xml.bind.JAXBException;

public interface FileProcessorBatchService {

	boolean copyFileFromSourceToTarget(String source, String target);

	Receivers getReceivers(String xmlFilename) throws JAXBException, FileNotFoundException;

	boolean moveFileSourceToTarget(String source, String target);

	void deletePdfFiles(File directory);

	void createXMLFile(Receiver receiver, String folder);
	
	public void moveFile(FileProcessorBatchService service, Receiver receiver, String sourceDirectory,
			String targetDirectory) throws IOException;
	
	public void moveFile(FileProcessorBatchService service, String xmlFilename, String sourceDirectory,
			String targetDirectory, String method) throws IOException;

}
