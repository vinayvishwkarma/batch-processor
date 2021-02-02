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

import com.finago.interview.task.batch.constant.FileConstant;
import com.finago.interview.task.model.Receiver;
import com.finago.interview.task.model.Receivers;
import com.finago.interview.task.util.AppUtil;

public class BatchProcessorHelper {

	static Logger logger = Logger.getLogger(BatchProcessorHelper.class);

	public boolean doWork() throws NoSuchAlgorithmException, IOException, InterruptedException, JAXBException {

		WatchService watchService = FileSystems.getDefault().newWatchService();
		Path inputDirectory = Path.of(AppUtil.getPath(FileConstant.FILE_FOLDER_IN));

		FileProcessorBatchService service = new FileProcessorBatchServiceImpl();

		inputDirectory.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);

		WatchKey key;

		while ((key = watchService.take()) != null) {
			for (WatchEvent<?> event : key.pollEvents()) {
				String xmlFilename = event.context().toString();
				if (xmlFilename.contains(".xml")) {
					if (AppUtil.isXMLFileValid(xmlFilename)) {
						Receivers receivers = service.getReceivers(xmlFilename);
						for (Receiver receiver : receivers.getReceiver()) {
							if (AppUtil.isFile(receiver.getFileName())) {

								if (!AppUtil.isPdfCorrupted(receiver.getFileName(), receiver.getFileMD5Checksum())) {

									service.moveFile(service, receiver, FileConstant.FILE_FOLDER_IN,
											FileConstant.FILE_FOLDER_OUT);

									service.createXMLFile(receiver, FileConstant.FILE_FOLDER_OUT);

								} else {
									service.moveFile(service, receiver, FileConstant.FILE_FOLDER_IN,
											FileConstant.FILE_FOLDER_ERROR);

									service.createXMLFile(receiver, FileConstant.FILE_FOLDER_ERROR);

								}
							} else {

								service.createXMLFile(receiver, FileConstant.FILE_FOLDER_ERROR);

							}
						}

					} else {
						service.moveFile(service, xmlFilename, FileConstant.FILE_FOLDER_IN,
								FileConstant.FILE_FOLDER_ERROR, FileConstant.FILE_MOVE);
					}

					service.moveFile(service, xmlFilename, FileConstant.FILE_FOLDER_IN,
							FileConstant.FILE_FOLDER_ARCHIVE, FileConstant.FILE_MOVE);

				}
			}
			service.deletePdfFiles(new File(AppUtil.getPath(FileConstant.FILE_FOLDER_IN)));
			logger.info("pdf Files are deleted from in folder");
			key.reset();
		}

		return true;

	}

}
