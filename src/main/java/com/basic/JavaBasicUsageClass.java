package com.basic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JavaBasicUsageClass {
	public int[] generateSequenceArr(int initValue, int diff, int length) {
		int[] sequence = new int[length];
		Arrays.setAll(sequence, index -> {
			return index == 0 ? initValue : initValue + index * diff;
		});
		return sequence;
	}

	public int getRandomNumberInRange(int min, int max) {
		Random r = new Random();
		return r.ints(min, (max + 1)).limit(1).findFirst().getAsInt();
	}

	public List<String> transformNumbers(List<Double> numbers, Function<Double, Double> fx) {
		List<String> appliedNumbers = new ArrayList<String>();
		for (Double n : numbers) {
			appliedNumbers.add(String.valueOf(fx.apply(n)));
		}
		return appliedNumbers;
	}

	public void removeAllIgnoreCase(Iterable<String> collection, final String value) {
		removeAll(collection, new java.util.function.Predicate<String>() {
			public boolean test(String item) {
				if (value == null) {
					return item == null;
				}
				if (item == null) {
					return false;
				}
				return value.equalsIgnoreCase(item);
			}
		});
	}

	public <T> void removeAll(Iterable<T> collection, Predicate<T> predicate) {
		Iterator<T> iterator = collection.iterator();
		while (iterator.hasNext()) {
			T item = iterator.next();
			if (predicate.test(item)) {
				iterator.remove();
			}
		}
	}

	public <T> ArrayList<T> integrateStream(Stream<ArrayList<T>> streamOfArrayList) {
		ArrayList<T> integratedList = streamOfArrayList.reduce(new ArrayList<T>(), (r, e) -> {
			r.addAll(e);
			return r;
		});
		return integratedList;
	}

	public <T, U> List<U> convertList(List<T> list, Function<T, U> mapper) {
		return list.stream().map(mapper).collect(Collectors.toList());
	}

	enum Enum_ABCD {
		A, B, C, D;
	}
}
