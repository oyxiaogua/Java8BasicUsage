package com.basic;

import java.util.Arrays;

import org.junit.Test;

public class JavaFunctionsTest {

	@Test
	public void testChunkArr(){
		int[] numberz = new int[] { 0, 1, 2, 3, 4, 5, 6, 7 };
		int[][] resultArr = JavaFunctions.chunk(numberz, 3);
		System.out.println(Arrays.deepToString(resultArr));
	}
	
	@Test
	public void testJoinArr(){
		Integer[] numberz = new Integer[] { 0, 1, 2, 3, 4, 5, 6, 7 };
		System.out.println(JavaFunctions.join(numberz, ",", "|"));
	}
}
