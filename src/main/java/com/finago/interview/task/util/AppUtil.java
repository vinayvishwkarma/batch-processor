package com.finago.interview.task.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;
import org.w3c.dom.Document;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

class AppUtil

{
	public static String getPath(String relativePath) throws IOException {

		try (InputStream inputStream = Files.newInputStream(Paths.get("resources/common.properties"))) {

			Properties prop = new Properties();

			prop.load(inputStream);

			if (relativePath.equals("in")) {
				return prop.getProperty("data.in.folder");

			} else if (relativePath.equals("out")) {
				return prop.getProperty("data.out.folder");

			} else if (relativePath.equals("error")) {
				return prop.getProperty("data.error.folder");

			} else if (relativePath.equals("archive")) {
				return prop.getProperty("data.archive.folder");
			}

		}

		return null;
	}

	public static boolean isCorrupted(String fileName, String checksum) throws NoSuchAlgorithmException, IOException {

		String path = getPath("in") + fileName;
		String md5Checksum = null;

		try (InputStream inputStream = Files.newInputStream(Paths.get(path))) {
			md5Checksum = org.apache.commons.codec.digest.DigestUtils.md5Hex(inputStream);
		}

		if (md5Checksum.equals(checksum)) {
			return false;
		}

		return true;

	}

	public static boolean isXMLFileValid(String xmlFile) throws IOException {

		String source =getPath("in") + xmlFile;
		try {
			File fXmlFile = new File(source);
			DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
			Document document = documentBuilder.parse(fXmlFile);
		} catch (Exception e) {
			String target=getPath("error") + xmlFile;
 			moveFile(source, target);
			return false;
		}

		return true;
	}

	
	private static void moveFile(String src, String dest ) {
	      Path result = null;
	      try {
	         result = Files.copy(Paths.get(src), Paths.get(dest));
	      } catch (IOException e) {
	         System.out.println("Exception while moving file: " + e.getMessage());
	      }
	      if(result != null) {
	         System.out.println("File moved successfully.");
	      }else{
	         System.out.println("File movement failed.");
	      }
	   }

}