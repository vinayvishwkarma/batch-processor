package com.finago.interview.task.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;

import com.finago.interview.task.constant.FileConstant;
import com.finago.interview.task.constant.FilePathConstant;

public class AppUtil {

	static Logger logger = Logger.getLogger(AppUtil.class);

	public static String getPath(String relativePath) throws IOException {

		if (relativePath.equals(FileConstant.FILE_FOLDER_IN)) {
			return FilePathConstant.DATA_IN_FOLDER;

		} else if (relativePath.equals(FileConstant.FILE_FOLDER_OUT)) {
			return FilePathConstant.DATA_OUT_FOLDER;

		} else if (relativePath.equals(FileConstant.FILE_FOLDER_ERROR)) {
			return FilePathConstant.DATA_ERROR_FOLDER;

		} else if (relativePath.equals(FileConstant.FILE_FOLDER_ARCHIVE)) {
			return FilePathConstant.DATA_ARCHIVE_FOLDER;
		}

		return null;
	}

	public static boolean isPdfCorrupted(String fileName, String checksum)
			throws NoSuchAlgorithmException, IOException {

		String path = FilePathConstant.DATA_IN_FOLDER + fileName;

		String md5Checksum = null;

		try (InputStream inputStream = Files.newInputStream(Paths.get(path))) {
			if (inputStream != null) {
				md5Checksum = org.apache.commons.codec.digest.DigestUtils.md5Hex(inputStream);
			} else {
				return true;
			}
		} catch (Exception e) {
			logger.info(e.getMessage());
			return true;
		}
		if (md5Checksum.equals(checksum)) {
			return false;
		}
		return true;

	}

	public static boolean isValidXML(String xmlFile) throws IOException {
		String source = FilePathConstant.DATA_IN_FOLDER + xmlFile;

		File fXmlFile = new File(source);
		DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();

		try {
			DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
			documentBuilder.parse(fXmlFile);
		} catch (Exception e) {
			logger.info("Xml file" + xmlFile + " is invalid ", e);
			return false;
		}
		return true;
	}

	public static String makeDirectory(int receiverID, String folder) throws IOException {

		int mod = receiverID % 100;
		String path = getPath(folder) + mod + "/" + receiverID + "/";
		Files.createDirectories(Paths.get(path));
		return path;

	}

	public static boolean isFile(String fileName) throws IOException {

		String filePath = FilePathConstant.DATA_IN_FOLDER + fileName;
		File file = new File(filePath);
		return (file.exists() && !file.isDirectory());

	}

}