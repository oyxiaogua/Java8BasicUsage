package com.basic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.DoubleSummaryStatistics;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.StringJoiner;
import java.util.TreeMap;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.junit.Test;

import com.bean.Employee_Bean;

public class JavaStreamCollectorsUsageClassTest {
	private JavaStreamCollectorsUsageClass javaStreamCollectorsUsageClass = new JavaStreamCollectorsUsageClass();

	@Test
	public void testCollectionsSortSimple(){
		List<String> wordList=new ArrayList<String>();
		wordList.add("test_1");
		wordList.add("test_2_2");
		wordList.add("test3");
		Collections.sort(wordList,
				(s1, s2) -> Integer.compare(s1.length(), s2.length()));
		wordList.forEach(System.out::println);
	}
	@Test
	public void testCollector() {
		List<Employee_Bean> empList = getEmpIncludeNullValueList();
		Collector<Employee_Bean, StringJoiner, String> personNameCollector = Collector.of(() -> new StringJoiner("|"),
				(j, p) -> j.add(p == null ? null : p.getFirstName() == null ? null : p.getFirstName().toUpperCase()),
				(j1, j2) -> j1.merge(j2), StringJoiner::toString);
		String names = empList.stream().collect(personNameCollector);
		System.out.println(names);
	}

	@Test
	public void testCollectorsToListWithSort() {
		List<Employee_Bean> empList = getEmpIncludeNullValueList();
		Comparator<String> strComparator = Comparator.nullsLast(String::compareToIgnoreCase);
		Comparator<Integer> intComparator = Comparator.nullsLast(Comparator.naturalOrder());
		List<Employee_Bean> resultList = empList.stream().filter(p -> p != null)
				.sorted(Comparator.comparing(Employee_Bean::getFirstName, strComparator)
						.thenComparing(Employee_Bean::getAge, intComparator))
				.collect(Collectors.toList());
		System.out.println(resultList);

		empList = getEmpIncludeNullValueList();
		Collections.sort(empList, new Comparator<Employee_Bean>() {
			public int compare(Employee_Bean o1, Employee_Bean o2) {
				if (o1 == null) {
					return o2 == null ? 0 : 1;
				}
				if (o2 == null) {
					return -1;
				}
				if (o1.getFirstName() == null && o2.getFirstName() == null) {
					int jobCmp = intComparator.compare(o1.getAge(), o2.getAge());
					return jobCmp;
				}
				int nameCmp = strComparator.compare(o1.getFirstName(), o2.getFirstName());
				if (nameCmp == 0) {
					int jobCmp = intComparator.compare(o1.getAge(), o2.getAge());
					return jobCmp;
				}
				return nameCmp;
			}
		});
		System.out.println(empList);
	}

	@Test
	public void testCollectorsToList() {
		Collection<Integer> numbers = new ArrayList<>(
				Arrays.asList(null, 1, 3, 52, 47, 8, 2, 15, 38, 15, 7, 42, 31, 43, 5));
		String result = String.join(",",
				numbers.stream().filter(p -> p != null).map(Object::toString).collect(Collectors.toList()));
		System.out.println(result);
		result = String.join(",", numbers.stream().map(n -> String.valueOf(n)).collect(Collectors.toList()));
		System.out.println(result);

		Map<String, List<Integer>> evenOddSquareMap = IntStream.rangeClosed(0, 9).mapToObj(Integer::valueOf)
				.collect(Collectors.groupingBy((i) -> (i % 2 == 0) ? "even" : "odd",
						Collectors.mapping((i) -> i * i, Collectors.toList())));
		System.out.println(evenOddSquareMap);
	}

	@Test
	public void testCollectorsJoining() {
		List<String> strList = Arrays.asList(null, "one", "two", "three", "four", "etc");
		String joinResult = strList.stream().collect(Collectors.joining(","));
		System.out.println(joinResult);
		List<Integer> interList = Arrays.asList(null, 11, 21, 33, 100, 150, 200);
		joinResult = interList.stream().map(n -> String.valueOf(n)).collect(Collectors.joining(",", "[", "]"));
		System.out.println(joinResult);
		String joinStr = String.join(":", "one", null, "two", "three", "too");
		joinResult = Pattern.compile(":").splitAsStream(joinStr).filter(s -> s.contains("t")).sorted()
				.collect(Collectors.joining(","));
		System.out.println(joinResult);

		List<Employee_Bean> empList = getEmpIncludeNullList();
		String allEmployees = empList.stream().filter(p -> p != null)
				.map(emp -> String.join(",", emp.getFirstName(), String.valueOf(emp.getAge())))
				.collect(Collectors.joining("|"));
		System.out.println(allEmployees);
	}

	@Test
	public void testCollectorsSummarizing() {
		List<Integer> list = IntStream.rangeClosed(1, 100).boxed().collect(Collectors.toList());
		IntSummaryStatistics intSummaryStatistics = list.stream().collect(Collectors.summarizingInt(Integer::valueOf));
		System.out.println(intSummaryStatistics);
		List<Employee_Bean> empList = getEmpIncludeNullList();
		intSummaryStatistics = empList.stream().filter(p -> p != null)
				.mapToInt(p -> p.getAge() == null ? 0 : p.getAge()).boxed()
				.collect(Collectors.summarizingInt(value -> value));
		System.out.println(intSummaryStatistics);

		DoubleSummaryStatistics stats = Stream.generate(Math::random).limit(100)
				.collect(Collectors.summarizingDouble(Double::doubleValue));
		System.out.println(stats);

		double sum = Stream.generate(Math::random).limit(100).mapToDouble(Double::doubleValue).sum();
		System.out.println(sum);
	}

	@Test
	public void testCollectorsAveraging() {
		List<Integer> list = IntStream.rangeClosed(1, 100).boxed().collect(Collectors.toList());
		double average = list.stream().collect(Collectors.averagingInt(item -> item));
		System.out.println(average);
	}

	@Test
	public void testCollectorsToMap() {
		Stream<Integer> intStream = Stream.of(new Integer[] { 1, 2, 3, 4 });
		Map<String, Integer> intMap = intStream.collect(Collectors.toMap(i -> "key_" + i, Function.identity()));
		System.out.println(intMap);
		List<Integer> interList = Arrays.asList(null, 1, 1, 2, 3, 4, 5);
		Map<String, String> strMap = interList.stream()
				.collect(Collectors.toMap(String::valueOf, String::valueOf, (name1, name2) -> name2));
		strMap.forEach((k, v) -> System.out.println(k + ":" + v));
		Map<String, List<Integer>> interMap = interList.stream().collect(Collectors.toMap(String::valueOf, p -> {
			List<Integer> tmp = new ArrayList<Integer>();
			tmp.add(p);
			return tmp;
		} , (l1, l2) -> {
			l1.addAll(l2);
			return l1;
		}));
		System.out.println(interMap);
	}

	@Test
	public void testCollectorsToMap2() {
		List<? extends Number> wildCardList = Arrays.asList(1, 1, 2, 3D);
		Map<Integer, Number> rtnMap = wildCardList.stream().collect(Collectors.<Number, Integer, Number> toMap(
				number -> Integer.valueOf(number.intValue() + 3), number -> number, (oldValue, newValue) -> newValue));
		System.out.println(rtnMap);
	}

	@Test
	public void testCollectorsReducing() {
		List<Integer> list = IntStream.range(1, 100).boxed().collect(Collectors.toList());
		Map<String, Integer> evenOddSumMap = list.stream().collect(Collectors
				.groupingBy((i) -> (i % 2 == 0) ? "even" : "odd", Collectors.reducing(0, (i1, i2) -> i1 + i2)));
		System.out.println(evenOddSumMap);
		List<Employee_Bean> empList = getEmpIncludeNullList();
		BinaryOperator<Employee_Bean> maxAgeEmp = (a, b) -> {
			if (a.getAge() == null) {
				return b.getAge() == null ? a : b;
			}
			if (b.getAge() == null) {
				return a;
			}
			return a.getAge() >= b.getAge() ? a : b;
		};
		Map<String, Employee_Bean> maxEmpMap = empList.stream().filter(p -> p != null)
				.collect(Collectors.groupingBy(p -> String.valueOf(p.getFirstName()),
						Collectors.collectingAndThen(Collectors.reducing(maxAgeEmp), Optional::get)));
		System.out.println(maxEmpMap);
	}

	@Test
	public void testCollectorsGroupingBy() {
		String[] strArr = { "java", "c++", "c", "h5", "java", "javascript", null };
		Stream<String> stream = Stream.of(strArr);
		Map<String, Long> counterMap = stream
				.collect(Collectors.groupingBy(v -> String.valueOf(v), Collectors.counting()));
		System.out.println(counterMap);
		stream = Stream.of(strArr);
		Map<Integer, Long> wordLengthMap = stream
				.collect(Collectors.groupingBy(JavaStreamCollectorsUsageClass::getStrLength, Collectors.counting()));
		System.out.println(wordLengthMap);

		stream = Stream.of(strArr);
		Map<Integer, Long> wordLengthDescMap = stream
				.collect(Collectors.groupingBy(JavaStreamCollectorsUsageClass::getStrLength,
						() -> new TreeMap<Integer, Long>(Comparator.<Integer> naturalOrder().reversed()),
						Collectors.counting()));
		System.out.println(wordLengthDescMap);

		stream = Stream.of(strArr);
		Map<Integer, List<String>> lengthWordMap = stream
				.collect(Collectors.groupingBy(JavaStreamCollectorsUsageClass::getStrLength,
						() -> new TreeMap<Integer, List<String>>(Comparator.<Integer> naturalOrder().reversed()),
						Collectors.toList()));
		System.out.println(lengthWordMap);

		List<Employee_Bean> empList = getEmpIncludeNullList();
		empList.stream().filter(p -> p != null)
				.collect(Collectors.groupingBy(v -> String.valueOf(v.getFirstName()),
						Collectors.summingInt(v -> v.getAge() == null ? 0 : v.getAge())))
				.forEach((u, t) -> System.out.println("emp firstname=" + u + ",sum age= " + t));
		empList.stream().filter(p -> p != null)
				.collect(Collectors.groupingBy(v -> String.valueOf(v.getFirstName()), Collectors.counting()))
				.forEach((k, v) -> System.out.println("[" + k + "," + v + "]"));

		Map<String, Set<String>> resultMap = empList.stream().filter(p -> p != null)
				.collect(Collectors.groupingBy(v -> String.valueOf(v.getFirstName()), Collectors.mapping(emp -> {
					if (emp.getAge() == null) {
						return null;
					}
					if (emp.getAge() < 18) {
						return "<18";
					}
					if (emp.getAge() < 54) {
						return "<=54";
					}
					return ">54";
				} , Collectors.toSet())));
		System.out.println(resultMap);

		Map<String, Map<String, List<Employee_Bean>>> empGroupByNameAndAgeMap = empList.stream().filter(p -> p != null)
				.collect(Collectors.groupingBy(v -> String.valueOf(v.getFirstName()),
						Collectors.groupingBy((Employee_Bean emp) -> {
							if (emp.getAge() == null) {
								return "null";
							}
							if (emp.getAge() < 18) {
								return "<18";
							}
							if (emp.getAge() < 54) {
								return "<=54";
							}
							return ">54";
						})));
		System.out.println(empGroupByNameAndAgeMap);

		Map<String, List<Employee_Bean>> empGroupByNameMap = empList.stream().filter(p -> p != null)
				.collect(Collectors.groupingBy(emp -> {
					if (emp.getAge() == null) {
						return "null";
					}
					if (emp.getAge() < 18) {
						return "<18";
					}
					if (emp.getAge() < 54) {
						return "<=54";
					}
					return ">54";
				}));
		System.out.println(empGroupByNameMap);

		Map<String, Collection<Employee_Bean>> rtnMap = javaStreamCollectorsUsageClass.notNullGroupBy(empList,
				p -> p != null, v -> String.valueOf(v.getFirstName()));
		System.out.println(rtnMap);
	}

	@Test
	public void testCollectorsPartitioningBy() {
		Map<Boolean, List<Integer>> primeMap = IntStream.rangeClosed(2, 100).boxed()
				.collect(Collectors.partitioningBy(candidate -> JavaStreamCollectorsUsageClass.isPrime(candidate)));
		System.out.println(primeMap);

		List<Employee_Bean> empList = getEmpIncludeNullList();
		Map<Boolean, Employee_Bean> empMap = empList
				.stream().filter(
						p -> p != null)
				.collect(
						Collectors
								.partitioningBy(
										v -> v.getFirstName() == null
												|| v.getFirstName().length()
														% 2 == 0,
										Collectors
												.collectingAndThen(
														Collectors
																.maxBy(Comparator.comparing(Employee_Bean::getFirstName,
																		Comparator.nullsFirst(
																				Comparator.reverseOrder()))),
										Optional::get)));
		System.out.println(empMap);
	}

	@Test
	public void testCollectorsMaxBy() {
		List<String> streamSource = Arrays.asList(null, "aa", "bb", "ccc", "aa", "Ac", "BB");
		Optional<String> largestElem = streamSource.stream()
				.collect(Collectors.maxBy(Comparator.nullsFirst(String::compareTo)));
		System.out.println(largestElem.get());
	}

	@Test
	public void testCollectorsCollectingAndThen() {
		List<String> list2 = new ArrayList<String>(Arrays.asList(new String[] { null, "java", "c" }));
		List<String> results = list2.stream()
				.collect(Collectors.collectingAndThen(Collectors.toList(), Collections::unmodifiableList));
		System.out.println(results);
	}

	@Test
	public void testCollectorsToCollection() {
		String[] strArr = { "java", "c++", "c", "h5", "java", "javascript", null };
		Stream<String> stream = Stream.of(strArr);
		ArrayList<String> list = stream.filter(x -> x == null || x.length() < 5)
				.collect(Collectors.toCollection(ArrayList::new));
		System.out.println(list);

		Stream<Integer> intStream = Stream.of(new Integer[] { 1, 2, 3, 4, null });
		List<Integer> intList = intStream.collect(Collectors.toList());
		System.out.println(intList);

		intStream = Stream.of(1, 2, 3, 4, null);
		Integer[] intArray = intStream.toArray(Integer[]::new);
		System.out.println(Arrays.toString(intArray));
	}

	public List<Employee_Bean> getEmpIncludeNullList() {
		List<Employee_Bean> personsList = Arrays.asList(new Employee_Bean(1, "a", "b", 33),
				new Employee_Bean(5, "b", "c", 14), new Employee_Bean(2, "b", "c", 14), new Employee_Bean(), null,
				new Employee_Bean(7, "a2", "b2", 24), new Employee_Bean(6, "a2", "b2", 24),
				new Employee_Bean(8, "a2", "b2", 54), new Employee_Bean(3, "a2", "b2", 24));
		return personsList;
	}

	public List<Employee_Bean> getEmpIncludeNullValueList() {
		List<Employee_Bean> personsList = Arrays.asList(new Employee_Bean(1, "a", "b", 33),
				new Employee_Bean(5, "b", "c", 9), new Employee_Bean(2, "b", "c3", 14), new Employee_Bean(), null,
				new Employee_Bean(7, "d2", "b2", 25), new Employee_Bean(6, "a2", "b2", 21),
				new Employee_Bean(14, "c2", "b3", null), new Employee_Bean(11, "", "b3", null),
				new Employee_Bean(10, null, "b3", 39), new Employee_Bean(9, null, "b2", 54),
				new Employee_Bean(8, "e2", "b2", 47), new Employee_Bean(3, "a2", "b2", 36));
		return personsList;
	}
}
