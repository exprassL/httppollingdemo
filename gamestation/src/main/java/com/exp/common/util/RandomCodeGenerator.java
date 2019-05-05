package com.exp.common.util;

import java.util.Random;

public class RandomCodeGenerator {

	private static final Random R = new Random();
	
	public static String randomCode(int length) {
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < length; i++) {
			sb.append(R.nextInt(10));
		}
		return sb.toString();
	}
	
}
