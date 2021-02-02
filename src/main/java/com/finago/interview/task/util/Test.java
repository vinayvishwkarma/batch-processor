package com.finago.interview.task.util;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import org.apache.log4j.Logger;
 
 
public class Test {
	

	
 	static Logger logger = Logger.getLogger(Test.class);

	public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
		
 
        logger.info("logger is working ");
        logger.debug("debug is working ");
        logger.error("error is working ");



 
	}
}
