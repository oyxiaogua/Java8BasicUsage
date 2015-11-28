package com.basic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.DoubleUnaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;

import com.bean.Employee_Bean;

public class JavaFunctionUsageClassTest {
	private JavaFunctionUsageClass javaFunctionUsageClass = new JavaFunctionUsageClass();

	@Test
	public void testFunction() {
		Function<String, Integer> toInteger = Integer::valueOf;
		Function<String, Integer> calculate = toInteger.andThen((p) -> p * p).andThen((p) -> p / 2)
				.compose((p) -> p + 0);
		// "6"->"60"->60->3600->1800
		System.out.println(calculate.apply("6"));
	}

	@Test
	public void testFunctionAndThenCompose() {
		Function<Integer, Integer> times2 = e -> e * 2;
		Function<Integer, Integer> squared = e -> e * e;
		int result = times2.compose(squared).apply(3);
		System.out.println(result);
		result = times2.andThen(squared).apply(4);
		System.out.println(result);
	}

	@Test
	public void testFunctionFlat() {
		List<Integer> list1 = Arrays.asList(1, 2, 3, 4, 5, 6, 7);
		List<Integer> list2 = Arrays.asList(2, 4, 6);
		List<Integer> list3 = Arrays.asList(3, 5, 7);
		List<List<Integer>> list = Arrays.asList(list1, list2, list3);
		Function<List<Integer>, Stream<Integer>> flatFun = l -> l.stream();
		List<Integer> listResult = list.stream().flatMap(flatFun).collect(Collectors.toList());
		System.out.println(listResult);
	}

	@Test
	public void testFunctionIdentity() {
		List<String> list = new ArrayList<String>();
		list.add("java");
		list.add("c");
		list.add("lisp");
		list.add("c#");
		Map<String, Integer> map = list.stream().collect(Collectors.toMap(Function.identity(), s -> s.length()));
		System.out.println(map);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testFunctionSimpleUsage() {
		final Function<String, Predicate<String>> startsWithLetter = letter -> name -> name != null
				&& name.startsWith(letter);
		List<String> names = Arrays.asList(null, "Bob", "Juha", "John", "Steven", "Jack", "Bill", "Bert");
		List<String> resultList = names.stream().filter(startsWithLetter.apply("B"))
				.collect(Collectors.collectingAndThen(Collectors.toList(), Collections::unmodifiableList));
		System.out.println(resultList);
		resultList.add("aa");
	}

	@Test
	public void testFunctionCombination() {
		Function<Integer, Function<Integer, Function<Integer, Integer>>> f3 = i1 -> i2 -> i3 -> i1 + i2 + i3;
		System.out.println("f3.apply(1).apply(2).apply(3) : " + f3.apply(1).apply(2).apply(3));
	}

	@Test
	public void testFunctionAndGroupBy() {
		List<Employee_Bean> personsList = Arrays.asList(new Employee_Bean(1, "a", "b", 33),
				new Employee_Bean(6, "b", "c", 24), new Employee_Bean(5, "b", "c", 15),
				new Employee_Bean(2, "b", "c", 14), new Employee_Bean(3, "a2", "b2", 24));
		Function<Employee_Bean, String> myFun = Employee_Bean::getFirstName;
		Map<String, Collection<Employee_Bean>> rtnMap = javaFunctionUsageClass.groupBy(personsList, myFun);
		System.out.println(rtnMap);
	}

	@Test
	public void testConvertListValue() {
		final List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);
		final List<String> mappedList = javaFunctionUsageClass.convertListValue(list, i -> "#" + i);
		System.out.println(mappedList);
	}

	@Test
	public void testDoubleUnaryOperator() {
		DoubleUnaryOperator convertFtoC = javaFunctionUsageClass.expandedCurriedConverter(1, 2, 3);
		System.out.println(convertFtoC.applyAsDouble(4));

		DoubleUnaryOperator dl = (x) -> {
			return x * x;
		};
		System.out.println(dl.applyAsDouble(3));
	}

	@Test
	public void testUnaryOperator() {
		UnaryOperator<String> i = (x) -> x.toUpperCase();
		System.out.println(i.apply("hello"));
	}

	@Test
	public void testFunctionApply() {
		Function<String, String> func4 = String::new;
		System.out.println(func4.apply("hello"));

		Function<String[], List<String>> asList = Arrays::<String> asList;
		System.out.println(asList.apply(new String[] { "a", "b", "c" }));

	}
}
