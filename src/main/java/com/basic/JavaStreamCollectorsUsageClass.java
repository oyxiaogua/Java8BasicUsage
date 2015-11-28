package com.basic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class JavaStreamCollectorsUsageClass {
	public static int getStrLength(String str) {
		if (str == null) {
			return -1;
		}
		return str.length();
	}

	public static boolean isPrime(int num) {
		return IntStream.rangeClosed(2, (int) Math.sqrt(num) + 1).noneMatch(i -> num % i == 0);
	}

	public <T, E> Map<E, Collection<T>> notNullGroupBy(Collection<T> collection, Predicate<? super T> fiter,
			Function<T, E> function) {
		return collection.stream().filter(fiter)
				.collect(Collectors.groupingBy(function::apply, HashMap::new, Collectors.toCollection(ArrayList::new)));
	}

}
