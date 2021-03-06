/*******************************************************************************
 * Copyright (c) 2014, the original author or authors.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; version 3 of the License.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * A copy of the GNU General Public License accompanies this software, 
 * and is also available at http://www.gnu.org/licenses.
 *******************************************************************************/
package name.abhijitsarkar.java.java8impatient.lambda;

import static java.util.Arrays.sort;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.of;

import java.io.File;
import java.io.FileFilter;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Abhijit Sarkar
 */
public class PracticeQuestionsCh1 {
	public static final Logger LOGGER = LoggerFactory.getLogger(PracticeQuestionsCh1.class);

	/**
	 *
	 * Q2: Using the {@code listFiles(FileFilter)} and isDirectory methods of the {@code java.io.File} class, write a
	 * method that returns all subdirectories of a given directory. Use a lambda expression instead of a
	 * {@code FileFilter} object.
	 * 
	 * @param directory
	 *            Directory path for which all subdirectories are to be found.
	 * @return A list containing the names of all subdirectories of directory.
	 */
	public static List<String> listAllSubdirectoriesUsingLambda(String directory) {
		return listAllSubdirectories((file) -> file.isDirectory(), directory);
	}

	/**
	 *
	 * Q2: Using the {@code listFiles(FileFilter)} and isDirectory methods of the {@code java.io.File} class, write a
	 * method that returns all subdirectories of a given directory. Use a method expression instead of a
	 * {@code FileFilter} object.
	 * 
	 * @param directory
	 *            Directory path for which all subdirectories are to be found.
	 * @return A list containing the names of all subdirectories of directory.
	 */
	public static List<String> listAllSubdirectoriesUsingMethodExpr(String directory) {
		return listAllSubdirectories(File::isDirectory, directory);
	}

	private static List<String> listAllSubdirectories(FileFilter fileFilter, String directory) {
		File dir = null;

		URL dirURL = PracticeQuestionsCh1.class.getResource(directory);

		try {
			if (dirURL != null) {
				dir = new File(dirURL.toURI());
			}
		} catch (URISyntaxException e) {
			// no-op;
		}

		List<String> subdir = new ArrayList<>();

		if (dir != null && dir.isDirectory()) {
			subdir = listFileNames(dir.listFiles(fileFilter));
		}

		return subdir;
	}

	private static List<String> listFileNames(File[] files) {
		try (Stream<File> stream = of(files)) {
			return stream.map(File::getName).collect(toList());
		}
	}

	/**
	 * Q4: Given an array of {@code File} objects, sort it so that the directories come before the files, and within
	 * each group, elements are sorted by path name. Use a lambda expression, not a {@code Comparator}.
	 * 
	 * @param files
	 *            Array of files to be sorted.
	 */
	public static void sortFiles(File[] files) {
		LOGGER.info("Incoming array: {}.", listFileNames(files));

		sort(files, (f1, f2) -> {
			int result = 0;

			if (f1.isDirectory() && f2.isFile()) {
				result = -1;
			} else if (f1.isFile() && f2.isDirectory()) {
				result = 1;
			} else {
				result = f1.getName().compareTo(f2.getName());
			}

			LOGGER.info("Comparison result for {} and {}: {}.", f1.getName(), f2.getName(), result);

			return result;
		});

		LOGGER.info("Sorted array: {}.", listFileNames(files));
	}

	/**
	 * Q7: Write a static method {@code andThen} that takes as parameters two {@code Runnable} instances and returns a
	 * {@code Runnable} that runs the first, then the second. In the {@code main} method, pass two lambda expressions
	 * into a call to {@code andThen}, and run the returned instance.
	 * 
	 * @param r1
	 *            First Runnable.
	 * @param r2
	 *            Second Runnable.
	 * @return Runnable that runs the first, then the second.
	 */
	public static Runnable andThen(Runnable r1, Runnable r2) {
		return () -> {
			r1.run();
			r2.run();
		};
	}

	/**
	 * Q8: What happens when a lambda expression captures values in an enhanced {@code for} loop such as this one?
	 * <p>
	 * Is it legal? <i>Yes.</i>
	 * <p>
	 *                                                      Does each lambda expression capture a different value, or do
	 * they all get the last value? <i>Each one captures a different value.</i>
	 * <p>
	 * What happens if you use a traditional loop {@code for (int i = 0; i < names.length; i++)}? <i>The code does not
	 * compile because {@code i} isn't effectively final.</i>
	 * 
	 * @return List<Runnable>
	 */
	public static List<Runnable> lambdaAndEnhancedFor() {
		String[] names = { "Peter", "Paul", "Mary" };

		List<Runnable> runners = new ArrayList<>();

		for (String name : names) {
			runners.add(() -> System.out.println(name));
		}

		return runners;
	}

	/**
	 * Q11: Suppose you have a class that implements two interfaces {@code I} and {@code J}, each of which has a method
	 * {@code void f()}. Exactly what happens if {@code f} is an abstract, default or static method of {@code I} and and
	 * abstract, default or static method of {@code J}? Repeat when a class extends a superclass {@code S} and
	 * implements an interface {@code I}, each of which has a method {@code void f()}.
	 * <p>
	 * <b>Ans:</b> When implementing interfaces, methods with same signature in both results in a compilation error.
	 * <p>
	 * If the conflicting methods are both abstract, class {@code C} may be declared abstract and no implementation of
	 * {@code f} is necessary. If {@code C} is not abstract, it must provide an implementation of {@code f}.
	 * <p>
	 * If any of the conflicting methods is default, {@code C} must provide an implementation of {@code f}.
	 * <p>
	 * Static methods are not inherited and do not cause a conflict.
	 * <p>
	 * When a class extends a superclass {@code S} and implements an interface {@code I}, {@code S} wins. There is no
	 * conflict.
	 * 
	 */
	public void existsOnlyToGlorifyJavadoc() {
	}
}
