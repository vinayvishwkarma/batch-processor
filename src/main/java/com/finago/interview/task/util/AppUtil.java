package com.finago.interview.task.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;
import org.apache.log4j.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import com.finago.interview.task.batch.FileMovingMethodConstant;
import com.finago.interview.task.model.Receivers;

public class AppUtil {

	static Logger logger = Logger.getLogger(AppUtil.class);

	public static String getPath(String relativePath) throws IOException {

		try (InputStream inputStream = Files.newInputStream(Paths.get("resources/common.properties"))) {

			Properties prop = new Properties();
			prop.load(inputStream);

			if (relativePath.equals(FileMovingMethodConstant.FILE_FOLDER_IN)) {
				return prop.getProperty("data.in.folder");

			} else if (relativePath.equals(FileMovingMethodConstant.FILE_FOLDER_OUT)) {
				return prop.getProperty("data.out.folder");

			} else if (relativePath.equals(FileMovingMethodConstant.FILE_FOLDER_ERROR)) {
				return prop.getProperty("data.error.folder");

			} else if (relativePath.equals(FileMovingMethodConstant.FILE_FOLDER_ARCHIVE)) {
				return prop.getProperty("data.archive.folder");
			}

		}

		return null;
	}

	public static boolean isPdfCorrupted(String fileName, String checksum)
			throws NoSuchAlgorithmException, IOException {

		String path = getPath(FileMovingMethodConstant.FILE_FOLDER_IN) + fileName;
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

	public static boolean isXMLFileValid(String xmlFile) throws IOException {
		String source = getPath("in") + xmlFile;

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

	public static Receivers unmarshall(String xmlFilename) throws JAXBException, FileNotFoundException {
		JAXBContext context = JAXBContext.newInstance(Receivers.class);
		return (Receivers) context.createUnmarshaller().unmarshal(new FileReader("data/in/" + xmlFilename));
	}

	public static String generateDirectory(int receiverID, String folder) throws IOException {

		int mod = receiverID % 100;
		String path = getPath(folder) + mod + "/" + receiverID + "/";
		Files.createDirectories(Paths.get(path));
		return path;

	}

	public static boolean isFile(String fileName) throws IOException {

		String filePath = getPath(FileMovingMethodConstant.FILE_FOLDER_IN) + fileName;
		File file = new File(filePath);
		return (file.exists() && !file.isDirectory());

	}

}