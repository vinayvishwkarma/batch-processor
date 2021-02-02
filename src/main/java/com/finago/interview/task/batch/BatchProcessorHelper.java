package com.finago.interview.task.batch;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.security.NoSuchAlgorithmException;

import javax.xml.bind.JAXBException;

import org.apache.log4j.Logger;

import com.finago.interview.task.model.Receiver;
import com.finago.interview.task.model.Receivers;
import com.finago.interview.task.util.AppUtil;

public class BatchProcessorHelper {

	static Logger logger = Logger.getLogger(BatchProcessorHelper.class);

	public boolean doWork() throws NoSuchAlgorithmException, IOException, InterruptedException, JAXBException {

		WatchService watchService = FileSystems.getDefault().newWatchService();
		Path inputDirectory = Path.of(AppUtil.getPath(FileMovingMethodConstant.FILE_FOLDER_IN));

		FileProcessorBatchService service = new FileProcessorBatchServiceImpl();

		inputDirectory.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);

		WatchKey key;

		while ((key = watchService.take()) != null) {
			for (WatchEvent<?> event : key.pollEvents()) {
				String xmlFilename = event.context().toString();
				if (xmlFilename.contains(".xml")) {
					if (AppUtil.isXMLFileValid(xmlFilename)) {
						Receivers receivers = AppUtil.unmarshall(xmlFilename);
						for (Receiver receiver : receivers.getReceiver()) {
							if (AppUtil.isFile(receiver.getFileName())) {

								if (AppUtil.isPdfCorrupted(receiver.getFileName(), receiver.getFileMD5Checksum())) {
									FileProcessorBatchServiceImpl.moveFile(service, receiver,
											FileMovingMethodConstant.FILE_FOLDER_IN,
											FileMovingMethodConstant.FILE_FOLDER_ERROR);

									service.convertToXML(receiver, "error");

								} else {
									FileProcessorBatchServiceImpl.moveFile(service, receiver,
											FileMovingMethodConstant.FILE_FOLDER_IN,
											FileMovingMethodConstant.FILE_FOLDER_OUT);

									service.convertToXML(receiver, "out");

								}
							} else {

								service.convertToXML(receiver, "error");

							}
						}

					} else {
						FileProcessorBatchServiceImpl.moveFile(service, xmlFilename,
								FileMovingMethodConstant.FILE_FOLDER_IN, FileMovingMethodConstant.FILE_FOLDER_ERROR,
								FileMovingMethodConstant.FILE_MOVE);
					}

					FileProcessorBatchServiceImpl.moveFile(service, xmlFilename,
							FileMovingMethodConstant.FILE_FOLDER_IN, FileMovingMethodConstant.FILE_FOLDER_ARCHIVE,
							FileMovingMethodConstant.FILE_MOVE);

				}
			}
			service.deletePdfFiles(new File(AppUtil.getPath(FileMovingMethodConstant.FILE_FOLDER_IN)));
			logger.info("pdf Files are deleted from in folder");
			key.reset();
		}

		return true;

	}

}
