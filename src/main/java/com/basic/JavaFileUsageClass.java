package com.basic;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JavaFileUsageClass {
	public void sortFile(List<File> fileList) {
		Collections.sort(fileList, (o1, o2) -> {
			if (o1.isDirectory() && !o2.isDirectory())
				return -1;
			else if (!o1.isDirectory() && o2.isDirectory())
				return 1;

			return o1.compareTo(o2);
		});
	}

	public List<File> getFileWithFilesFind(String filePath, Predicate<Path> pred, int maxDepth) throws IOException {
		Path start = Paths.get(filePath);
		List<File> resultList = new ArrayList<File>();
		try (Stream<Path> stream = Files.find(start, maxDepth, (path, attr) -> pred.test(path))) {
			resultList = stream.map(p -> p.toFile()).collect(Collectors.toList());
		}
		return resultList;
	}

	public List<File> getFileWithFilesWalk(String filePath, Predicate<Path> pred, int maxDepth) throws IOException {
		Path start = Paths.get(filePath);
		List<File> resultList = new ArrayList<File>();
		try (Stream<Path> stream = Files.walk(start, maxDepth)) {
			resultList = stream.filter(pred).map(p -> p.toFile()).collect(Collectors.toList());
		}
		return resultList;
	}

	public List<File> getCurrentFolderFileWithFilesList(String filePath, Predicate<Path> pred) throws IOException {
		Path start = Paths.get(filePath);
		List<File> resultList = new ArrayList<File>();
		try (Stream<Path> stream = Files.list(start)) {
			resultList = stream.filter(pred).map(p -> p.toFile()).collect(Collectors.toList());
		}
		return resultList;
	}

	public List<File> getFilesEndWith(File dir, String extension) {
		return Arrays.asList(dir.listFiles((d, n) -> n.endsWith("." + extension)));
	}

	public List<File> getAllFileFolder(File file) {
		List<File> dirs = new ArrayList<File>();
		for (File d : file.listFiles(File::isDirectory)) {
			dirs.add(d);
			dirs.addAll(getAllFileFolder(d));
		}
		return dirs;
	}

	public List<File> getAllFilterFile(String filePath, Predicate<Path> pred) throws Exception {
		return Files.walk(Paths.get(filePath)).filter(pred).map(p -> p.toFile()).collect(Collectors.toList());
	}

	public List<File> getCurrentFolderFiterFile(String filePath, Predicate<Path> pred) {
		List<File> resultList = new ArrayList<File>();
		try {
			Files.newDirectoryStream(Paths.get(filePath)).forEach(p -> {
				if (pred.test(p)) {
					resultList.add(p.toFile());
				}
			});
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return resultList;
	}

	public boolean isExists(Path p) {
		return Files.exists(p, new LinkOption[] { LinkOption.NOFOLLOW_LINKS });
	}

	public String readFileContent(Path path) {
		try {
			String contents = new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
			return contents;
		} catch (Exception e) {
		}
		return null;
	}
}
