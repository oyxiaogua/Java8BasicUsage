package com.basic;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.StringJoiner;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.BinaryOperator;
import java.util.function.IntFunction;
import java.util.function.LongToDoubleFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.ToDoubleBiFunction;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntBiFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;

import com.basic.JavaBasicUsageClass.Enum_ABCD;

public class JavaBasicUsageClassTest {
	private JavaBasicUsageClass javaBasicUsageClass = new JavaBasicUsageClass();

	@Test
	public void testIntegrateStream() {
		String[] strArr = { "one", "two" };
		String[] strArr2 = { "hello", null };
		List<ArrayList<String>> listOfArrayList = new ArrayList<>();
		listOfArrayList.add(new ArrayList<>(Arrays.asList(strArr)));
		listOfArrayList.add(new ArrayList<>(Arrays.asList(strArr2)));
		ArrayList<String> list = javaBasicUsageClass.integrateStream(listOfArrayList.stream());
		System.out.println(list);
	}

	@Test
	public void testStringRemove() {
		List<String> strList = new ArrayList<String>();
		strList.add("a");
		strList.add(null);
		strList.add("A");
		strList.add("b");
		javaBasicUsageClass.removeAllIgnoreCase(strList, "a");
		System.out.println(strList);
	}

	@Test
	public void testStringJoin() {
		StringJoiner stringJoiner = new StringJoiner(",", "{", "}");
		String str = stringJoiner.add("one").add("two").add("three").toString();
		System.out.println(str);

		System.out.println(String.join(",", "1", "2", "4"));
	}

	@Test(expected = NullPointerException.class)
	public void testObjects() {
		Objects.requireNonNull(null, "first is Null.");
	}

	@Test
	public void testMap() {
		Map<String, String> map = new HashMap<>();
		map.putIfAbsent("1", "hello");
		System.out.println(map.getOrDefault("1", "default_1"));
		map.replace("2", "key_2");
		System.out.println(map.getOrDefault("2", "default_2"));
		map.computeIfPresent("1", (k, v) -> k + "->" + v);
		System.out.println(map.get("1"));
		map.putIfAbsent("2", "2");
		map.merge("2", "concat", (value, newValue) -> value.concat(newValue));
		System.out.println(map.get("2"));
		map.remove("2", "2");
		System.out.println(map.get("2"));
		map.remove("2", "2concat");
		System.out.println(map.get("2"));
	}

	@Test
	public void testSortMap() {
		Map<Integer, String> testMap = new HashMap<Integer, String>();
		testMap.put(1, "c");
		testMap.put(9, "java");
		testMap.put(8, "html");
		testMap.put(2, "js");
		testMap.put(4, "php");
		Map<Integer, String> sortedMapByValue = testMap.entrySet().stream().sorted(Map.Entry.comparingByValue())
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (v1, v2) -> v1, // BinaryOperator
						LinkedHashMap::new));
		System.out.println(sortedMapByValue);
	}

	@Test
	public void testEnum() {
		EnumSet.allOf(Enum_ABCD.class).forEach(System.out::println);
		Arrays.stream(Enum_ABCD.values()).forEach(System.out::println);
	}

	@Test
	public void testArraysParallelPrefix() {
		int[] numberz2 = new int[] { 0, 1, 2, 3, 4, 5, 6, 7 };
		Arrays.parallelPrefix(numberz2, (i1, i2) -> i1 + i2);
		System.out.println(Arrays.toString(numberz2));
	}

	@Test
	public void testGenerateSequenceArr() {
		int[] resultArr = javaBasicUsageClass.generateSequenceArr(1, 3, 10);
		System.out.println(Arrays.toString(resultArr));
	}

	@Test
	public void testConvertList() {
		List<Double> doubleList = Arrays.asList(5d, 1d, 3d, null, 4d, 2d);
		List<String> list = javaBasicUsageClass.convertList(doubleList, p -> String.valueOf(p));
		System.out.println(list);
	}

	@Test
	public void testList() {
		List<Double> doubleList = Arrays.asList(5d, 1d, 3d, null, 4d, 2d);
		System.out.println(
				String.join(",", doubleList.stream().map(v -> String.valueOf(v)).collect(Collectors.toList())));
		doubleList.replaceAll(p -> p == null ? null : p * 2);
		System.out.println(doubleList);
		doubleList.sort(Comparator.nullsLast(Comparator.reverseOrder()));
		System.out.println(doubleList);

		Collection<Integer> numbers = new ArrayList<Integer>(
				Arrays.asList(null, 1, 3, 52, 47, 8, 2, 15, null, 38, 15, 7, 42, 31, null, 43, 5));
		numbers.removeIf(n -> n == null || n % 3 == 0);
		System.out.println(numbers);

		List<String> list = new ArrayList<>(Arrays.asList("alpha", "bravo", "charlie", "delta", "echo", "foxtrot"));
		list.replaceAll(String::toUpperCase);
		list.forEach(System.out::println);
	}

	@Test
	public void testFilterNull() {
		Collection<String> names = Arrays.asList("aa", "dd", "a2", "java", null, "food", "after", "before", null,
				"clean", "drop", "create", "insert", "table", "database", "mysql", "oracle");
		Collection<String> subNames = names.stream().filter(p -> p != null).parallel().distinct()
				.map(String::toUpperCase).sorted().skip(2 * 4).limit(5).collect(Collectors.toList());
		System.out.println(subNames);
		List<String> filterStrList = names.stream().filter(Objects::nonNull).collect(Collectors.toList());
		System.out.println(filterStrList);
	}

	@Test
	public void testCreateArr() {
		IntFunction<int[]> arrayCreator1 = size -> new int[size];
		int[] intArray1 = arrayCreator1.apply(5);
		System.out.println(Arrays.toString(intArray1));

		IntFunction<int[][]> TwoDimArrayCreator = int[][]::new;
		int[][] int2DimArray = TwoDimArrayCreator.apply(5);
		// int[5][] array
		int2DimArray[0] = new int[5];
		int2DimArray[1] = new int[5];
		int2DimArray[2] = new int[5];
		int2DimArray[3] = new int[5];
		int2DimArray[4] = new int[5];
		System.out.println(Arrays.deepToString(int2DimArray));

		int[] array = new int[5];
		Arrays.setAll(array, i -> 5 - i);
		System.out.println(Arrays.toString(array));

	}

	@Test
	public void testLongToDoubleFunction() {
		LongToDoubleFunction i = (l) -> Math.sin(l);
		System.out.println(i.applyAsDouble(90));
	}

	@Test
	public void testToDoubleBiFunction() {
		ToDoubleBiFunction<Integer, Long> i = (x, y) -> Math.sin(x) + Math.cos(y);
		System.out.println(i.applyAsDouble(45, 45L));
	}

	@Test
	public void testToDoubleFunction() {
		ToDoubleFunction<Integer> i = (x) -> Math.sin(x);
		System.out.println(i.applyAsDouble(45));
	}

	@Test
	public void testToIntBiFunction() {
		ToIntBiFunction<String, String> i = (x, y) -> Integer.parseInt(x) + Integer.parseInt(y);
		System.out.println(i.applyAsInt("2", "3"));
	}

	@Test
	public void testBinaryOperator() {
		BinaryOperator<Integer> maxnumber = (a, b) -> {
			if (a == null) {
				return b == null ? a : b;
			}
			if (b == null) {
				return a;
			}
			return a > b ? a : b;
		};
		System.out.println(maxnumber.apply(3, null));
		System.out.println(maxnumber.apply(null, null));
		System.out.println(maxnumber.apply(2, 1));
		System.out.println(maxnumber.apply(null, 1));

	}

	@Test
	public void testTransformNumbers() {
		List<Double> numbers = Arrays.asList(9d, 25d, 100d);
		System.out.println(javaBasicUsageClass.transformNumbers(numbers, Math::sqrt));
	}

	@Test
	public void testGetMethodInfo() {
		for (Method m : JavaBasicUsageClassTest.class.getMethods()) {
			System.out.println("----------------------------------------");
			System.out.println("   method: " + m.getName());
			System.out.println("   return: " + m.getReturnType().getName());
			for (Parameter p : m.getParameters()) {
				System.out.println("parameter: " + p.getType().getName() + ", " + p.getName());
			}
		}
	}

	@Test
	public void testBase64() throws Exception {
		String encodedBytes = Base64.getEncoder().encodeToString("hello".getBytes("utf-8"));
		System.out.println("encodedBytes " + encodedBytes);
		byte[] decodedBytes = Base64.getDecoder().decode(encodedBytes);
		System.out.println("decodedBytes " + new String(decodedBytes));
	}

	@Test
	public void testBase64NoPaddding() throws Exception {
		final String originalInput = "test input";
		final String encodedString = Base64.getEncoder().withoutPadding().encodeToString(originalInput.getBytes());
		System.out.println(encodedString);
		final byte[] decodedBytes = Base64.getDecoder().decode(encodedString);
		final String decodedString = new String(decodedBytes);
		System.out.println(decodedString);
	}

	@Test
	public void testBase64EncodeUrl() throws Exception {
		final String originalUrl = "https://www.google.co.nz/?gfe_rd=cr&ei=dzbFVf&gws_rd=ssl#q=java~!@#$%^&*()_+`-+[]{}|:\"<>?,./;'\\][=-0'";
		final String encodedUrl = Base64.getUrlEncoder().encodeToString(originalUrl.getBytes());
		System.out.println(encodedUrl);
		final byte[] decodedBytes = Base64.getUrlDecoder().decode(encodedUrl.getBytes());
		final String decodedUrl = new String(decodedBytes);
		System.out.println(decodedUrl);
	}

	@Test
	public void testBase64MimeEncoder() throws Exception {
		final String buffer = UUID.randomUUID().toString();
		final byte[] forEncode = buffer.getBytes();
		final String encodedMime = Base64.getMimeEncoder().encodeToString(forEncode);
		System.out.println(encodedMime);
		final byte[] decodedBytes = Base64.getMimeDecoder().decode(encodedMime);
		final String decodedMime = new String(decodedBytes);
		System.out.println(decodedMime);
	}

	@Test(expected = ArithmeticException.class)
	public void testToIntExact() {
		System.out.println(Long.MAX_VALUE);
		System.out.println((int) Long.MAX_VALUE);
		System.out.println(Math.toIntExact(10_00_00_000));
		System.out.println(Math.toIntExact(Long.MAX_VALUE));
		System.out.println(Math.multiplyExact(100000, 100000));
		System.out.println(Math.addExact(Integer.MAX_VALUE, Integer.MAX_VALUE));
	}

	@Test
	public void testGetRandomNumberInRange() {
		int result = javaBasicUsageClass.getRandomNumberInRange(1, 10);
		System.out.println(result);
		new Random().ints(5, 2, 10).forEach(System.out::println);
	}

	@Test
	public void testPredicate() {
		Predicate<String> isEmpty = String::isEmpty;
		System.out.println(isEmpty.test(""));
		Predicate<Integer> greaterThanTen = (i) -> i > 10;
		Predicate<Integer> lowerThanTwenty = (i) -> i < 20;
		boolean result = greaterThanTen.and(lowerThanTwenty).test(15);
		System.out.println(result);
	}

	@Test
	public void Supplier() {
		Supplier<String> macSupplier = () -> ThreadLocalRandom.current().ints(6, 0, 255)
				.mapToObj(i -> String.format("%02x", i)).collect(Collectors.joining(":"));
		Stream.generate(macSupplier).limit(5).forEach(System.out::println);
	}
}
