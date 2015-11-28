package com.basic;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.atomic.LongAdder;
import java.util.concurrent.locks.StampedLock;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.Test;

public class JavaConcurrentUsageClassTest {
	private JavaConcurrentUsageClass javaConcurrentUsageClass = new JavaConcurrentUsageClass();

	@Test
	public void testStampedLockOptimisticRead() {
		ExecutorService executor = Executors.newFixedThreadPool(2);
		StampedLock lock = new StampedLock();
		executor.submit(() -> {
			long stamp = lock.tryOptimisticRead();
			try {
				System.out.println(stamp + " Optimistic Lock Valid: " + lock.validate(stamp));
				javaConcurrentUsageClass.sleep(1);
				System.out.println(stamp + " Optimistic Lock Valid: " + lock.validate(stamp));
				javaConcurrentUsageClass.sleep(2);
				System.out.println(stamp + " Optimistic Lock Valid: " + lock.validate(stamp));
			} finally {
				lock.unlock(stamp);
			}
		});

		executor.submit(() -> {
			long stamp = lock.writeLock();
			try {
				System.out.println(stamp + " Write Lock acquired");
				javaConcurrentUsageClass.sleep(2);
			} finally {
				lock.unlock(stamp);
				System.out.println("Write done");
			}
		});
		javaConcurrentUsageClass.stop(executor, 5);
	}

	@Test
	public void testscheduleAtFixedRate() throws Exception {
		ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
		Runnable task = () -> System.out.println("Scheduling: " + System.nanoTime());
		int initialDelay = 0;
		int period = 1;
		executor.scheduleAtFixedRate(task, initialDelay, period, TimeUnit.SECONDS);
		javaConcurrentUsageClass.sleep(2);
		javaConcurrentUsageClass.stop(executor, 5);
	}

	@Test
	public void testScheduledExecuto() throws Exception {
		ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
		Runnable task = () -> System.out.println("Scheduling: " + System.nanoTime());
		ScheduledFuture<?> future = executor.schedule(task, 300, TimeUnit.MICROSECONDS);
		TimeUnit.MILLISECONDS.sleep(2002);
		long remainingDelay = future.getDelay(TimeUnit.MILLISECONDS);
		System.out.println(remainingDelay);
		javaConcurrentUsageClass.stop(executor, 5);
	}

	@Test
	public void testInvokeAll() throws Exception {
		ExecutorService executor;
		executor = Executors.newWorkStealingPool();
		List<Callable<String>> callables = Arrays.asList(() -> "task1", () -> "task2", () -> "task3");
		executor.invokeAll(callables).stream().map(future -> {
			try {
				return future.get();
			} catch (Exception e) {
				throw new IllegalStateException(e);
			}
		}).forEach(System.out::println);
		javaConcurrentUsageClass.stop(executor, 5);
	}

	@Test(expected = TimeoutException.class)
	public void testFutureTimeOut() throws Exception {
		ExecutorService executor = Executors.newFixedThreadPool(1);
		Future<Integer> future = (Future<Integer>) executor.submit(() -> {
			try {
				TimeUnit.SECONDS.sleep(2);
				return 123;
			} catch (InterruptedException e) {
				throw new IllegalStateException("task interrupted", e);
			}
		});
		System.out.println(future.get(1, TimeUnit.SECONDS));
		javaConcurrentUsageClass.stop(executor, 5);
	}

	@Test
	public void testLongAdder() {
		LongAdder adder = new LongAdder();
		ExecutorService executor = Executors.newFixedThreadPool(2);
		IntStream.range(0, 100).forEach(i -> executor.submit(adder::increment));
		javaConcurrentUsageClass.stop(executor, 5);
		System.out.println(adder.sumThenReset());
	}

	@Test
	public void testAtomicIntegerAccumulateAndGet() {
		AtomicInteger atomicInt = new AtomicInteger(0);
		ExecutorService executor = Executors.newFixedThreadPool(2);
		IntStream.range(0, 1000).forEach(i -> {
			Runnable task = () -> atomicInt.accumulateAndGet(i, (n, m) -> n + m);
			executor.submit(task);
		});
		javaConcurrentUsageClass.stop(executor, 5);
		System.out.println(atomicInt.get());
	}

	@Test
	public void testAtomicIntegerUpdateAndGet() {
		AtomicInteger atomicInt = new AtomicInteger(0);
		ExecutorService executor = Executors.newFixedThreadPool(2);
		IntStream.range(0, 1000).forEach(i -> {
			Runnable task = () -> atomicInt.updateAndGet(n -> n + 2);
			executor.submit(task);
		});
		javaConcurrentUsageClass.stop(executor, 5);
		System.out.println(atomicInt.get());
	}

	@Test
	public void testAtomicIntegerIncrementAndGet() {
		AtomicInteger atomicInt = new AtomicInteger(0);
		ExecutorService executor = Executors.newFixedThreadPool(2);
		IntStream.range(0, 1000).forEach(i -> executor.submit(atomicInt::incrementAndGet));
		javaConcurrentUsageClass.stop(executor, 5);
		System.out.println(atomicInt.get());
	}

	@Test
	public void testAtomicIntegerArray() {
		List<String> words = Arrays.asList("c", "js", "html", "javascript", "c++", "java");
		AtomicIntegerArray shortWords = new AtomicIntegerArray(8);
		words.parallelStream().forEach(w -> {
			if (w.length() < 8)
				shortWords.getAndIncrement(w.length());
		});

		Map<Integer, Long> shortWordsMap = words.parallelStream().filter(w -> w.length() < 12)
				.collect(Collectors.groupingBy(String::length, Collectors.counting()));
		System.out.println(shortWordsMap);
	}

	@Test
	public void testGetFactorialWithJava8() {
		javaConcurrentUsageClass.getFactorialWithJava8(8);
	}

	@Test
	public void testGetMax() {
		List<String> words = Arrays.asList("", "a", "aa", "aaa", "null", null, "testss");
		System.out.println(javaConcurrentUsageClass.getMax(words, s -> Long.valueOf(s == null ? -1 : s.length())));
		System.out.println(javaConcurrentUsageClass.getMax2(words, s -> Long.valueOf(s == null ? -1 : s.length())));
	}

	@Test
	public void testParallelWithCompletableFuture() throws Exception {
		ForkJoinPool forkJoinPool = new ForkJoinPool(5);
		CompletableFuture<List<Integer>> futurePrimesList = CompletableFuture.supplyAsync(() -> IntStream.range(1, 10)
				.parallel().filter(JavaConcurrentUsageClass::mockLongTimeRun).boxed().collect(Collectors.toList()),
				forkJoinPool);
		System.out.println(futurePrimesList.get());
		forkJoinPool.awaitTermination(10, TimeUnit.SECONDS);
		forkJoinPool.shutdown();
	}

	@Test
	public void testParallelForkJoinPool() throws Exception {
		ForkJoinPool forkJoinPool = new ForkJoinPool(5);
		forkJoinPool.submit(() -> {
			IntStream.range(1, 10).parallel().forEach((number) -> {
				try {
					Thread.sleep(1000);
				} catch (Exception e) {
				}
				System.out.println(number);
			});
		});
		forkJoinPool.awaitTermination(8, TimeUnit.SECONDS);
		forkJoinPool.shutdown();
	}
}
