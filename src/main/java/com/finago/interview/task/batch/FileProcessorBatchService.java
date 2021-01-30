package com.finago.interview.task.batch;

import com.finago.interview.task.model.Receivers;
import java.io.FileNotFoundException;
import javax.xml.bind.JAXBException;

public interface FileProcessorBatchService {
	
	boolean copyFileFromSourceToTarget(String source, String target);

	Receivers getReceiverBlocks(String xmlFilename) throws JAXBException, FileNotFoundException;

}
