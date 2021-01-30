package com.finago.interview.task.util;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class Test {

	public static void main(String[] args) throws IOException, NoSuchAlgorithmException {

		System.out.println(AppUtil.isXMLFileValid("110021015.xml"));
		System.out.println(AppUtil.isXMLFileValid("220019915.xml"));

 
	}
}
