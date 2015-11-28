package com.basic;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.Test;

public class JavaFileUsageClassTest {
	private JavaFileUsageClass javaFileUsageClass=new JavaFileUsageClass();
	@Test
	public void testFileList() throws IOException {
		String filePath = "E:/test_tmp";
		Path workingDirectory = FileSystems.getDefault().getPath(filePath);
		Files.list(workingDirectory).forEach(System.out::println);
		Files.walk(workingDirectory).forEach(System.out::println);
	}

	@Test
	public void testGetFile() throws Exception {
		String filePath = "E:/test_tmp";
		List<File> resultList =javaFileUsageClass.getCurrentFolderFiterFile(filePath, Files::isRegularFile);
		System.out.println(resultList);
		resultList = javaFileUsageClass.getCurrentFolderFileWithFilesList(filePath, Files::isRegularFile);
		System.out.println(resultList);
		resultList = javaFileUsageClass.getAllFilterFile(filePath, Files::isRegularFile);
		System.out.println(resultList);
		javaFileUsageClass.sortFile(resultList);
		System.out.println("sort=" + resultList);
		resultList = javaFileUsageClass.getAllFileFolder(new File(filePath));
		System.out.println(resultList);
		resultList = javaFileUsageClass.getFilesEndWith(new File(filePath), "txt");
		System.out.println(resultList);
		resultList = javaFileUsageClass.getFileWithFilesWalk(filePath, Files::isRegularFile, 3);
		System.out.println(resultList);
		resultList = javaFileUsageClass.getFileWithFilesFind(filePath, Files::isRegularFile, 3);
		System.out.println(resultList);

	}
}
