package com.basic;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.LongAccumulator;
import java.util.function.Function;
import java.util.function.LongBinaryOperator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class JavaConcurrentUsageClass {
	public void getFactorialWithJava8(int x2) {
		LongBinaryOperator op = (x, y) -> x * (y + 1);
		LongAccumulator accumulator = new LongAccumulator(op, 1L);
		ExecutorService executor = Executors.newFixedThreadPool(1);
		IntStream.range(0, x2).forEach(i -> executor.submit(() -> accumulator.accumulate(i)));
		stop(executor, 5);
		System.out.println(accumulator.getThenReset());
	}

	public long getMax2(Collection<String> elements, Function<String, Long> toLong) {
		LongAccumulator maxLength = new LongAccumulator(Math::max, 0);
		elements.stream().forEach(w -> {
			maxLength.accumulate(toLong.apply(w));
		});
		return maxLength.get();
	}

	public long getMax(Collection<String> elements, Function<String, Long> toLong) {
		LongAccumulator maxFinder = new LongAccumulator((observed, newVal) -> observed > newVal ? observed : newVal,
				Long.MIN_VALUE);
		elements.stream().forEach(elm -> maxFinder.accumulate(toLong.apply(elm)));
		return maxFinder.get();
	}

	@SuppressWarnings("unchecked")
	public <T> CompletableFuture<List<T>> allOf(CompletableFuture<T>... cfs) {
		CompletableFuture<Void> allDoneFuture = CompletableFuture.allOf(cfs);
		return allDoneFuture
				.thenApply(it -> Arrays.stream(cfs).map(future -> future.join()).collect(Collectors.toList()));
	}

	public void sleep(int seconds) {
		try {
			TimeUnit.SECONDS.sleep(seconds);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void stop(ExecutorService executor, int waitSecond) {
		try {
			executor.shutdown();
			executor.awaitTermination(waitSecond, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			System.err.println("tasks interrupted");
		} finally {
			if (!executor.isTerminated()) {
				System.err.println("cancel non-finished tasks");
			}
			executor.shutdownNow();
			System.out.println("shutdown finished");
		}
	}

	public static boolean mockLongTimeRun(int i) {
		try {
			TimeUnit.SECONDS.sleep(new Random().nextInt(i % 3 == 0 ? 1 : i % 3) + 1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new Random().nextBoolean();
	}
}
