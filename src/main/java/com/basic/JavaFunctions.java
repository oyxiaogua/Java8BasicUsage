package com.basic;

import java.util.AbstractMap.SimpleEntry;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.function.IntBinaryOperator;
import java.util.function.IntPredicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class JavaFunctions {
	public static int[][] chunk(int[] numbers, int size) {
		return IntStream.iterate(0, i -> i + size).limit((long) Math.ceil((double) numbers.length / size)).mapToObj(
				cur -> Arrays.copyOfRange(numbers, cur, cur + size > numbers.length ? numbers.length : cur + size))
				.toArray(int[][]::new);
	}

	public static int[] deepFlatten(Object[] input) {
		return Arrays.stream(input).flatMapToInt(o -> {
			if (o instanceof Object[]) {
				return Arrays.stream(deepFlatten((Object[]) o));
			}
			return IntStream.of((Integer) o);
		}).toArray();
	}

	public static Object[] flattenDepth(Object[] elements, int depth) {
		if (depth == 0) {
			return elements;
		}
		return Arrays
				.stream(elements).flatMap(el -> el instanceof Object[]
						? Arrays.stream(flattenDepth((Object[]) el, depth - 1)) : Arrays.stream(new Object[] { el }))
				.toArray();

	}

	public static int[] difference(int[] first, int[] second) {
		Set<Integer> set = Arrays.stream(second).boxed().collect(Collectors.toSet());
		return Arrays.stream(first).filter(v -> !set.contains(v)).toArray();
	}

	public static int[] differenceWith(int[] first, int[] second, IntBinaryOperator comparator) {
		return Arrays.stream(first).filter(a -> Arrays.stream(second).noneMatch(b -> comparator.applyAsInt(a, b) == 0))
				.toArray();
	}

	public static int[] dropElements(int[] elements, IntPredicate condition) {
		while (elements.length > 0 && !condition.test(elements[0])) {
			elements = Arrays.copyOfRange(elements, 1, elements.length);
		}
		return elements;
	}

	public static int[] dropRight(int[] elements, int n) {
		if (n < 0) {
			throw new IllegalArgumentException("n is less than 0");
		}
		return n < elements.length ? Arrays.copyOfRange(elements, 0, elements.length - n) : new int[0];
	}

	public static <T> String join(T[] arr, String separator, String end) {
		return IntStream.range(0, arr.length).mapToObj(i -> new SimpleEntry<>(i, arr[i])).reduce("", (acc,
				val) -> val.getKey() == arr.length - 1 ? acc + val.getValue() + end : acc + val.getValue() + separator,
				(fst, snd) -> fst);
	}

	public static List<Class<?>> getAllInterfaces(Class<?> cls) {
		return Stream
				.concat(Arrays.stream(cls.getInterfaces())
						.flatMap(intf -> Stream.concat(Stream.of(intf), getAllInterfaces(intf).stream())),
				cls.getSuperclass() == null ? Stream.empty() : getAllInterfaces(cls.getSuperclass()).stream())
				.distinct().collect(Collectors.toList());
	}
}
