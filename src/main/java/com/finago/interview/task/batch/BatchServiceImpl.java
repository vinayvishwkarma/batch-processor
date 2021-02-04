package com.finago.interview.task.batch;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.stream.Collectors;

import javax.xml.bind.JAXBException;

import org.apache.log4j.Logger;

import com.finago.interview.task.constant.FileConstant;
import com.finago.interview.task.constant.FilePathConstant;
import com.finago.interview.task.model.Receiver;
import com.finago.interview.task.model.Receivers;
import com.finago.interview.task.util.AppUtil;

public class BatchServiceImpl implements BatchService {

	static Logger logger = Logger.getLogger(BatchServiceImpl.class);

	@Override
	public void preProcess() {

		List<String> files;
		try {
			files = Files.list(Paths.get(FilePathConstant.DATA_IN_FOLDER)).map(s -> s.toString())
					.filter(s -> s.toString().contains(".xml")).map(s -> s.substring(8)).collect(Collectors.toList());
			for (String file : files) {
				doOneUnitWork(file);

			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();

		}
		postProcess();

	}

	@Override
	public void process() {

		try {
			preProcess();
			doWork();
		} catch (NoSuchAlgorithmException | IOException | InterruptedException | JAXBException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

	public boolean doWork() throws NoSuchAlgorithmException, IOException, InterruptedException, JAXBException {

		WatchService watchService = FileSystems.getDefault().newWatchService();
		Path inputDirectory = Path.of(FilePathConstant.DATA_IN_FOLDER);

		inputDirectory.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);

		WatchKey key = null;

		while ((key = watchService.take()) != null) {
			System.out.println("inside watchService loop");

			for (WatchEvent<?> event : key.pollEvents()) {

				String xmlFilename = event.context().toString();
				if (xmlFilename.contains(".xml")) {
					doOneUnitWork(xmlFilename);

				}
			}
			postProcess();
			logger.info("pdf Files are deleted from in folder");
			key.reset();
		}

		return true;

	}

	@Override
	public void postProcess() {

		BatchUtil.deletePdfFiles(new File(FilePathConstant.DATA_IN_FOLDER));

	}

	private void doOneUnitWork(String xmlFilename)
			throws IOException, JAXBException, FileNotFoundException, NoSuchAlgorithmException {
		if (AppUtil.isValidXML(xmlFilename)) {
			Receivers receivers = BatchUtil.getReceivers(xmlFilename);
			for (Receiver receiver : receivers.getReceiver()) {
				if (AppUtil.isFile(receiver.getFileName())) {

					if (!AppUtil.isPdfCorrupted(receiver.getFileName(), receiver.getFileMD5Checksum())) {

						BatchUtil.moveFile(receiver, FileConstant.FILE_FOLDER_IN, FileConstant.FILE_FOLDER_OUT);

						BatchUtil.createXMLFile(receiver, FileConstant.FILE_FOLDER_OUT);

					} else {
						BatchUtil.moveFile(receiver, FileConstant.FILE_FOLDER_IN, FileConstant.FILE_FOLDER_ERROR);

						BatchUtil.createXMLFile(receiver, FileConstant.FILE_FOLDER_ERROR);

					}
				} else {

					BatchUtil.createXMLFile(receiver, FileConstant.FILE_FOLDER_ERROR);

				}
			}

		} else {
			BatchUtil.moveFile(xmlFilename, FileConstant.FILE_FOLDER_IN, FileConstant.FILE_FOLDER_ERROR,
					FileConstant.FILE_MOVE);
		}

		BatchUtil.moveFile(xmlFilename, FileConstant.FILE_FOLDER_IN, FileConstant.FILE_FOLDER_ARCHIVE,
				FileConstant.FILE_MOVE);

	}

}
