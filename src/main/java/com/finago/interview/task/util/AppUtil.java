package com.finago.interview.task.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;

class AppUtil

{
	public static String getPath(String relativePath) throws IOException {

		try (InputStream inputStream = Files.newInputStream(Paths.get("/batch-processor/resources/common.properties"))) {

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
			return true;
		}

		return false;

	}

}