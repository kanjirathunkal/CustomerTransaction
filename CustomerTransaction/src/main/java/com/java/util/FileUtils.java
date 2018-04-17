package com.java.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static java.util.stream.Collectors.toList;


public class FileUtils {

	private static final Logger log = LoggerFactory.getLogger(FileUtils.class);

	  /**
	   * List all files in the given directory path, excluding subdirectories.
	  */
	  public static List<Path> listFiles(Path path) {
	    log.info("Loading files from directory at {}", path);

	    try (Stream<Path> paths = Files.walk(path)) {
	      return paths.filter(Files::isRegularFile).collect(toList());
	    } catch (IOException e) {
	      throw new RuntimeException("Error reading files from " + path, e);
	    }
	  }

	  /**
	   * Read the file at the given path and return a list of strings representing
	   * the lines of the file.

	   */
	  public static List<String> readLinesFromFile(Path path) {
	    log.info("Loading lines from file at {}", path);

	    try (Stream<String> lines = Files.lines(path)) {
	      return lines.collect(toList());
	    } catch (IOException e) {
	      throw new RuntimeException("Error reading file " + path, e);
	    }
	  }

	  /**
	   * Write the given content as a file to the given path.
	   */
	  public static void writeFile(Path path, String content) {
	    log.info("Writing file at {}", path);

	    try {
	      Files.write(path, content.getBytes());
	    } catch (IOException e) {
	      throw new RuntimeException("Error writing file " + path, e);
	    }
	  }

	  /**
	   * Move a file from one position on the filesystem to another.
	   */
	  public static void moveFile(Path from, Path to) {
	    try {
	      Files.move(from, to, REPLACE_EXISTING);  
	    } catch (IOException e) {
	      throw new RuntimeException("Error moving file from " + from + " to " + to, e);
	    }
	  }
	
	
}
