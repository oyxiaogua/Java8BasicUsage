package com.basic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.DoubleUnaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;

public class JavaFunctionUsageClass {
	public DoubleUnaryOperator expandedCurriedConverter(double w, double y, double z) {
		return (double x) -> (x + w) * y + z;
	}

	public <T, E> Map<E, Collection<T>> groupBy(Collection<T> collection, Function<T, E> function) {
		return collection.stream()
				.collect(Collectors.groupingBy(function::apply, HashMap::new, Collectors.toCollection(ArrayList::new)));
	}

	public <T, R> List<R> convertListValue(List<T> list, Function<T, R> mapper) {
		final List<R> result = new ArrayList<>();
		for (final T t : list) {
			result.add(mapper.apply(t));
		}
		return result;
	}
}
