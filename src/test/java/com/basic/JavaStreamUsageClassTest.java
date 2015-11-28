package com.basic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.LongSupplier;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import org.junit.Test;

public class JavaStreamUsageClassTest {
	@Test
	public void testGenerateStream() {
		Stream<String> stream = Stream.generate(() -> "test").limit(3);
		stream.forEach(System.out::println);

		Stream<Double> doubleStream = Stream.generate(Math::random).limit(3);
		Double[] doubleArr = doubleStream.toArray(Double[]::new);
		System.out.println(Arrays.toString(doubleArr));

		String sentence = "hello world.";
		Stream<String> wordStream = Pattern.compile("\\W").splitAsStream(sentence);
		String[] wordArr = wordStream.toArray(String[]::new);
		System.out.println(Arrays.toString(wordArr));
	}

	@Test
	public void testStreamConcat() {
		String[] arr1 = { "1", "2", "3", null };
		String[] arr2 = { null, "4", "5", "6" };
		Stream<String> stream1 = Stream.of(arr1);
		Stream<String> stream2 = Stream.of(arr2);
		Stream<String> stream3 = Stream.concat(stream1, stream2);
		String[] arr = stream3.toArray(String[]::new);
		System.out.println(Arrays.toString(arr));

		String[] arr3 = { "7", "8", null };
		stream1 = Stream.of(arr1);
		stream2 = Stream.of(arr2);
		stream3 = Stream.of(arr3);
		Stream<String> stream = Stream.of(stream1, stream2, stream3).flatMap(x -> x);
		arr = stream.toArray(String[]::new);
		System.out.println(Arrays.toString(arr));
	}

	@Test
	public void testStreamMap() {
		List<Double> pointsToMarks = Arrays.asList(1d, 2d, 3d, 4d, 5d, null);
		System.out.println(
				String.join(",", pointsToMarks.stream().map(v -> String.valueOf(v)).collect(Collectors.toList())));
	}

	@Test
	public void testSum() {
		Stream<Integer> stream = Stream.of(1, 3, 5, 2, 7, 3, 11);
		Integer intSum = stream.reduce(0, (sum, p) -> {
			return sum += p;
		} , (sum1, sum2) -> {
			return sum1 + sum2;
		});
		System.out.println(intSum);
		stream = Stream.of(1, 3, 5, 2, 7, 3, 11);
		intSum = stream.reduce(0, (sum, p) -> sum += p, (sum1, sum2) -> (sum1 + sum2));
		System.out.println(intSum);
	}

	@Test
	public void testIntMax() {
		List<Integer> data = new ArrayList<Integer>(
				Arrays.asList(new Integer[] { -4, 0, 1, 3, 2, 56, 99, 123, 21, 34, 54, 22, 123 }));
		int max = Integer.MIN_VALUE;
		max = data.stream().reduce(0, (a, b) -> Integer.max(a, b));
		System.out.println(max);

		max = data.stream().max(Integer::compare).get();
		System.out.println(max);
	}

	@Test
	public void testIntStream() {
		IntStream.rangeClosed(1, 3).forEach(System.out::println);
		IntStream.iterate(0, i -> i + 2).skip(3).limit(3).forEach(System.out::println);
		IntStream.generate(() -> ThreadLocalRandom.current().nextInt(10)).limit(3).forEach(System.out::println);
		int max = IntStream.range(1, 5).max().getAsInt();
		System.out.println(max);

		max = IntStream.range(1, 5).reduce(0, Math::max);
		System.out.println(max);
	}

	@Test
	public void testIntStream2() {
		List<Integer> intList = Arrays.asList(1, 2, 3, 4, 5);
		List<Boolean> boolList = Arrays.asList(true, false, true, true, false);

		List<Integer> collect = IntStream.range(0, intList.size()).filter(i -> boolList.get(i))
				.mapToObj(i -> intList.get(i)).collect(Collectors.toList());
		System.out.println(collect);
	}

	@Test
	public void testDoubleStream() {
		DoubleStream stream = IntStream.range(1, 5).mapToDouble(i -> i);
		stream.forEach(System.out::println);
	}

	@Test
	public void testFibonacci() {
		LongSupplier fib = new LongSupplier() {
			private long previous = 0;
			private long current = 1;

			public long getAsLong() {
				long nextValue = this.previous + this.current;
				this.previous = this.current;
				this.current = nextValue;
				return this.previous;
			}
		};
		LongStream.generate(fib).limit(20).forEach(System.out::println);
		Stream.iterate(new long[] { 0, 1 }, t -> new long[] { t[1], t[0] + t[1] }).skip(1).limit(20).map(t -> t[0])
				.forEach(System.out::println);
	}

	@Test
	public void testFlatMap() {
		List<Integer> list1 = Arrays.asList(1, 2, 3, 4, 5, 6, 7);
		List<Integer> list2 = Arrays.asList(2, 4, 6);
		List<Integer> list3 = Arrays.asList(3, 5, 7);
		List<List<Integer>> list = Arrays.asList(list1, list2, list3);
		transform(list).forEach(System.out::println);

		List<Integer> numbers1 = Arrays.asList(1, 2, 3, 4, 5);
		List<Integer> numbers2 = Arrays.asList(6, 7, 8);
		List<int[]> pairs = numbers1.stream()
				.flatMap((Integer i) -> numbers2.stream().map((Integer j) -> new int[] { i, j }))
				.filter(pair -> (pair[0] + pair[1]) % 3 == 0).collect(Collectors.toList());
		pairs.forEach(pair -> System.out.println("(" + pair[0] + ", " + pair[1] + ")"));

		Stream<int[]> pythagoreanTriples = IntStream.rangeClosed(1, 100).boxed()
				.flatMap(a -> IntStream.rangeClosed(a, 100).filter(b -> Math.sqrt(a * a + b * b) % 1 == 0).boxed()
						.map(b -> new int[] { a, b, (int) Math.sqrt(a * a + b * b) }));
		pythagoreanTriples.forEach(t -> System.out.println(t[0] + ", " + t[1] + ", " + t[2]));
	}

	public <T> List<T> transform(List<List<T>> collection) {
		return collection.stream().flatMap(value -> value.stream()).collect(Collectors.toList());
	}

	@Test
	public void testStreamMatch() {
		Stream<Integer> numbersStream = Stream.of(1, 2, 3, 4, 5);
		System.out.println("Stream contains 4?= " + numbersStream.anyMatch(i -> i == 4));

		Stream<Integer> numbersStream2 = Stream.of(1, 2, 3, 4, 5);
		System.out.println("Stream all elements less than 10?= " + numbersStream2.allMatch(i -> i < 10));

		Stream<Integer> numbersStream3 = Stream.of(1, 2, 3, 4, 5);
		System.out.println("Stream doesn't contain 10? " + numbersStream3.noneMatch(i -> i == 10));
	}

	@Test
	public void testFilterByPattern() {
		Pattern pattern = Pattern.compile(".*@qq\\.com");
		long resultInt = Stream.of("aa@qq.com", "bb@qq2.com").filter(pattern.asPredicate()).count();
		System.out.println(resultInt);
	}

	@Test
	public void testGetFactorialWithJava8Reduce() {
		System.out.println(getFactorialWithJava8Reduce(8));
	}

	@Test
	public void testReverseIntStream() {
		int[] arr = new int[] { 1, 2, 4, 3, 4, 5, 6, 7, 8 };
		IntStream.range(1, arr.length + 1).boxed().mapToInt(i -> arr[arr.length - i])
				.forEach(e -> System.out.print(e + ","));
		System.out.println();
	}

	public long getFactorialWithJava8Reduce(long n) {
		return LongStream.rangeClosed(1, n).reduce(1, (long a, long b) -> a * b);
	}
}
