package com.finago.interview.task.util;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

 
public class Test {
	
///batch-processor/resources/log4j.properties
 /*static Logger logger = Logger.getLogger(Test.class);
 static final String path = "resources/log4j.properties";*/
	
 	static Logger logger = Logger.getLogger(Test.class);

	public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
		
 
        logger.info("logger is working ");

 
	}
}
