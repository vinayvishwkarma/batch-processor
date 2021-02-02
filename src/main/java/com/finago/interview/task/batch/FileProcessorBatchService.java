package com.finago.interview.task.batch;

import com.finago.interview.task.model.Receiver;
import com.finago.interview.task.model.Receivers;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import javax.xml.bind.JAXBException;

public interface FileProcessorBatchService {

	boolean copyFileFromSourceToTarget(String source, String target);

	Receivers getReceiverBlocks(String xmlFilename) throws JAXBException, FileNotFoundException;

	boolean moveFileSourceToTarget(String source, String target);

	void deletePdfFiles(File directory);

	void convertToXML(Receiver receiver, String folder);

}
