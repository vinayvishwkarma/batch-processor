package com.finago.interview.task.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;

public class Test {

	public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
		 try (InputStream input = new FileInputStream("common.properties")) {

	            Properties prop = new Properties();

	            // load a properties file
	            prop.load(input);

	            // get the property value and print it out
	            System.out.println(prop.getProperty("db.url"));
	            System.out.println(prop.getProperty("db.user"));
	            System.out.println(prop.getProperty("db.password"));

	        } catch (IOException ex) {
	            ex.printStackTrace();
	        }
}
}
