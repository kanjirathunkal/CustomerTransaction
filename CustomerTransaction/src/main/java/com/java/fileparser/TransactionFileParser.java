package com.java.fileparser;

import com.java.util.FileUtils;
import com.java.transaction.*; 
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.util.List;

public class TransactionFileParser {

	private static final Logger log = LoggerFactory.getLogger(TransactionFileParser.class);

	  /**
	   * Read the customer transaction file and process it into
	   * a  TransactionFile object.
	   * TransactionFile instance representing the contents of
	   * the customer transaction file
	   */
	  public static TransactionFile fromPath(Path path) {
	    TransactionFile transactionFile = new TransactionFile(path);

	    double start = System.currentTimeMillis();
	    List<String> lines = FileUtils.readLinesFromFile(path);

	    // Skip the header line
	    if (lines.size() > 0) {
	      lines.remove(0);
	    }

	    for (String line : lines) {
	      transactionFile.addTransaction(processLine(line));
	    }

	    log.info("Processed {} transactions in {}ms", lines.size(), System.currentTimeMillis() - start);
	    return transactionFile;
	  }

	  /**
	   * Process a single line from a customer transaction file and return a
	   *  Transaction instance representing the transaction. The line should
	   * be of the following format:
	   *
	   *   56455464554, 600.00
	   *
	   * where the first comma-delimited token is a customer account number, and
	   * the second is a transaction amount.
	   *
	   * If the customer account number is non-numeric or the format is violated in
	   * any other way, the transaction will be skipped (null returned).
	   *
	  
	   */
	  private static Transaction processLine(String line) {
	    String[] parts = line.replace(" ", "").split(",");

	    if (parts.length != 2) {
	      return null;
	    }

	    String customerAccountNumber = parts[0];
	    if (!StringUtils.isNumeric(customerAccountNumber)) {
	      return null;
	    }

	    String transactionAmount = parts[1];
	    if (!NumberUtils.isCreatable(transactionAmount)) {
	      return null;
	    }

	    return new Transaction(Integer.parseInt(customerAccountNumber), Double.parseDouble(transactionAmount));
	  }
	
}
