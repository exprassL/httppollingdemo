package com.exp.common.util;

import java.util.Random;

public class ArrayUtil {

	public static <T> T[] disorderRandomly(T[] arr) {
		int len = arr.length;
		Random rand = new Random();
		int tempi;
		T temp;
		for (int i = 0; i < len; i++) {
			tempi = rand.nextInt(len);
			temp = arr[tempi];
			arr[tempi] = arr[i];
			arr[i] = temp;
		}
		return arr;
	}
}
