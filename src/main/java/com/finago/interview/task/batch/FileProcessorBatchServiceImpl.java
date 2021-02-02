package com.finago.interview.task.batch;

import java.io.File;
import java.io.FileNotFoundException;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import com.finago.interview.task.model.Receiver;
import com.finago.interview.task.model.Receivers;
import com.finago.interview.task.util.AppUtil;

public class FileProcessorBatchServiceImpl implements FileProcessorBatchService {

	static Logger logger = Logger.getLogger(FileProcessorBatchServiceImpl.class);

	@Override
	public boolean copyFileFromSourceToTarget(String source, String target) {
		Path result = null;
		try {
			result = Files.copy(Paths.get(source), Paths.get(target));
		} catch (IOException e) {
			logger.error("Exception while moving file: " + e.getMessage(), e);
		}
		if (result != null) {
			return true;
		}
		return false;
	}

	@Override
	public Receivers getReceiverBlocks(String xmlFilename) throws JAXBException, FileNotFoundException {
		return AppUtil.unmarshall(xmlFilename);
	}

	@Override
	public boolean moveFileSourceToTarget(String source, String target) {
		Path result = null;

		try {
			result = Files.move(Paths.get(source), Paths.get(target));
		} catch (IOException e) {
			logger.error("Exception while moving file: ", e);
		}
		if (result != null) {
			return true;
		}
		return false;

	}

	@Override
	public void deletePdfFiles(File directory) {
		try {
			FileUtils.cleanDirectory(directory);
		} catch (IOException e) {
			logger.error("file can be deleted ", e);
		}
	}

	public static void moveFile(FileProcessorBatchService service, Receiver receiver, String sourceDirectory,
			String targetDirectory) throws IOException {
		String source = AppUtil.getPath(sourceDirectory) + receiver.getFileName();
		String target = AppUtil.generateDirectory(receiver.getReceiver_id(), targetDirectory) + receiver.getFileName();
		service.copyFileFromSourceToTarget(source, target);
	}

	public static void moveFile(FileProcessorBatchService service, String xmlFilename, String sourceDirectory,
			String targetDirectory, String method) throws IOException {

		String source = AppUtil.getPath(sourceDirectory) + xmlFilename;
		String target = AppUtil.getPath(targetDirectory) + xmlFilename;

		if (method.equalsIgnoreCase(FileMovingMethodConstant.FILE_MOVE)) {
			service.moveFileSourceToTarget(source, target);
		} else if (method.equalsIgnoreCase(FileMovingMethodConstant.FILE_COPY)) {
			service.copyFileFromSourceToTarget(source, target);

		}
	}

	@Override
	public void convertToXML(Receiver receiver, String folder) {
		try {
			JAXBContext context = JAXBContext.newInstance(Receiver.class);

			Marshaller marshaller = context.createMarshaller();

			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

			String outputFile = AppUtil.generateDirectory(receiver.getReceiver_id(), folder)
					+ receiver.getFileName().substring(0, receiver.getFileName().indexOf(".")) + ".xml";

			marshaller.marshal(receiver, new File(outputFile));

		} catch (Exception e) {
			logger.error("convertToXML failed ", e);
		}
	}

}
