package com.finago.interview.task.batch;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import com.finago.interview.task.constant.FileConstant;
import com.finago.interview.task.model.Receiver;
import com.finago.interview.task.model.Receivers;
import com.finago.interview.task.util.AppUtil;

public class BatchUtil {

	static Logger logger = Logger.getLogger(BatchUtil.class);

	public static boolean copyFileFromSourceToTarget(String source, String target) {
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

	public static Receivers getReceivers(String xmlFilename) throws JAXBException, FileNotFoundException {
		JAXBContext context = JAXBContext.newInstance(Receivers.class);
		return (Receivers) context.createUnmarshaller().unmarshal(new FileReader("data/in/" + xmlFilename));
	}

	public static boolean moveFileSourceToTarget(String source, String target) {
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

	public static void deletePdfFiles(File directory) {
		try {
			FileUtils.cleanDirectory(directory);
		} catch (IOException e) {
			logger.error("file can be deleted ", e);
		}
	}

	public static void moveFile(Receiver receiver, String sourceDirectory, String targetDirectory) throws IOException {
		String source = AppUtil.getPath(sourceDirectory) + receiver.getFileName();
		String target = AppUtil.makeDirectory(receiver.getReceiver_id(), targetDirectory) + receiver.getFileName();
		copyFileFromSourceToTarget(source, target);
	}

	public static void moveFile(String xmlFilename, String sourceDirectory, String targetDirectory, String method)
			throws IOException {

		String source = AppUtil.getPath(sourceDirectory) + xmlFilename;
		String target = AppUtil.getPath(targetDirectory) + xmlFilename;

		if (method.equalsIgnoreCase(FileConstant.FILE_MOVE)) {
			moveFileSourceToTarget(source, target);
		} else if (method.equalsIgnoreCase(FileConstant.FILE_COPY)) {
			copyFileFromSourceToTarget(source, target);

		}
	}

	public static void createXMLFile(Receiver receiver, String folder) {
		try {
			JAXBContext context = JAXBContext.newInstance(Receiver.class);

			Marshaller marshaller = context.createMarshaller();

			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

			String outputFile = AppUtil.makeDirectory(receiver.getReceiver_id(), folder)
					+ receiver.getFileName().substring(0, receiver.getFileName().indexOf(".")) + ".xml";

			marshaller.marshal(receiver, new File(outputFile));

		} catch (Exception e) {
			logger.error("convertToXML failed ", e);
		}
	}

}
